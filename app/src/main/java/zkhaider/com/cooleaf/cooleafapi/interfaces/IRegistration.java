package zkhaider.com.cooleaf.cooleafapi.interfaces;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Query;
import zkhaider.com.cooleaf.cooleafapi.entities.Registration;
import zkhaider.com.cooleaf.cooleafapi.entities.RegistrationRequest;
import zkhaider.com.cooleaf.cooleafapi.entities.User;

/**
 * Created by ZkHaider on 5/24/15.
 */
public interface IRegistration {

    @POST("/v2/registrations/check.json")
    void checkRegistration(@Query("email") String email, Callback<Registration> callback);

    @POST("/v2/registrations.json")
    void register(@Body RegistrationRequest registration, Callback<User> callback);

}
