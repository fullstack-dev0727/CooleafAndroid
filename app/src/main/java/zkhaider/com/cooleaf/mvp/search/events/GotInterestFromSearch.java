package zkhaider.com.cooleaf.mvp.search.events;

import zkhaider.com.cooleaf.cooleafapi.entities.Interest;

/**
 * Created by ZkHaider on 10/9/15.
 */
public class GotInterestFromSearch {

    private Interest mInterest;

    public GotInterestFromSearch(Interest interest) {
        this.mInterest = interest;
    }

    public Interest getInterest() {
        return mInterest;
    }

}
