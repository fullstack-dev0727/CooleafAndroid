package zkhaider.com.cooleaf.mvp.registrations.events;

/**
 * Created by Haider on 3/1/2015.
 */
public class LoadRegistrationCheckEvent {

    private String mEmail;

    public LoadRegistrationCheckEvent(String email) {
        this.mEmail = email;
    }

    public String getEmail() {
        return mEmail;
    }

}
