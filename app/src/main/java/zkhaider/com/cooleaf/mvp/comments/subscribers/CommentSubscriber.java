package zkhaider.com.cooleaf.mvp.comments.subscribers;

import com.squareup.otto.Subscribe;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zkhaider.com.cooleaf.base.BaseSubscriber;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.cooleafapi.events.LoadedErrorEvent;
import zkhaider.com.cooleaf.mvp.comments.controllers.CommentController;
import zkhaider.com.cooleaf.mvp.comments.events.LoadEventCommentsEvent;
import zkhaider.com.cooleaf.mvp.comments.events.PostEventCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.RemoveEventCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.RemovedEventCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.UpdateEventCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.UpdatedEventCommentEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadedCreateNewFeedEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadedFeedsEvent;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class CommentSubscriber extends BaseSubscriber {

    private CommentController mCommentController = new CommentController();

    @Subscribe
    public void onLoadEventCommentsEvent(LoadEventCommentsEvent loadEventCommentsEvent) {

        String commentableType = loadEventCommentsEvent.getCommentableType();
        int id = loadEventCommentsEvent.getEventId();
        final int page = loadEventCommentsEvent.getPage();
        int perPage = loadEventCommentsEvent.getPerPage();

        mCommentController.getComments(commentableType, id, page, perPage, new Callback<List<Post>>() {
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
        public void onPostEventCommentEvent(PostEventCommentEvent postEventCommentEvent) {

            String commentableType = postEventCommentEvent.getCommentableType();
            int id = postEventCommentEvent.getEventId();
            String content = postEventCommentEvent.getContent();
            String fileCache = postEventCommentEvent.getFileCache();
            String original = postEventCommentEvent.getOriginal();
            String thumb = postEventCommentEvent.getThumb();

        mCommentController.postComment(commentableType, id, content, fileCache, original,
                thumb, new Callback<Post>() {
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
    public void onUpdateEventCommentEvent(UpdateEventCommentEvent updateEventCommentEvent) {

        String commentableType = updateEventCommentEvent.getCommentableType();
        int commentableId = updateEventCommentEvent.getCommentableId();
        int commentId = updateEventCommentEvent.getCommentId();
        String content = updateEventCommentEvent.getContent();
        String fileCache = updateEventCommentEvent.getFileCache();
        String original = updateEventCommentEvent.getOriginal();
        String thumb = updateEventCommentEvent.getThumb();
        boolean removeAttachment = updateEventCommentEvent.removeAttachment();

        mCommentController.updateComment(commentableType, commentableId, commentId,
                content, fileCache, original, thumb, removeAttachment, new Callback<Post>() {
                    @Override
                    public void success(Post post, Response response) {
                        post(new UpdatedEventCommentEvent(post));
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        post(new LoadedErrorEvent(retrofitError));
                    }
                });
    }

    @Subscribe
    public void onRemoveEventCommentEvent(RemoveEventCommentEvent removeEventCommentEvent) {

        String commentableType = removeEventCommentEvent.getCommentableType();
        int commentableId = removeEventCommentEvent.getCommentableId();
        int commentId = removeEventCommentEvent.getCommentId();

        mCommentController.deleteComment(commentableType, commentableId, commentId,
                new Callback<Post>() {
                    @Override
                    public void success(Post post, Response response) {
                        post(new RemovedEventCommentEvent(post));
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        post(new LoadedErrorEvent(retrofitError));
                    }
                });
    }

}
