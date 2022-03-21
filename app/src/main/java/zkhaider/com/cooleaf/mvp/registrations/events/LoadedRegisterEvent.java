package zkhaider.com.cooleaf.mvp.registrations.events;

import zkhaider.com.cooleaf.cooleafapi.entities.User;

/**
 * Created by Haider on 3/1/2015.
 */
public class LoadedRegisterEvent {

    private User mUser;

    public LoadedRegisterEvent(User user) {
        this.mUser = user;
    }

    public User getUser() {
        return mUser;
    }

}
