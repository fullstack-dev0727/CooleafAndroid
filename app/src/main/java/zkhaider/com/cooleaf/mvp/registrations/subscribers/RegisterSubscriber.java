package zkhaider.com.cooleaf.mvp.registrations.subscribers;

import com.squareup.otto.Subscribe;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zkhaider.com.cooleaf.base.BaseSubscriber;
import zkhaider.com.cooleaf.mvp.registrations.events.LoadRegisterEvent;
import zkhaider.com.cooleaf.mvp.registrations.events.LoadRegistrationCheckEvent;
import zkhaider.com.cooleaf.cooleafapi.events.LoadedErrorEvent;
import zkhaider.com.cooleaf.mvp.registrations.events.LoadedRegisterEvent;
import zkhaider.com.cooleaf.mvp.registrations.events.LoadedRegistrationCheckEvent;
import zkhaider.com.cooleaf.cooleafapi.entities.Registration;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.mvp.registrations.controllers.RegistrationController;

/**
 * Created by ZkHaider on 8/21/15.
 */
public class RegisterSubscriber extends BaseSubscriber {

    private RegistrationController mRegistrationController = new RegistrationController();

    @Subscribe
    public void onLoadRegistrationCheckEvent(LoadRegistrationCheckEvent loadRegistrationCheckEvent) {
        String mEmail = loadRegistrationCheckEvent.getEmail();
        mRegistrationController.getRegistrationCheck(mEmail, new Callback<Registration>() {
            @Override
            public void success(Registration registration, Response response) {
                post(new LoadedRegistrationCheckEvent(registration));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onLoadRegisterEvent(LoadRegisterEvent loadRegisterEvent) {
        mRegistrationController.register(loadRegisterEvent.getRegistrationRequest(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                post(new LoadedRegisterEvent(user));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

}
