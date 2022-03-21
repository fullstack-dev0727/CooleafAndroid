package zkhaider.com.cooleaf.mvp.interests.events;

import zkhaider.com.cooleaf.cooleafapi.entities.Interest;

/**
 * Created by kcoleman on 3/6/15.
 */
public class LoadedInterestEvent {
    private Interest mInterest;
    public Interest getInterest()
    { return mInterest; }

    public LoadedInterestEvent(Interest interest)
    {
        mInterest = interest;
    }
}
