package zkhaider.com.cooleaf.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.cooleafapi.entities.FilePreview;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.mvp.comments.interactors.ICommentInteractor;
import zkhaider.com.cooleaf.mvp.comments.presenters.CommentPresenter;
import zkhaider.com.cooleaf.mvp.filepreviews.events.LoadUploadFileEvent;
import zkhaider.com.cooleaf.mvp.filepreviews.interactors.IFilePreviewInteractor;
import zkhaider.com.cooleaf.mvp.filepreviews.presenters.FilePreviewPresenter;
import zkhaider.com.cooleaf.ui.activities.ImagePreviewActivity;
import zkhaider.com.cooleaf.ui.adapters.EventCommentsAdapter;
import zkhaider.com.cooleaf.ui.helpers.UIConstants;
import zkhaider.com.cooleaf.ui.itemdecoration.SpacesItemDecoration;
import zkhaider.com.cooleaf.ui.listeners.OnImageClickListener;
import zkhaider.com.cooleaf.ui.listeners.OnLongPressListener;
import zkhaider.com.cooleaf.ui.listeners.OnPostCardTouchListener;
import zkhaider.com.cooleaf.ui.viewholders.FeedHeaderViewHolder;
import zkhaider.com.cooleaf.utils.LocalPreferences;

/**
 * Created by ZkHaider on 10/7/15.
 */

// TODO - Refactor this class and FeedDetailFragment into single class and use it in inheritance
public class EventCommentsFragment extends CameraFragment implements ICommentInteractor,
        IFilePreviewInteractor, OnPostCardTouchListener, OnImageClickListener, OnLongPressListener {

    private static final String TAG = EventCommentsFragment.class.getSimpleName();

    // Base layouts for Comments
    private CoordinatorLayout mCoordinatorLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private EventCommentsAdapter mCommentsAdapter;
    private Resources mResources;

    // Reply box
    protected View mRoot;
    protected RelativeLayout mRelativeLayout;
    protected EditText mReplyEditText;
    protected ImageButton mUploadFileButton;
    protected ImageButton mReplyButton;
    protected View mOverlay;
    protected ProgressBar mCommentProgressBar;

    // IVar Objects
    protected CommentPresenter mCommentPresenter;
    protected FilePreviewPresenter mFilePreviewPresenter;
    protected int mEventId;
    protected int mUserId;
    private boolean mIsUndo;
    private boolean mIsEdit;
    private int mVibrantColor;
    private int mUpdatedPosition;

    // Pagination Variables
    private int page = 1;
    private boolean loading = false;
    int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;

    /**
     * ***************************************************************************************
     * Constructor and Class Getter / Setter Methods
     * ****************************************************************************************
     */

    public void setEventId(int eventId) {
        this.mEventId = eventId;
    }

    public void setVibrantColor(int vibrantColor) {
        this.mVibrantColor = vibrantColor;
    }

    /**
     * ***************************************************************************************
     * Fragment LifeCycle Methods
     * ****************************************************************************************
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCommentPresenter();
        initFilePreviewPresenter();
        mUserId = LocalPreferences.getInt("user_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_comment_popup, null);
        mResources = mRoot.getContext().getResources();
        initViews(mRoot);
        initSwipeRefresh(mRoot);
        initRecyclerView();
        initItemTouchCallback();
        initDialogViews(mRoot);
        initReplyListener();
        return mRoot;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Register presenters
        mCommentPresenter.registerOnBus();
        mFilePreviewPresenter.registerOnBus();

        // onResume refresh for comments - most likely user has just landed on comments
        mCommentPresenter.loadComments(UIConstants.EVENT, mEventId, page, UIConstants.PER_PAGE);
    }

    @Override
    public void onPause() {
        // Unregister Presenters
        mFilePreviewPresenter.unregisterOnBus();
        mCommentPresenter.unregisterOnBus();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PHOTO_REQUEST:
                if (resultCode == Activity.RESULT_OK && !mIsEdit) {
                    showContainer();
                    BusProvider.getInstance().post(new LoadUploadFileEvent(mTypedFile));
                } else if (resultCode == Activity.RESULT_OK) {
                    BusProvider.getInstance().post(new LoadUploadFileEvent(mTypedFile));
                }
                break;
            case GET_FROM_GALLERY:
                if (resultCode == Activity.RESULT_OK && !mIsEdit) {
                    showContainer();
                    BusProvider.getInstance().post(new LoadUploadFileEvent(mTypedFile));
                } else if (resultCode == Activity.RESULT_OK) {
                    BusProvider.getInstance().post(new LoadUploadFileEvent(mTypedFile));
                }
                break;
            default:
                Toast.makeText(getActivity(), R.string.generalError, Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * ***************************************************************************************
     * OnPostCardTouchListener Methods
     * ****************************************************************************************
     */

    @Override
    public void onCardTap(final View view, final int position) {

        final Post post = mCommentsAdapter.getComment(position);

        if (post.getUserId() == mUserId && position != 0) {
            mIsEdit = true;

            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View v = layoutInflater.inflate(R.layout.feed_new_dialog, null);
            TextInputLayout textInputLayout = (TextInputLayout) v.findViewById(R.id.newFeedTextInputLayout);
            textInputLayout.setHint("Edit Post");
            final EditText postEditText = (EditText) v.findViewById(R.id.newFeedEditText);
            final ImageButton addFileButton = (ImageButton) v.findViewById(R.id.addFileButton);
            postEditText.setText(post.getContent());

            mAttachment = (ImageView) v.findViewById(R.id.photoAttachment);
            mRemoveImageButton = (ImageButton) v.findViewById(R.id.removeimage);
            mRemoveImageButton.setVisibility(View.GONE);
            mProgressBar = (ProgressBar) v.findViewById(R.id.fileProgress);
            addFileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showListNoTitle();
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setView(v)
                    .setPositiveButton(mResources.getString(R.string.save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!postEditText.getText().toString().trim().isEmpty()) {
                                String content = postEditText.getText().toString().trim();
                                updateCommentEvent(position, post, content);
                            }
                            mIsEdit = false;
                            initDialogViews(mRoot);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            mIsEdit = false;
                            initDialogViews(mRoot);
                        }
                    });

            if (post.getAttachment() != null) {
                mAttachment.setVisibility(View.VISIBLE);
                mAttachment.getLayoutParams().height = mResources.getDimensionPixelSize(R.dimen.inner_box_height);
                String attachmentUrl = post.getAttachment().getVersions().getMobileURL();
                Picasso.with(getActivity())
                        .load(attachmentUrl)
                        .fit()
                        .centerCrop()
                        .into(mAttachment);
            }

            builder.show();
        }
    }

    /**
     * ***************************************************************************************
     * OnImageClickListener Methods
     * ****************************************************************************************
     */

    @Override
    public void previewImage(int position) {
        Post post = mCommentsAdapter.getComment(position);
        if (post.getAttachment() != null) {
            Intent intent = new Intent(getActivity(), ImagePreviewActivity.class);
            intent.putExtra("imageurl", post.getAttachment().getVersions().getMobileURL());
            getActivity().startActivity(intent);
        }
    }

    /**
     * ***************************************************************************************
     * ICommentInteractor Methods
     * ****************************************************************************************
     */

    @Override
    public void initComments(List<Post> comments) {
        mProgressBar.setVisibility(View.GONE);
        int position = mLinearLayoutManager.findFirstVisibleItemPosition();
        if (page != 1) {
            mCommentsAdapter.addCommments(comments);
        } else {
            mCommentsAdapter.setComments(comments);
        }

        mRecyclerView.setAdapter(mCommentsAdapter);
        mRecyclerView.invalidate();
        mRecyclerView.scrollToPosition(position);
        mSwipeRefreshLayout.setRefreshing(false);
        loading = false;
    }

    @Override
    public void addedNewComment(Post comment) {
        stopNewCommentProgress();
        mCommentsAdapter.addItem(comment);
        mRecyclerView.scrollToPosition(mCommentsAdapter.getItemCount() - 1);
    }

    @Override
    public void updatedComment(Post comment) {
        mCommentsAdapter.updateItem(mUpdatedPosition, comment);
        mRecyclerView.scrollToPosition(mUpdatedPosition);
    }

    @Override
    public void deletedComment(Post post) {

    }

    /**
     * ***************************************************************************************
     * IFilePreviewInteractor Methods
     * ****************************************************************************************
     */

    @Override
    public void initFilePreviews(FilePreview filePreview) {
        mFilePreview = filePreview;
        aspectBitmap();
    }

    /**
     * ***************************************************************************************
     * OnLongPressListener Methods
     * ****************************************************************************************
     */

    @Override
    public void onLongPress(int position) {
        Post post = mCommentsAdapter.getComment(position);
        if (mUserId == post.getUserId())
            showDeleteView(position, post);
    }

    /**
     * ***************************************************************************************
     * Initializer Methods
     * ****************************************************************************************
     */

    private void initCommentPresenter() {
        mCommentPresenter = new CommentPresenter(this);
    }

    private void initFilePreviewPresenter() {
        mFilePreviewPresenter = new FilePreviewPresenter(this);
    }

    private void initViews(View root) {
        mRecyclerView = (RecyclerView) root.findViewById(R.id.commentRecyclerView);
        mCoordinatorLayout = (CoordinatorLayout) root.findViewById(R.id.commentCoordinatorLayout);
        mRelativeLayout = (RelativeLayout) root.findViewById(R.id.commentRelativeLayout);
        mReplyEditText = (EditText) root.findViewById(R.id.replyEditText);
        mUploadFileButton = (ImageButton) root.findViewById(R.id.uploadPhoto);
        mReplyButton = (ImageButton) root.findViewById(R.id.replyButton);
        (root.findViewById(R.id.commentReplyContainer)).setBackgroundColor(mVibrantColor);
        mOverlay = root.findViewById(R.id.progressOverlay);
        mCommentProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        mImageContainer = (RelativeLayout) root.findViewById(R.id.commentPhotoUploadContainer);
        mImageContainer.setBackgroundColor(mVibrantColor);
        mCommentProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);
    }

    private void initSwipeRefresh(View root) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.commentSwipeToRefresh);
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

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(getActivity()));
        mRecyclerView.setNestedScrollingEnabled(false);
        mCommentsAdapter = new EventCommentsAdapter(getActivity());
        mCommentsAdapter.setOnPostCardTouchListener(this);
        mCommentsAdapter.setOnImageClickListener(this);
        mCommentsAdapter.setOnLongPressListener(this);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                lastVisibleItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

                int pageMultiplier = page * UIConstants.PER_PAGE;

                if (!loading) {
                    if ((pageMultiplier == totalItemCount) && (lastVisibleItem == pageMultiplier - 1)) {
                        loading = true;
                        page += 1;
                        mCommentPresenter.loadMoreComments(UIConstants.EVENT, mEventId, page, UIConstants.PER_PAGE);
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                }

            }
        });
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mCommentsAdapter);
    }

    private void initItemTouchCallback() {
        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Post post = mCommentsAdapter.removeItem(position);
                mCommentsAdapter.notifyItemRemoved(position);
                showSnackBar(position, post);
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                Post post = mCommentsAdapter.getComment(position);
                if (post.getUserId() == mUserId && !(viewHolder instanceof FeedHeaderViewHolder))
                    return super.getSwipeDirs(recyclerView, viewHolder);
                else
                    return 0;
            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void initDialogViews(View root) {
        mAttachment = (ImageView) root.findViewById(R.id.commentPhotoUpload);
        mRemoveImageButton = (ImageButton) root.findViewById(R.id.commentRemoveImage);
        mProgressBar = (ProgressBar) root.findViewById(R.id.commentFileProgress);
    }

    private void initReplyListener() {
        mReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEditText();
            }
        });
        mUploadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListNoTitle();
            }
        });
    }

    /**
     * ***************************************************************************************
     * Accessory Methods
     * ****************************************************************************************
     */

    private void showSnackBar(final int position, final Post post) {

        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, mResources.getString(R.string.item_removed), Snackbar.LENGTH_LONG)
                .setAction(mResources.getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCommentsAdapter.addItem(position, post);
                        mIsUndo = true;
                    }
                });

        // Setup the dismiss listener
        snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                mIsUndo = false;
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (!mIsUndo) {
                    mCommentPresenter.removeComment(UIConstants.EVENT, mEventId, post.getId());
                }
            }
        });

        // Show the SnackBar
        snackbar.show();
    }

    private void refreshContent() {
        if (page == 1) {
            Log.d(TAG, "Page is 1");
            page = 1;
            mCommentPresenter.loadComments(UIConstants.EVENT, mEventId, page, UIConstants.PER_PAGE);
            mProgressBar = new ProgressBar(getActivity());
            mProgressBar.setIndeterminate(true);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * ***************************************************************************************
     * Helper Methods
     * ****************************************************************************************
     */

    private void checkEditText() {
        if (mReplyEditText.getText().toString().trim().isEmpty()) {
            // Create dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.content_is_empty)
                    .setPositiveButton(android.R.string.ok, null)
                    .setCancelable(true)
                    .create()
                    .show();
            hideKeyboard();
            clearTextField();
        } else {
            sendNewCommentEvent();
            hideContainer();
            startNewCommentProgress();
            hideKeyboard();
            clearTextField();
        }
    }

    private void sendNewCommentEvent() {
        String content = mReplyEditText.getText().toString().trim();
        String fileCache = (mFilePreview.getFileCache() != null) ? mFilePreview.getFileCache() : null;
        String original = (mFilePreview.getOriginal() != null) ? mFilePreview.getOriginal() : null;
        String thumb = (mFilePreview.getVersions() != null) ? mFilePreview.getVersions().getThumb() : null;
        mCommentPresenter.addComment(UIConstants.EVENT, mEventId, content, fileCache, original, thumb);
        mFilePreview = new FilePreview();
    }

    private void updateCommentEvent(int position, Post post, String content) {
        mUpdatedPosition = position;
        String fileCache = (mFilePreview.getFileCache() != null) ? mFilePreview.getFileCache() : null;
        String original = (mFilePreview.getOriginal() != null) ? mFilePreview.getOriginal() : null;
        String thumb = (mFilePreview.getVersions() != null) ? mFilePreview.getVersions().getThumb() : null;
        boolean removeAttachment = false;
        mCommentPresenter.updateComment(UIConstants.EVENT, mEventId, post.getId(), content, fileCache, original, thumb, removeAttachment);
        mFilePreview = new FilePreview();
    }

    private void clearTextField() {
        mReplyEditText.setText("");
        mReplyEditText.clearFocus();
    }

    public void startNewCommentProgress() {
        mOverlay.setVisibility(View.VISIBLE);
        mCommentProgressBar.setVisibility(View.VISIBLE);
    }

    public void stopNewCommentProgress() {
        mOverlay.setVisibility(View.GONE);
        mCommentProgressBar.setVisibility(View.GONE);
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showDeleteView(final int position, final Post post) {
        new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setTitle("Delete Comment?")
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
        Post removedPost = mCommentsAdapter.removeItem(position);
        showSnackBar(position, removedPost);
    }

}
