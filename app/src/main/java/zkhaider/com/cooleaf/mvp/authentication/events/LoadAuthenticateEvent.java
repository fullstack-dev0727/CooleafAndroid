package zkhaider.com.cooleaf.mvp.authentication.events;

/**
 * Created by Haider on 3/1/2015.
 */
public class LoadAuthenticateEvent {

    private String mEmail;
    private String mPassword;

    public LoadAuthenticateEvent(String email, String password) {
        this.mEmail = email;
        this.mPassword = password;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

}
