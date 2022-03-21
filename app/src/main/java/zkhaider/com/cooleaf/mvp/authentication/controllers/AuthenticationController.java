package zkhaider.com.cooleaf.mvp.authentication.controllers;

import retrofit.Callback;
import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.cooleafapi.interfaces.IAuthentication;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.cooleafapi.utils.CooleafSessionManager;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class AuthenticationController {

    public void authenticate(String email, String password, String deviceID, Callback<User> callback) {
        IAuthentication authentication = CooleafAPIClient.getAsynsRestAdapter().create(IAuthentication.class);
        authentication.authenticate(email, password, deviceID, callback);
    }

    public void authenticate(String email, String password, String deviceId, String platform, Callback<User> callback) {
        IAuthentication authentication = CooleafAPIClient.getAsynsRestAdapter().create(IAuthentication.class);
        authentication.authenticate(email, password, deviceId, platform, callback);
    }

    public void deauthenticate() {
        IAuthentication authentication = CooleafAPIClient.getAsynsRestAdapter().create(IAuthentication.class);
        CooleafAPIClient.setOrganization(null);
        CooleafSessionManager.clearSession(CooleafAPIClient.getContext());
        authentication.deauthenticate();
    }

    public void deauthenticate(String deviceId, String platform, Callback<Integer> callback) {
        IAuthentication authentication = CooleafAPIClient.getAsynsRestAdapter().create(IAuthentication.class);
        CooleafAPIClient.setOrganization(null);
        CooleafSessionManager.clearSession(CooleafAPIClient.getContext());
        authentication.deauthenticate(deviceId, platform, callback);
    }
}
