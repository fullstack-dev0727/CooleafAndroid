package zkhaider.com.cooleaf.mvp.events.controllers;

import java.util.List;

import retrofit.Callback;
import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.cooleafapi.interfaces.IEvents;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class EventController {

    public void getEvents(int page, Callback<List<Event>> callback) {
        IEvents events = CooleafAPIClient.getAsynsRestAdapter().create(IEvents.class);
        events.getEvents(page, callback);
    }

    public void getSeriesEvents(int eventId, Callback<List<Event>> callback) {
        IEvents events = CooleafAPIClient.getAsynsRestAdapter().create(IEvents.class);
        events.getSeriesEvents(eventId, callback);
    }

    public void getUserEvents(int userId, String scope, Callback<List<Event>> callback) {
        IEvents events = CooleafAPIClient.getAsynsRestAdapter().create(IEvents.class);
        events.getUserEvents(userId, scope, callback);
    }

    public void searchEvents(String query, String scope, int page,
                             int perPage, int offset, Callback<List<Event>> callback) {
        IEvents events = CooleafAPIClient.getAsynsRestAdapter().create(IEvents.class);
        events.searchEvents(query, scope, page, perPage, offset, callback);
    }

}
