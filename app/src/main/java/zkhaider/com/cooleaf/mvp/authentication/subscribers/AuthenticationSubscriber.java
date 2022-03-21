package zkhaider.com.cooleaf.mvp.authentication.subscribers;

import com.squareup.otto.Subscribe;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zkhaider.com.cooleaf.base.BaseSubscriber;
import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.cooleafapi.events.LoadedErrorEvent;
import zkhaider.com.cooleaf.cooleafapi.utils.APIConfig;
import zkhaider.com.cooleaf.gcm.GCMConfig;
import zkhaider.com.cooleaf.mvp.authentication.controllers.AuthenticationController;
import zkhaider.com.cooleaf.mvp.authentication.events.DeauthorizeEvent;
import zkhaider.com.cooleaf.mvp.authentication.events.LoadAuthenticateEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedMeEvent;
import zkhaider.com.cooleaf.utils.LocalPreferences;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class AuthenticationSubscriber extends BaseSubscriber {

    private static final String TAG = AuthenticationSubscriber.class.getSimpleName();

    private AuthenticationController mAuthenticationController = new AuthenticationController();

    @Subscribe
    public void onLoadAuthenticateEvent(LoadAuthenticateEvent loadAuthenticateEvent) {

        String email = loadAuthenticateEvent.getEmail();
        String password = loadAuthenticateEvent.getPassword();
        final String deviceID = LocalPreferences.getString("gcm_token");

        mAuthenticationController.authenticate(email, password, deviceID, APIConfig.getPlatform(),
                new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                // If the device id is not null then the token was sent to the server, set it as true
                if (deviceID != null)
                    LocalPreferences.set(GCMConfig.SEND_TOKEN_TO_SERVER, true);

                // Set the X-Organization header for requests
                CooleafAPIClient.setOrganization(user.getRole().getOrganization().getSubdomain());
                post(new LoadedMeEvent(user));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onDeauthorizeEvent(DeauthorizeEvent deauthorizeEvent) {

        String deviceId = LocalPreferences.getString("gcm_token");

        mAuthenticationController.deauthenticate(deviceId, APIConfig.getPlatform(), new Callback<Integer>() {
            @Override
            public void success(Integer integer, Response response) {
                LocalPreferences.set(GCMConfig.SEND_TOKEN_TO_SERVER, false);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }
}
