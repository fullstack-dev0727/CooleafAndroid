package zkhaider.com.cooleaf.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchQuery;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.mvp.authentication.events.DeauthorizeEvent;
import zkhaider.com.cooleaf.mvp.events.interactors.IEventSearchInteractor;
import zkhaider.com.cooleaf.mvp.events.presenters.EventPresenter;
import zkhaider.com.cooleaf.mvp.feeds.interactors.IFeedSearchInteractor;
import zkhaider.com.cooleaf.mvp.feeds.presenters.FeedPresenter;
import zkhaider.com.cooleaf.mvp.interests.interactors.IInterestSearchInteractor;
import zkhaider.com.cooleaf.mvp.interests.presenters.InterestPresenter;
import zkhaider.com.cooleaf.mvp.search.interactors.ISearchInteractor;
import zkhaider.com.cooleaf.mvp.search.presenters.SearchPresenter;
import zkhaider.com.cooleaf.ui.activities.CommentActivity;
import zkhaider.com.cooleaf.ui.activities.EventDetailActivity;
import zkhaider.com.cooleaf.ui.activities.FailureActivity;
import zkhaider.com.cooleaf.ui.activities.GroupDetailActivity;
import zkhaider.com.cooleaf.ui.activities.MyProfileActivity;
import zkhaider.com.cooleaf.ui.activities.UserLoginActivity;
import zkhaider.com.cooleaf.ui.adapters.SearchAdapter;
import zkhaider.com.cooleaf.ui.helpers.PaletteCache;
import zkhaider.com.cooleaf.ui.helpers.UIConstants;
import zkhaider.com.cooleaf.ui.itemdecoration.SpacesItemDecoration;
import zkhaider.com.cooleaf.ui.listeners.OnSearchItemTouchListener;
import zkhaider.com.cooleaf.utils.AutoCompleteHelper;
import zkhaider.com.cooleaf.utils.LocalPreferences;

/**
 * Created by ZkHaider on 10/5/15.
 */
public class SearchFragment extends Fragment implements ISearchInteractor, IEventSearchInteractor,
        IInterestSearchInteractor, IFeedSearchInteractor, SearchView.OnQueryTextListener,
        OnSearchItemTouchListener {

    private static final String TAG = SearchFragment.class.getSimpleName();

    private static SearchFragment mSearchFragment;

    private RelativeLayout mParentLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar mSearchProgressBar;
    private static AutoCompleteTextView mAutoCompleteTextView;

    private SearchPresenter mSearchPresenter;
    private FeedPresenter mFeedPresenter;
    private InterestPresenter mInterestPresenter;
    private EventPresenter mEventPresenter;
    private Context mContext;
    private String mScope;
    private String mQuery;

    // Pagination Variables
    private int page = 1;
    private int perPage = 25;
    private boolean loading = false;
    int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;

    public static SearchFragment getInstance() {
        if (mSearchFragment == null)
            mSearchFragment = new SearchFragment();
        return mSearchFragment;
    }

    public void setScope(String scope) {
        this.mScope = scope;
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    /********************************************************************************************
     *  Fragment LifeCycle methods
     ********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, null);
        this.setHasOptionsMenu(true);
        initActionBar();
        initViews(root);
        initSearchPresenter();
        initEventPresenter();
        initInterestPresenter();
        initFeedPresenter();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mInterestPresenter.registerOnBus();
        mFeedPresenter.registerOnBus();
        mSearchPresenter.registerOnBus();
        mEventPresenter.registerOnBus();
    }

    @Override
    public void onPause() {
        mInterestPresenter.unregisterOnBus();
        mFeedPresenter.unregisterOnBus();
        mEventPresenter.unregisterOnBus();
        mSearchPresenter.unregisterOnBus();
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);
        initSearchView(menu);
        initAutoCompleteTextView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getActivity().finish();
                break;
            case R.id.action_search:
                item.expandActionView();
                break;
            case R.id.action_logout:
                goToUserLogin();
                BusProvider.getInstance().post(new DeauthorizeEvent());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /********************************************************************************************
     *  Initialization methods
     ********************************************************************************************/

    private void initActionBar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search");
        if (Build.VERSION.SDK_INT >=21)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
    }

    private void initViews(View root) {
        mParentLayout = (RelativeLayout) root.findViewById(R.id.searchFragmentMainContent);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.searchSwipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.searchRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                lastVisibleItem = mLayoutManager.findLastCompletelyVisibleItemPosition();

                // Page number times results per page
                int pageMultiplier = page * 25;

                if (!loading) {
                    if ((pageMultiplier == totalItemCount) && (lastVisibleItem == pageMultiplier - 1)) {
                        loading = true;
                        page += 1;
                        mSearchPresenter.searchQuery(mQuery, mScope, page, perPage);
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                }

            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(getActivity()));

        // Declare and set adapter
        mAdapter = new SearchAdapter(mContext);
        mAdapter.setOnSearchItemTouchListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initSearchPresenter() {
        mSearchPresenter = new SearchPresenter(this);
    }

    private void initEventPresenter() {
        mEventPresenter = new EventPresenter(this);
    }

    private void initInterestPresenter() {
        mInterestPresenter = new InterestPresenter(this);
    }

    private void initFeedPresenter() {
        mFeedPresenter = new FeedPresenter(this);
    }

    private void initSearchView(Menu menu) {
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        searchView.setIconified(false);

        mAutoCompleteTextView = (AutoCompleteTextView)
                searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        ColorStateList colorStateList = getResources().getColorStateList(R.color.ripple_drawable_text);
        if (Build.VERSION.SDK_INT >= 21)
            mAutoCompleteTextView.getDropDownBackground().setTintList(colorStateList); // TODO - Needs a workaround

        mAutoCompleteTextView.setDropDownBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.abc_popup_background_mtrl_mult));
    }

    private void initAutoCompleteTextView() {
        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(mParentLayout.getWindowToken(), 0);
                String result = parent.getAdapter().getItem(position).toString();
                onQueryTextSubmit(result);
            }
        });
        mAutoCompleteTextView.setThreshold(2);
    }

    /********************************************************************************************
     *  SearchView.onQueryTextListener methods
     ********************************************************************************************/

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (s.length() > 0) {
            mQuery = s;
            if (mSearchProgressBar == null)
                showSearchProgressDialog(s);
            mSearchPresenter.searchQuery(s, mScope, page, 25);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String s) {
        if (s.length() > 3) {
            mQuery = s;
            Handler handler = new Handler();
            int timeDelay = 1500;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mSearchProgressBar == null)
                        showSearchProgressDialog(s);
                    mSearchPresenter.searchQuery(s, mScope, page, 25);
                }
            }, timeDelay);
        }
        return false;
    }

    /********************************************************************************************
     *  ISearchInteractor methods
     ********************************************************************************************/

    @Override
    public void initSearchResult(List<SearchQuery> results) {

        if (mSearchProgressBar != null)
            stopSearchProgressDialog();

        List<String> suggestions = AutoCompleteHelper.getAutoCompleteQueries(results);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, suggestions);

        if (mAutoCompleteTextView != null)
            mAutoCompleteTextView.setAdapter(adapter);

        int position = mLayoutManager.findFirstVisibleItemPosition();
        if (page != 1) {
            mAdapter.addQueries(results);
        } else {
            mAdapter.setQueries(results);
        }

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.invalidate();
        mRecyclerView.scrollToPosition(position);
        mSwipeRefreshLayout.setRefreshing(false);
        loading = false;
    }

    @Override
    public void onSearchError(String message) {
        if (mSearchProgressBar != null)
            stopSearchProgressDialog();
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .show();
    }

    /********************************************************************************************
     *  IEventSearchInteractor methods
     ********************************************************************************************/

    @Override
    public void gotEventFromSearch(Event event) {
        Intent i = new Intent(getActivity(), EventDetailActivity.class);

        int userId = LocalPreferences.getInt("user_id");

        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putString("event", gson.toJson(event));
        bundle.putInt("user_id", userId);
        i.putExtras(bundle);

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    /********************************************************************************************
     *  IInterestSearchInteractor methods
     ********************************************************************************************/

    @Override
    public void gotInterestFromSearch(Interest interest) {

        Intent i = new Intent(getActivity(), GroupDetailActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();

        String userString = LocalPreferences.getString("user");
        bundle.putString("user", userString);
        bundle.putString("interest", gson.toJson(interest));
        i.putExtras(bundle);

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    /********************************************************************************************
     *  IFeedSearchInteractor methods
     ********************************************************************************************/

    @Override
    public void gotFeedFromSearch(Post post) {
        Intent i = new Intent(getActivity(), CommentActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();

        int vibrantColor = getActivity().getResources().getColor(R.color.colorPrimary);
        int darkVibrantColor = getActivity().getResources().getColor(R.color.colorPrimaryDark);

        LocalPreferences.set("current_feed_id", post.getId());

        bundle.putString("parent_post", gson.toJson(post));
        bundle.putInt("vibrant_color", vibrantColor);
        bundle.putInt("dark_vibrant_color", darkVibrantColor);

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);

        i.putExtras(bundle);
        startActivity(i);
        this.getActivity().overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_in_bottom);
    }

    /********************************************************************************************
     *  OnSearchItemTouchListener methods
     ********************************************************************************************/

    @Override
    public void onCardTap(View view, int position) {
        Log.d(TAG, "card tap");
        SearchQuery query = mAdapter.getQuery(position);
        String type = query.getType();
        switch (type) {
            case UIConstants.USER:
                int userId = query.getId();
                goToUserProfile(userId);
                break;
            case UIConstants.EVENT:
                int eventId = query.getId();
                goToEventDetail(eventId);
                break;
            case UIConstants.GROUP:
                Log.d(TAG, "Group Tap");
                int groupId = query.getId();
                goToGroupDetail(groupId);
                break;
            case UIConstants.FEED:
                int feedId = query.getId();
                goToFeedDetail(feedId);
                break;
            default:
                break;
        }
    }

    /********************************************************************************************
     *  Accessory methods
     ********************************************************************************************/

    private void showSearchProgressDialog(String s) {
        mSearchProgressBar = new ProgressBar(getActivity());
        mSearchProgressBar.setIndeterminate(true);
        mSearchProgressBar.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mParentLayout.addView(mSearchProgressBar, params);
    }

    private void stopSearchProgressDialog() {
        mSearchProgressBar.setIndeterminate(false);
        mSearchProgressBar.setVisibility(View.GONE);
        mSearchProgressBar = null;
    }

    /********************************************************************************************
     *  Navigation methods
     ********************************************************************************************/

    private void goToUserProfile(int userId) {
        Intent i = new Intent(getActivity(), MyProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("user_id", userId);
        i.putExtras(bundle);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void goToEventDetail(int eventId) {
        mEventPresenter.getEvent(eventId);
    }

    private void goToGroupDetail(int interestId) {
        mInterestPresenter.loadInterest(interestId);
    }

    private void goToFeedDetail(int feedId) {
        mFeedPresenter.loadFeedFromSearch(feedId);
    }

    protected void goToUserLogin() {
        Intent userLoginIntent = new Intent(getActivity(), UserLoginActivity.class);
        userLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        userLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(userLoginIntent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
