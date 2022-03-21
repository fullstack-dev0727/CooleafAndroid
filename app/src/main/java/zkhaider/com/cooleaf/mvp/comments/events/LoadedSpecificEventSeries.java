package zkhaider.com.cooleaf.mvp.comments.events;

import java.util.List;

import zkhaider.com.cooleaf.cooleafapi.entities.Event;

/**
 * Created by ZkHaider on 8/12/15.
 */
public class LoadedSpecificEventSeries {

    private List<Event> mEvents;

    public LoadedSpecificEventSeries(List<Event> events) {
        mEvents = events;
    }

    public List<Event> getEvents() {
        return mEvents;
    }

}
