package zkhaider.com.cooleaf.mvp.participants.events;

import zkhaider.com.cooleaf.cooleafapi.entities.EventsToJoinRequest;

/**
 * Created by kcoleman on 2/17/15.
 */
public class LoadJoinActivityEvent {

    private EventsToJoinRequest mEventsToJoinRequest;
    private int mSeriesId;
    public LoadJoinActivityEvent(int seriesId, EventsToJoinRequest eventsToJoinRequest)
    {
        mSeriesId = seriesId;
        mEventsToJoinRequest = eventsToJoinRequest;
    }
    public EventsToJoinRequest getEventsToJoinRequest()
    {
        return mEventsToJoinRequest;
    }
    public int getSeriesId()
    {
        return mSeriesId;
    }

}
