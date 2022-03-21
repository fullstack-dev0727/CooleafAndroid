package zkhaider.com.cooleaf.mvp.users.events;

import zkhaider.com.cooleaf.cooleafapi.entities.User;

/**
 * Created by Haider on 2/11/2015.
 */
public class LoadedMeEvent {

    private User mUser;

    public User getUser()
    {
        return mUser;
    }

    public LoadedMeEvent(User user) {
        mUser = user;
    }

}
