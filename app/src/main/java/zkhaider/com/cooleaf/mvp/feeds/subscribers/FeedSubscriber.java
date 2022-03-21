package zkhaider.com.cooleaf.mvp.feeds.subscribers;

import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zkhaider.com.cooleaf.base.BaseSubscriber;
import zkhaider.com.cooleaf.mvp.comments.events.CreateNewCommentEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.CreateNewFeedEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadFeedEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadFeedsEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadedCreateNewFeedEvent;
import zkhaider.com.cooleaf.cooleafapi.events.LoadedErrorEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadedFeedEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadedFeedsEvent;
import zkhaider.com.cooleaf.mvp.comments.events.LoadedPostCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.RemoveCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.UpdateCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.UpdatedCommentEvent;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.mvp.feeds.controllers.FeedController;
import zkhaider.com.cooleaf.mvp.feeds.events.RemoveFeedEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.RemovedFeedEvent;
import zkhaider.com.cooleaf.mvp.search.events.GetFeedFromSearch;
import zkhaider.com.cooleaf.mvp.search.events.GotFeedFromSearch;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class FeedSubscriber extends BaseSubscriber {

    private FeedController mFeedController = new FeedController();

    @Subscribe
    public void onLoadFeedsEvent(LoadFeedsEvent loadFeedsEvent) {

        String feedType = loadFeedsEvent.getFeedType();
        int interestId = loadFeedsEvent.getInterestId();
        final int page = loadFeedsEvent.getPage();
        int perPage = loadFeedsEvent.getPerPage();

        mFeedController.getSpecificFeed(feedType, interestId, page, perPage, new Callback<List<Post>>() {
            @Override
            public void success(List<Post> posts, Response response) {
                post(new LoadedFeedsEvent(posts, page));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onLoadFeedEvent(LoadFeedEvent loadFeedEvent) {
        // Get feed id
        int feedId = loadFeedEvent.getFeedId();

        mFeedController.getFeed(feedId, new Callback<Post>() {
            @Override
            public void success(Post post, Response response) {
                Log.d("FeedSubscriber", "Success!");
                post(new LoadedFeedEvent(post));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onGetFeedFromSearch(GetFeedFromSearch getFeedFromSearch) {

        int feedId = getFeedFromSearch.getFeedId();

        mFeedController.getFeed(feedId, new Callback<Post>() {
            @Override
            public void success(Post post, Response response) {
                post(new GotFeedFromSearch(post));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onCreateNewFeedEvent(CreateNewFeedEvent createNewFeedEvent) {

        String type = createNewFeedEvent.getType();
        int feedableId = createNewFeedEvent.getFeedableId();
        String content = createNewFeedEvent.getContent();
        String fileCache = createNewFeedEvent.getFileCache();
        String original = createNewFeedEvent.getOriginal();
        String thumb = createNewFeedEvent.getThumb();

        mFeedController.addNewFeed(type, feedableId, content, fileCache, original, thumb, new Callback<Post>() {
            @Override
            public void success(Post post, Response response) {
                post(new LoadedCreateNewFeedEvent(post));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onLoadPostCommentEvent(CreateNewCommentEvent createNewCommentEvent) {

        int feedableId = createNewCommentEvent.getFeedableId();
        String content = createNewCommentEvent.getContent();
        String fileCache = createNewCommentEvent.getFileCache();
        String original = createNewCommentEvent.getOriginal();
        String thumb = createNewCommentEvent.getThumb();

        mFeedController.postComment(feedableId, content, fileCache, original, thumb, new Callback<Post>() {
            @Override
            public void success(Post post, Response response) {
                post(new LoadedPostCommentEvent(post));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onUpdatePostEvent(UpdateCommentEvent updateCommentEvent) {

        int commentId = updateCommentEvent.getCommentId();
        String content = updateCommentEvent.getContent();
        String fileCache = updateCommentEvent.getFileCache();
        String original = updateCommentEvent.getOriginal();
        String thumb = updateCommentEvent.getThumb();
        boolean removeAttachment = updateCommentEvent.removeAttachment();

        mFeedController.updateComment(commentId, content, fileCache, original, thumb, removeAttachment,
                new Callback<Post>() {
                    @Override
                    public void success(Post post, Response response) {
                        post(new UpdatedCommentEvent(post));
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {

                    }
                });
    }

    @Subscribe
    public void onRemoveFeedEvent(RemoveFeedEvent removeFeedEvent) {

        int feedId = removeFeedEvent.getFeedId();

        mFeedController.deleteFeed(feedId, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                post(new RemovedFeedEvent());
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onRemovePostEvent(RemoveCommentEvent removeCommentEvent) {

        int commentId = removeCommentEvent.getCommentId();

        mFeedController.deleteComment(commentId, new Callback<Post>() {
            @Override
            public void success(Post post, Response response) {

            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

}
