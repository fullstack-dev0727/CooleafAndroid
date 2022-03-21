package zkhaider.com.cooleaf.mvp.comments.presenters;

import android.util.Log;

import com.squareup.otto.Subscribe;

import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.mvp.comments.events.LoadEventCommentsEvent;
import zkhaider.com.cooleaf.mvp.comments.events.PostEventCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.RemoveEventCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.RemovedEventCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.UpdateEventCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.UpdatedEventCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.interactors.ICommentInteractor;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadedCreateNewFeedEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadedFeedsEvent;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class CommentPresenter {

    private ICommentInteractor mInteractor;

    /*********************************************************************************************
     * Initialization
     *********************************************************************************************/

    public CommentPresenter(ICommentInteractor interactor) {
        this.mInteractor = interactor;
    }

    /*********************************************************************************************
     * BusProvider Methods
     *********************************************************************************************/

    public void registerOnBus() {
        BusProvider.getInstance().register(this);
    }

    public void unregisterOnBus() {
        BusProvider.getInstance().unregister(this);
    }

    /*********************************************************************************************
     * Load Comments Methods
     *********************************************************************************************/

    public void loadComments(String commentableType, int commentableId, int page, int perPage) {
        BusProvider.postEvent(new LoadEventCommentsEvent(commentableType, commentableId, page, perPage));
    }

    public void loadMoreComments(String commentableType, int commentableId, int page, int perPage) {

    }

    /*********************************************************************************************
     * Add Comment Methods
     *********************************************************************************************/

    public void addComment(String commentableType, int commentableId,  String content, String fileCache,
                              String original, String thumb) {
        BusProvider.postEvent(new PostEventCommentEvent(commentableType, commentableId, content,
                                fileCache, original, thumb));
    }

    /*********************************************************************************************
     * Update Comment Methods
     *********************************************************************************************/

    public void updateComment(String commentableType, int commentableId, int commentId, String content,
                              String fileCache, String original, String thumb, boolean removeAttachment) {
        BusProvider.postEvent(new UpdateEventCommentEvent(commentableType, commentableId, commentId, content,
                            fileCache, original, thumb, removeAttachment));
    }

    /*********************************************************************************************
     * Remove Comment Methods
     *********************************************************************************************/

    public void removeComment(String commentableType, int commentableId, int commentId) {
        BusProvider.postEvent(new RemoveEventCommentEvent(commentableType, commentableId, commentId));
    }

    /*********************************************************************************************
     * Subscription Methods
     *********************************************************************************************/

    @Subscribe
    public void onLoadedFeedsEvents(LoadedFeedsEvent loadedFeedsEvent) {
        if (mInteractor != null)
            mInteractor.initComments(loadedFeedsEvent.getPosts());
    }

    @Subscribe
    public void onLoadedCreateNewFeedEvent(LoadedCreateNewFeedEvent createNewFeedEvent) {
        if (mInteractor != null)
            mInteractor.addedNewComment(createNewFeedEvent.getPost());
    }

    @Subscribe
    public void onUpdatedEventCommentEvent(UpdatedEventCommentEvent updatedEventCommentEvent) {
        if (mInteractor != null)
            mInteractor.updatedComment(updatedEventCommentEvent.getPost());
    }

    @Subscribe
    public void onRemovedEventCommentEvent(RemovedEventCommentEvent removedEventCommentEvent) {
        if (mInteractor != null)
            mInteractor.deletedComment(removedEventCommentEvent.getComment());
    }

}
