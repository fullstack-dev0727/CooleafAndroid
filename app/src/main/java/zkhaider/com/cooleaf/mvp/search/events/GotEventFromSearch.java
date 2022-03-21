package zkhaider.com.cooleaf.mvp.search.events;

import zkhaider.com.cooleaf.cooleafapi.entities.Event;

/**
 * Created by ZkHaider on 10/9/15.
 */
public class GotEventFromSearch {

    private Event mEvent;

    public GotEventFromSearch(Event event) {
        this.mEvent = event;
    }

    public Event getEvent() {
        return mEvent;
    }

}
