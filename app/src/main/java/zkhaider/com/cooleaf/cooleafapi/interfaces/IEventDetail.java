package zkhaider.com.cooleaf.cooleafapi.interfaces;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;

/**
 * Created by ZkHaider on 5/24/15.
 */
public interface IEventDetail {

    @GET("/v2/events/{id}.json")
    void getEvent(@Path("id") int id, Callback<Event> callback);

}
