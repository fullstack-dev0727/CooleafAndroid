package zkhaider.com.cooleaf.mvp.feeds.events;

/**
 * Created by ZkHaider on 10/6/15.
 */
public class RemoveFeedEvent {

    private int mFeedId;

    public RemoveFeedEvent(int feedId) {
        this.mFeedId = feedId;
    }

    public int getFeedId() {
        return mFeedId;
    }

}
