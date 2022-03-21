package zkhaider.com.cooleaf.mvp.interests.events;

/**
 * Created by kcoleman on 3/6/15.
 */
public class LoadInterestEvent {

    private int mInterestId;
    public int getInterestId()
    { return mInterestId; }

    public LoadInterestEvent(int interestId)
    {
        mInterestId = interestId;
    }
}
