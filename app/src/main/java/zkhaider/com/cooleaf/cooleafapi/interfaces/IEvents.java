package zkhaider.com.cooleaf.cooleafapi.interfaces;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;

/**
 * Created by ZkHaider on 5/24/15.
 */
public interface IEvents {

    @GET("/v2/events.json")
    void getEvents(@Query("page")           int page,
                                            Callback<List<Event>> callback);

    @GET("/v2/events/{event_id}/series.json")
    void getSeriesEvents(@Path("event_id")  int eventId,
                                            Callback<List<Event>> callback);

    @GET("/v2/events/user/{user_id}.json")
    void getUserEvents(@Path("user_id")     int userId,
                       @Query("scope")      String scope,
                                            Callback<List<Event>> callback);

    @GET("/v2/events/globalSearch.json")
    void searchEvents(@Query("query")       String query,
                      @Query("scope")       String scope,
                      @Query("page")        int page,
                      @Query("per_page")    int perPage,
                      @Query("offset")      int offset,
                                            Callback<List<Event>> callback);

}
