package zkhaider.com.cooleaf.mvp.feeds.events;

/**
 * Created by ZkHaider on 10/6/15.
 */
public class LoadFeedEvent {

    private int mFeedId;

    public LoadFeedEvent(int feedId) {
        this.mFeedId = feedId;
    }

    public int getFeedId() {
        return mFeedId;
    }

}
