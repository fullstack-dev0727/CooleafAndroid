package zkhaider.com.cooleaf.mvp.events.events;

import java.util.List;

import zkhaider.com.cooleaf.cooleafapi.entities.Event;

/**
 * Created by kcoleman on 2/17/15.
 */
public class LoadedEventsEvent {

    private List<Event> mEvents;

    public LoadedEventsEvent(List<Event> events) {
        mEvents = events;
    }

    public List<Event> getEvents() {
        return mEvents;
    }

}
