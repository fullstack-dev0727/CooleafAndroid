package zkhaider.com.cooleaf.cooleafapi.interfaces;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import zkhaider.com.cooleaf.cooleafapi.entities.EventsToJoinRequest;
import zkhaider.com.cooleaf.cooleafapi.entities.User;

/**
 * Created by ZkHaider on 5/24/15.
 */
public interface IParticipant {

    @GET("/v2/participations/{event_id}.json")
    void getParticipants(@Path("event_id") int eventId,
                         @Query("page") int page,
                         @Query("per_page") int perPage,
                         Callback<List<User>> callback);

    @POST("/v2/participations/{series_id}.json")
    void join(@Path("series_id") int seriesId, @Body EventsToJoinRequest body, Callback<Void> event);

}
