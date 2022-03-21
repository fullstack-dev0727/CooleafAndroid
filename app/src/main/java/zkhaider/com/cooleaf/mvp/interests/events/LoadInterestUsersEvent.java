package zkhaider.com.cooleaf.mvp.interests.events;

/**
 * Created by kcoleman on 3/6/15.
 * Edited by ZkHaider on 7-6-15
 */
public class LoadInterestUsersEvent {

    private int mInterestId;
    private int mPage;
    private int mPerPage;

    public LoadInterestUsersEvent(int interestId, int page) {
        this.mInterestId = interestId;
        this.mPage = page;
        this.mPerPage = 25;
    }

    public int getInterestId()
    {
        return mInterestId;
    }

    public int getPage() {
        return mPage;
    }

    public int getPerPage() {
        return mPerPage;
    }

}
