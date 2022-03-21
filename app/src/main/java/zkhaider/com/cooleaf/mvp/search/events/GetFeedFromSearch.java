package zkhaider.com.cooleaf.mvp.search.events;

/**
 * Created by ZkHaider on 10/9/15.
 */
public class GetFeedFromSearch {

    private int mFeedId;

    public GetFeedFromSearch(int feedId) {
        this.mFeedId = feedId;
    }

    public int getFeedId() {
        return mFeedId;
    }

}
