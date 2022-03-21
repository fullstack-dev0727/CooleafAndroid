package zkhaider.com.cooleaf.mvp.events.events;

/**
 * Created by kcoleman on 2/17/15.
 */
public class LoadEventSeries {

    private int mEventId;
    private boolean mIsSpecificEvent = false;

    public LoadEventSeries(int eventId) {
        mEventId = eventId;
    }

    public LoadEventSeries(int eventId, boolean isSpecificEvent) {
        this.mEventId = eventId;
        this.mIsSpecificEvent = isSpecificEvent;
    }

    public int getId() {
        return mEventId;
    }

    public void setSpecificEvent(boolean isDashboard) {
        this.mIsSpecificEvent = isDashboard;
    }

    public boolean isSpecificEvent() {
        return mIsSpecificEvent;
    }

}
