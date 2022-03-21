package zkhaider.com.cooleaf.mvp.interests.events;

/**
 * Created by kcoleman on 3/10/15.
 */
public class LoadInterestEventsEvent {

    private int mInterestId;
    private int mPage;
    private int mPerPage;

//    public LoadInterestEventsEvent(int interestId) {
//        mInterestId = interestId;
//    }

    public LoadInterestEventsEvent(int interestId, int page, int perPage) {
        this.mInterestId = interestId;
        this.mPage = page;
        this.mPerPage = perPage;
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
