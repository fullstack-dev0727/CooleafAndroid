package zkhaider.com.cooleaf.mvp.users.events;

import zkhaider.com.cooleaf.cooleafapi.entities.User;

/**
 * Created by Haider on 2/18/2015.
 */
public class LoadedSpecificUserEvent {

    private User mUser;

    public User getUser()
    {
        return mUser;
    }

    public LoadedSpecificUserEvent(User user) {
        mUser = user;
    }

}
