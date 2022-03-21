package zkhaider.com.cooleaf.mvp.users.controllers;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.cooleafapi.interfaces.IUser;
import zkhaider.com.cooleaf.cooleafapi.entities.Edit;
import zkhaider.com.cooleaf.cooleafapi.entities.LocationTag;
import zkhaider.com.cooleaf.cooleafapi.entities.User;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class UserController {

    public void getMe(Callback<User> callback) {
        IUser me = CooleafAPIClient.getAsynsRestAdapter().create(IUser.class);
        me.getMe(callback);
    }

    public void getSpecificUser(int id, Callback<User> callback) {
        IUser iSpecificUser = CooleafAPIClient.getAsynsRestAdapter().create(IUser.class);
        iSpecificUser.getSpecificUser(id, callback);
    }

    public void getUsers(int page, Callback<List<User>> callback) {
        IUser user = CooleafAPIClient.getAsynsRestAdapter().create(IUser.class);
        user.getUsers(page, callback);
    }

    public void edit(Edit edit, Callback<User> callback) {
        IUser editProfile = CooleafAPIClient.getAsynsRestAdapter().create(IUser.class);
        editProfile.edit(edit, callback);
    }

    public void getLocationTags(Callback<ArrayList<LocationTag>> callback) {
        IUser user = CooleafAPIClient.getAsynsRestAdapter().create(IUser.class);
        user.getLocationTags(callback);
    }

    public void searchUsers(String query, String scope, int page,
                             int perPage, int offset, Callback<List<User>> callback) {
        IUser users = CooleafAPIClient.getAsynsRestAdapter().create(IUser.class);
        users.searchUsers(query, scope, page, perPage, offset, callback);
    }

}
