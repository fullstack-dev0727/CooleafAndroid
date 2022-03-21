package zkhaider.com.cooleaf.cooleafapi.interfaces;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import zkhaider.com.cooleaf.cooleafapi.entities.Edit;
import zkhaider.com.cooleaf.cooleafapi.entities.LocationTag;
import zkhaider.com.cooleaf.cooleafapi.entities.User;

/**
 * Created by ZkHaider on 5/24/15.
 */
public interface IUser {

    @GET("/v1/users/me.json")
    void getMe(Callback<User> callback);

    @GET("/v1/users/{id}.json")
    void getSpecificUser(@Path("id")        int id,
                                            Callback<User> callback);

    @GET("/v1/users.json")
    void getUsers(@Query("page")            int page,
                                            Callback<List<User>> callback);

    @PUT("/v2/users/edit.json")
    void edit(@Body                         Edit edit,
                                            Callback<User> callback);

    @GET("/v2/users/location_tags.json")
    void getLocationTags(Callback<ArrayList<LocationTag>> callback);

    @GET("/v2/events/globalSearch.json")
    void searchUsers(@Query("query")        String query,
                      @Query("scope")       String scope,
                      @Query("page")        int page,
                      @Query("per_page")    int perPage,
                      @Query("offset")      int offset,
                      Callback<List<User>>  callback);

}
