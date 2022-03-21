package zkhaider.com.cooleaf.mvp.events.events;

import zkhaider.com.cooleaf.cooleafapi.entities.Event;

/**
 * Created by Haider on 2/11/2015.
 */
public class LoadedActivityEvent {

    private Event mEvent;

    public Event getEvent()
    {
        return this.mEvent;
    }

    public LoadedActivityEvent(Event event) {
        this.mEvent = event;
    }

}
