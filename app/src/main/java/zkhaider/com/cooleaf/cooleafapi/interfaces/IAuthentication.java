package zkhaider.com.cooleaf.cooleafapi.interfaces;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import zkhaider.com.cooleaf.cooleafapi.entities.User;

/**
 * Created by ZkHaider on 5/24/15.
 */
public interface IAuthentication {

    @POST("/v2/authorize.json")
    void authenticate(@Query("email")       String email,
                      @Query("password")    String password,
                      @Query("device_id")   String deviceId,
                                            Callback<User> callback);

    @POST("/v2/authorize.json")
    void authenticate(@Query("email")       String email,
                      @Query("password")    String password,
                      @Query("device_id")   String deviceId,
                      @Query("platform")    String platform,
                                            Callback<User> callback);

    @POST("/v2/deauthorize.json")
    void deauthenticate();

    @Multipart
    @POST("/v2/deauthorize.json")
    void deauthenticate(@Part("device_id") String deviceId,
                        @Part("platform")  String platform,
                                            Callback<Integer> callback);

}
