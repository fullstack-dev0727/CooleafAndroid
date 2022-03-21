package zkhaider.com.cooleaf.mvp.registrations.events;

import zkhaider.com.cooleaf.cooleafapi.entities.Registration;

/**
 * Created by Haider on 3/1/2015.
 */
public class LoadedRegistrationCheckEvent {

    private Registration mRegistration;

    public LoadedRegistrationCheckEvent(Registration registration) {
        this.mRegistration = registration;
    }

    public Registration getRegistration() {
        return mRegistration;
    }

}
