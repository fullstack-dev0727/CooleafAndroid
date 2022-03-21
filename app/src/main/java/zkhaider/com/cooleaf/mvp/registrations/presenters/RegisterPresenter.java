package zkhaider.com.cooleaf.mvp.registrations.presenters;

import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.mvp.registrations.events.LoadRegisterEvent;
import zkhaider.com.cooleaf.mvp.registrations.events.LoadRegistrationCheckEvent;
import zkhaider.com.cooleaf.cooleafapi.entities.Registration;
import zkhaider.com.cooleaf.cooleafapi.entities.RegistrationRequest;

/**
 * Created by ZkHaider on 8/21/15.
 */
public class RegisterPresenter {

    public void performRegisterationCheck(String email) {
        BusProvider.getInstance().post(new LoadRegistrationCheckEvent(email));
    }

    public void performRegister(Registration registration, RegistrationRequest registrationRequest,
                                String userName, String password) {
        registrationRequest.setGender(registration.getGender());
        registrationRequest.setToken(registration.getToken());
        registrationRequest.setName(userName);
        registrationRequest.setPassword(password);
        BusProvider.getInstance().post(new LoadRegisterEvent(registrationRequest));
    }

}
