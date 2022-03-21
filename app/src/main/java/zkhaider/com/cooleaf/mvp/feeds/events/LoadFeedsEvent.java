package zkhaider.com.cooleaf.mvp.feeds.events;

/**
 * Created by ZkHaider on 7/7/15.
 */
public class LoadFeedsEvent {

    private String mFeedType;
    private int mInterestId;
    private int mPage = 1;
    private int mPerPage = 25;

    public LoadFeedsEvent(String feedType, int interestId) {
        this.mFeedType = feedType;
        this.mInterestId = interestId;
    }

    public LoadFeedsEvent(String feedType, int interestId, int page, int perPage) {
        this.mFeedType = feedType;
        this.mInterestId = interestId;
        this.mPage = page;
        this.mPerPage = perPage;
    }

    public String getFeedType() {
        return mFeedType;
    }

    public int getInterestId() {
        return mInterestId;
    }

    public int getPage() {
        return mPage;
    }

    public int getPerPage() {
        return mPerPage;
    }

}
