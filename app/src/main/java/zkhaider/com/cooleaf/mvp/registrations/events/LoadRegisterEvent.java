package zkhaider.com.cooleaf.mvp.registrations.events;

import zkhaider.com.cooleaf.cooleafapi.entities.RegistrationRequest;

/**
 * Created by Haider on 3/1/2015.
 */
public class LoadRegisterEvent {

    private RegistrationRequest mRegistrationRequest;

    public LoadRegisterEvent(RegistrationRequest registrationRequest) {
        this.mRegistrationRequest = registrationRequest;
    }

    public RegistrationRequest getRegistrationRequest() {
        return mRegistrationRequest;
    }

}
