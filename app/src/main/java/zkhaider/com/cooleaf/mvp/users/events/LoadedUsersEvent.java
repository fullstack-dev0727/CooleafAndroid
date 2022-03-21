package zkhaider.com.cooleaf.mvp.users.events;

import java.util.List;

import zkhaider.com.cooleaf.cooleafapi.entities.User;

/**
 * Created by kcoleman on 2/17/15.
 */
public class LoadedUsersEvent {

    private List<User> mUsers;

    public List<User> getUsers()
    {
        return mUsers;
    }

    public LoadedUsersEvent(List<User>  users) {
        this.mUsers = users;
    }
}
