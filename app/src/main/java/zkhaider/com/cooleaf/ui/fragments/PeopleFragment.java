package zkhaider.com.cooleaf.ui.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.ui.events.LoadPeopleEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedUsersEvent;
import zkhaider.com.cooleaf.ui.activities.MyProfileActivity;
import zkhaider.com.cooleaf.ui.adapters.UsersAdapter;
import zkhaider.com.cooleaf.ui.itemdecoration.SpacesItemDecoration;

/**
 * Created by Haider on 2/20/2015.
 */
public class PeopleFragment extends Fragment {

    public static final String TAG = PeopleFragment.class.getSimpleName();

    private static final int PER_PAGE = 25;

    private RecyclerView mRecyclerView;
    private UsersAdapter mUsersAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Bus mBus;
    private boolean mShowToolbar = false;
    private String mTitle = "";
    private int mColor;

    // Pagination Variables
    private int page = 1;
    private boolean loading = false;
    int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_people, null);

        initToolBar(root);
        initAdapter(root);
        initSwipeRefresh(root);
        initItemTouchListener();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshContent();
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setBus(Bus bus) {
        mBus = bus;
        mBus.register(this);
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    private void initToolBar(View root) {
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.peopleToolbar);
        toolbar.setBackgroundColor(mColor);
        if (Build.VERSION.SDK_INT >= 21)
            toolbar.setElevation(12);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mTitle);
    }

    private void initAdapter(View root) {
        mUsersAdapter = new UsersAdapter();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.peopleRecyclerView);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                lastVisibleItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

                int pageMultiplier = page * PER_PAGE;

                if (!loading) {
                    if ((pageMultiplier == totalItemCount) && (lastVisibleItem == pageMultiplier - 1)) {
                        loading = true;
                        page += 1;
                        mBus.post(new LoadPeopleEvent(page, PER_PAGE));
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                }

            }
        });
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mUsersAdapter);
    }

    private void initSwipeRefresh(View root) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.peopleSwipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                refreshContent();
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initItemTouchListener() {
        final GestureDetector mGestureDetector = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());

                if (child != null && mGestureDetector.onTouchEvent(e)) {

                    int position = rv.getChildAdapterPosition(child);

                    User user = mUsersAdapter.getUser(position);

                    Intent i = new Intent(getActivity(), MyProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id", user.getId());
                    i.putExtras(bundle);
                    startActivity(i);

                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }

        });

    }

    @Subscribe
    public void onUsersLoaded(LoadedUsersEvent loadedUserEvent) {
        List<User> users = loadedUserEvent.getUsers();
        int position = mLinearLayoutManager.findFirstVisibleItemPosition();
        if (page != 1) {
            mUsersAdapter.addUsers(users);
        } else {
            mUsersAdapter.setUsers(users);
        }

        mRecyclerView.setAdapter(mUsersAdapter);
        mRecyclerView.invalidate();
        mRecyclerView.scrollToPosition(position);
        mSwipeRefreshLayout.setRefreshing(false);
        loading = false;
    }

    private void refreshContent() {
        if (page == 1) {
            page = 1;
            BusProvider.getInstance().post(new LoadPeopleEvent(page));
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
