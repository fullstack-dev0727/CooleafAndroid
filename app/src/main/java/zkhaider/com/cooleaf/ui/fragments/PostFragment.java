package zkhaider.com.cooleaf.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.mvp.feeds.interactors.IFeedInteractor;
import zkhaider.com.cooleaf.mvp.feeds.presenters.FeedPresenter;
import zkhaider.com.cooleaf.ui.activities.CommentActivity;
import zkhaider.com.cooleaf.ui.activities.ImagePreviewActivity;
import zkhaider.com.cooleaf.ui.adapters.SearchItemAdapter;
import zkhaider.com.cooleaf.ui.helpers.PaletteCache;
import zkhaider.com.cooleaf.ui.listeners.OnImageClickListener;
import zkhaider.com.cooleaf.ui.listeners.OnLongPressListener;
import zkhaider.com.cooleaf.ui.listeners.OnPostCardTouchListener;
import zkhaider.com.cooleaf.utils.LocalPreferences;

/**
 * Created by Valerie on 6/10/2015.
 */
public class PostFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener,
        OnPostCardTouchListener, OnLongPressListener, IFeedInteractor, OnImageClickListener {

    private static final String TAG = PostFragment.class.getSimpleName();
    private static final int PER_PAGE = 25;

    @InjectView(R.id.searchPostFragmentLayout)
    RelativeLayout mLinearLayout;

    @InjectView(R.id.searchPostsSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.searchPostsRecyclerView)
    RecyclerView mRecyclerView;

    @InjectView(R.id.progressOverlay)
    View mOverlay;

    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;

    private static PostFragment mPostFragment;

    private LinearLayoutManager mLayoutManager;
    private SearchItemAdapter mSearchItemAdapter;
    private boolean mIsDisabled = false;
    private Interest mInterest;
    private String mFeedableType;
    private int mDeletedFeedPosition;

    private FeedPresenter mFeedPresenter;

    // Pagination Variables
    private int page = 1;
    private boolean loading = false;
    int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;

    /******************************************************************************************
     *  Constructor Methods
     ******************************************************************************************/

    public static PostFragment getInstance() {
        if (mPostFragment == null)
            mPostFragment = new PostFragment();
        return mPostFragment;
    }

    /******************************************************************************************
     *  Fragment Lifecycle Methods
     ******************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSwipeRefreshLayout.setEnabled(!mIsDisabled);
        initSwipeRefreshLayout();
        mFeedPresenter.registerOnBus();
    }

    @Override
    public void onPause() {
        mFeedPresenter.unregisterOnBus();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_post, container, false);
        ButterKnife.inject(this, rootView);

        initBundle();
        initPresenter();
        initRecyclerView();

        initRecyclerViewScrollListener();
        addItemDecoration();
        return rootView;
    }

    /******************************************************************************************
     *  Initialization Methods
     ******************************************************************************************/

    private void initBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String interestJSON = bundle.getString("interest");
            mInterest = gson.fromJson(interestJSON, Interest.class);
            mFeedableType = bundle.getString("feedable_type");
        }
    }

    private void initPresenter() {
        mFeedPresenter = new FeedPresenter(this);
        if (mInterest != null && mFeedableType != null) {
            mFeedPresenter.setFeedableType(mFeedableType);
            refreshContent();
        }
    }

    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mSearchItemAdapter = new SearchItemAdapter(getActivity(), false);
        mSearchItemAdapter.setOnPostCardTouchListener(this);
        mSearchItemAdapter.setOnImageClickListener(this);
        mSearchItemAdapter.setOnLongPressListener(this);
        mRecyclerView.setAdapter(mSearchItemAdapter);
    }

    private void initSwipeRefreshLayout() {
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

    private void initRecyclerViewScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                lastVisibleItem = mLayoutManager.findLastCompletelyVisibleItemPosition();

                int pageMultiplier = page * PER_PAGE;

                if (!loading) {
                    if ((pageMultiplier == totalItemCount) && (lastVisibleItem == pageMultiplier - 1)) {
                        loading = true;
                        page += 1;
                        mFeedPresenter.loadMoreFeeds(mInterest.getId(), page, PER_PAGE);
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                }
            }
        });
    }

    /******************************************************************************************
     *  Overriden Interface Methods
     ******************************************************************************************/

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            mSwipeRefreshLayout.setEnabled(true);
        } else {
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    @Override
    public void onCardTap(View view, int position) {
        showCommentActivity(mSearchItemAdapter.getPost(position),
                mSearchItemAdapter.getComments(position));
    }

    @Override
    public void loadedFeeds(List<Post> posts) {
        if (mSearchItemAdapter == null || mRecyclerView == null || mSwipeRefreshLayout == null)
            return;
        int position = mLayoutManager.findFirstVisibleItemPosition();
        if (page != 1) {
            mSearchItemAdapter.addPosts(posts);
        } else {
            mSearchItemAdapter.setPosts(posts);
        }
        mRecyclerView.setAdapter(mSearchItemAdapter);
        mRecyclerView.invalidate();
        mRecyclerView.scrollToPosition(position);
        mSwipeRefreshLayout.setRefreshing(false);
        loading = false;
    }

    @Override
    public void addPost(Post post) {
        if (mSearchItemAdapter != null) {
            mSearchItemAdapter.addPost(post);
            mRecyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void previewImage(int position) {
        Post post = mSearchItemAdapter.getPost(position);
        if (post.getAttachment() != null) {
            Intent intent = new Intent(getActivity(), ImagePreviewActivity.class);
            intent.putExtra("imageurl", post.getAttachment().getVersions().getMobileURL());
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onLongPress(int position) {
        Post post = mSearchItemAdapter.getPost(position);
        int userId = LocalPreferences.getInt("user_id");
        if (userId == post.getUserId())
            showDeleteView(position, post);
    }

    @Override
    public void onDeletedFeed() {
        stopProgress();
        mSearchItemAdapter.removePost(mDeletedFeedPosition);
    }

    /******************************************************************************************
     *  Helper Methods
     ******************************************************************************************/

    private void refreshContent() {
        if (page == 1) {
            mFeedPresenter.loadFeeds(mInterest.getId());
        } else {
            page = 1;
            mFeedPresenter.loadFeeds(mInterest.getId());
        }
    }

    private void addItemDecoration() {
        // Add padding to end
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int margin = (int) getResources().getDimension(R.dimen.activity_vertical_margin);

                final int itemPosition = parent.getChildAdapterPosition(view);
                if (itemPosition == RecyclerView.NO_POSITION)
                    return;

                final int itemCount = state.getItemCount();

                // Last Position
                /** last position */
                if (itemCount > 0 && itemPosition == itemCount - 1) {
                    outRect.set(margin, margin, margin, margin);
                }
                /** positions before last */
                else {
                    outRect.set(margin, margin, margin, 0);
                }
        }
    });
    }

    public AppBarLayout.OnOffsetChangedListener getOffsetChangedListener() {
        return this;
    }

    private void showCommentActivity(Post parentPost, List<Post> comments) {
        if (comments != null && mInterest != null) {
            Intent i = new Intent(getActivity(), CommentActivity.class);
            Bundle bundle = new Bundle();
            Gson gson = new Gson();

            int vibrantColor = 0;
            int darkVibrantColor = 0;
            if (PaletteCache.getInstance().getVibrantColorCache() != null && PaletteCache.getInstance().getVibrantColorCache().containsKey(mInterest.getName()))
                vibrantColor = PaletteCache.getInstance().getVibrantColorCache().get(mInterest.getName());
            if (PaletteCache.getInstance().getDarkVibrantColorCache() != null && PaletteCache.getInstance().getVibrantColorCache().containsKey(mInterest.getName()))
                darkVibrantColor = PaletteCache.getInstance().getVibrantColorCache().get(mInterest.getName());

            LocalPreferences.set("current_feed_id", parentPost.getId());

            bundle.putString("parent_post", gson.toJson(parentPost));
            bundle.putString("comments", gson.toJson(comments));
            bundle.putString("group_name", mInterest.getName());
            bundle.putInt("vibrant_color", vibrantColor);
            bundle.putInt("dark_vibrant_color", darkVibrantColor);
            i.putExtras(bundle);
            startActivity(i);
            this.getActivity().overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_in_bottom);
        }
    }

    private void showDeleteView(final int position, final Post post) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Feed?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFeed(position, post);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void deleteFeed(int position, Post post) {
        startProgress();
        mDeletedFeedPosition = position;
        mFeedPresenter.removeFeed(post.getId());
    }

    public void startProgress() {
        mOverlay.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void stopProgress() {
        mOverlay.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

}
