package zkhaider.com.cooleaf.mvp.search.events;

/**
 * Created by ZkHaider on 10/9/15.
 */
public class GetEventFromSearch {

    private int mEventId;

    public GetEventFromSearch(int eventId) {
        this.mEventId = eventId;
    }

    public int getEventId() {
        return mEventId;
    }

}
