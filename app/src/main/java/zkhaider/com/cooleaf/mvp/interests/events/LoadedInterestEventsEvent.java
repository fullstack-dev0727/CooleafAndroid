package zkhaider.com.cooleaf.mvp.interests.events;

import java.util.List;

import zkhaider.com.cooleaf.cooleafapi.entities.Event;

/**
 * Created by kcoleman on 3/10/15.
 */
public class LoadedInterestEventsEvent {

    private List<Event> mEvents;

    public List<Event>  getEvents()
    {
        return mEvents;
    }

    public LoadedInterestEventsEvent(List<Event> events)
    {
        mEvents = events;
    }

}
