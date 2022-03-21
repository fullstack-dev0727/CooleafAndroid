package zkhaider.com.cooleaf.mvp.feeds.presenters;

import com.squareup.otto.Subscribe;

import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.mvp.comments.events.CreateNewCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.LoadedPostCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.RemoveCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.UpdateCommentEvent;
import zkhaider.com.cooleaf.mvp.comments.events.UpdatedCommentEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadFeedEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadFeedsEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadedFeedEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadedFeedsEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.RemoveFeedEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.RemovedFeedEvent;
import zkhaider.com.cooleaf.mvp.feeds.interactors.IFeedInteractor;
import zkhaider.com.cooleaf.mvp.feeds.interactors.IFeedDetailInteractor;
import zkhaider.com.cooleaf.mvp.feeds.interactors.IFeedSearchInteractor;
import zkhaider.com.cooleaf.mvp.search.events.GetFeedFromSearch;
import zkhaider.com.cooleaf.mvp.search.events.GotFeedFromSearch;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class FeedPresenter {

    private IFeedInteractor mIFeedInteractor;
    private IFeedDetailInteractor mInteractor;
    private IFeedSearchInteractor mFeedSearchInteractor;
    private String mFeedableType;

    /*********************************************************************************************
     * Initialization
     *********************************************************************************************/

    public FeedPresenter(IFeedInteractor interactor) {
        this.mIFeedInteractor = interactor;
    }

    public FeedPresenter(IFeedDetailInteractor interactor) {
        this.mInteractor = interactor;
    }

    public FeedPresenter(IFeedSearchInteractor feedSearchInteractor) {
        this.mFeedSearchInteractor = feedSearchInteractor;
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
     * Feed Loading Methods
     *********************************************************************************************/

    public void loadFeeds(int interestId) {
        BusProvider.postEvent(new LoadFeedsEvent(mFeedableType, interestId));
    }

    public void loadMoreFeeds(int interestId, int page, int perPage) {
        BusProvider.postEvent(new LoadFeedsEvent(mFeedableType, interestId, page, perPage));
    }

    public void loadFeed(int feedId) {
        BusProvider.postEvent(new LoadFeedEvent(feedId));
    }

    public void loadFeedFromSearch(int feedId) {
        BusProvider.postEvent(new GetFeedFromSearch(feedId));
    }

    /*********************************************************************************************
     * Add Feed Methods
     *********************************************************************************************/

    public void addFeedComment(int postId, String content, String fileCache, String original, String thumb) {
        BusProvider.postEvent(new CreateNewCommentEvent(postId, content, fileCache, original, thumb));
    }

    /*********************************************************************************************
     * Update Feed Methods
     *********************************************************************************************/

    public void updateFeedComment(int postId, String content, String fileCache, String original, String thumb, boolean removeAttachment) {
        BusProvider.postEvent(new UpdateCommentEvent(postId, content, fileCache, original, thumb, removeAttachment));
    }

    /*********************************************************************************************
     * Remove Feed Methods
     *********************************************************************************************/

    public void removeFeed(int feedId) {
        BusProvider.postEvent(new RemoveFeedEvent(feedId));
    }

    public void removeFeedComment(int postId) {
        BusProvider.postEvent(new RemoveCommentEvent(postId));
    }

    /*********************************************************************************************
     * Subscription Events
     *********************************************************************************************/

    @Subscribe
    public void onLoadedFeedsEvent(LoadedFeedsEvent loadedFeedsEvent) {
        if (mIFeedInteractor != null)
            mIFeedInteractor.loadedFeeds(loadedFeedsEvent.getPosts());
    }

    @Subscribe
    public void onLoadedFeedEvent(LoadedFeedEvent loadedFeedEvent) {
        if (mInteractor != null)
            mInteractor.initFeed(loadedFeedEvent.getFeed());
    }

    @Subscribe
    public void onGotFeedFromSearch(GotFeedFromSearch gotFeedFromSearch) {
        if (mFeedSearchInteractor != null)
            mFeedSearchInteractor.gotFeedFromSearch(gotFeedFromSearch.getPost());
    }

    @Subscribe
    public void onCreatedNewCommentEvent(LoadedPostCommentEvent loadedPostCommentEvent) {
        if (mInteractor != null)
            mInteractor.addedComment(loadedPostCommentEvent.getPost());
    }

    @Subscribe
    public void onUpdatedCommentEvent(UpdatedCommentEvent updatedCommentEvent) {
        if (mInteractor != null)
            mInteractor.updatedComment(updatedCommentEvent.getUpdatedPost());
    }

    @Subscribe
    public void onRemovedFeedEvent(RemovedFeedEvent removedFeedEvent) {
        if (mIFeedInteractor != null)
            mIFeedInteractor.onDeletedFeed();
    }

    /*********************************************************************************************
     * Accessory Methods
     *********************************************************************************************/

    public void setFeedableType(String type) {
        this.mFeedableType = type;
    }

}
