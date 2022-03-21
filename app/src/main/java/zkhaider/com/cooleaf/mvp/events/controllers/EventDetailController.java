package zkhaider.com.cooleaf.mvp.events.controllers;

import retrofit.Callback;
import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.cooleafapi.interfaces.IEventDetail;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class EventDetailController {

    public void getEvent(int id, Callback<Event> callback) {
        IEventDetail eventInterface = CooleafAPIClient.getAsynsRestAdapter().create(IEventDetail.class);
        eventInterface.getEvent(id, callback);
    }

}
