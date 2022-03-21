package zkhaider.com.cooleaf.mvp.search.events;

/**
 * Created by ZkHaider on 10/9/15.
 */
public class GetInterstFromSearch {

    private int mInterestId;

    public GetInterstFromSearch(int interestId) {
        this.mInterestId = interestId;
    }

    public int getInterestId() {
        return mInterestId;
    }

}
