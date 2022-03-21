package zkhaider.com.cooleaf.cooleafapi.interfaces;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.User;

/**
 * Created by ZkHaider on 5/24/15.
 */
public interface IInterest {

    @GET("/v2/interests.json")
    void getInterests(Callback<List<Interest>>  callback);

    @GET("/v2/interests/{interest_id}.json")
    void getInterest(@Path("interest_id")       int interestId,
                                                Callback<Interest> callback);

    @GET("/v2/interests/{interest_id}/users.json")
    void getInterestUsers(@Path("interest_id")  int eventId,
                         @Query("page")         int page,
                         @Query("per_page")     int perPage,
                         Callback<List<User>> callback);

    @GET("/v2/interests/{interest_id}/events.json")
    void getInterestEvents(@Path("interest_id") int interestId,
                           @Query("page")       int page,
                           @Query("per_page")   int perPage,
                           Callback<List<Event>> callback);

    @GET("/v2/interests/globalSearch.json")
    void searchInterests(@Query("query")        String query,
                                                Callback<List<Interest>> callback);

}
