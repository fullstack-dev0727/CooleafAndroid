package zkhaider.com.cooleaf.mvp.registrations.controllers;

import retrofit.Callback;
import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.cooleafapi.interfaces.IRegistration;
import zkhaider.com.cooleaf.cooleafapi.entities.Registration;
import zkhaider.com.cooleaf.cooleafapi.entities.RegistrationRequest;
import zkhaider.com.cooleaf.cooleafapi.entities.User;

/**
 * Created by ZkHaider on 8/21/15.
 */
public class RegistrationController {

    public void getRegistrationCheck(String email, Callback<Registration> callback) {
        IRegistration registration = CooleafAPIClient.getAsynsRestAdapter().create(IRegistration.class);
        registration.checkRegistration(email, callback);
    }

    public void register(RegistrationRequest registrationRequest, Callback<User> callback) {
        IRegistration register = CooleafAPIClient.getAsynsRestAdapter().create(IRegistration.class);
        register.register(registrationRequest, callback);
    }

}
