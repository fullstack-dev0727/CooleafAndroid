package zkhaider.com.cooleaf.mvp.users.subscribers;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zkhaider.com.cooleaf.base.BaseSubscriber;
import zkhaider.com.cooleaf.mvp.users.events.LoadEditEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadLocationTagEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadMeEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadSpecificUserEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadUsersEvent;
import zkhaider.com.cooleaf.cooleafapi.events.LoadedErrorEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedLocationTagEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedMeEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedSpecificUserEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedUsersEvent;
import zkhaider.com.cooleaf.cooleafapi.entities.LocationTag;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.mvp.users.controllers.UserController;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class UserSubscriber extends BaseSubscriber {

    private UserController mUserController = new UserController();

    @Subscribe
    public void onLoadMeEvent(LoadMeEvent loadMeEvent) {
        mUserController.getMe(new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                post(new LoadedMeEvent(user));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onLoadSpecificUser(LoadSpecificUserEvent loadSpecificUserEvent) {
        mUserController.getSpecificUser(loadSpecificUserEvent.getId(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                post(new LoadedSpecificUserEvent(user));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onLoadUsersEvent(LoadUsersEvent loadUsersEvent) {
        mUserController.getUsers(loadUsersEvent.getPage(), new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                post(new LoadedUsersEvent(users));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void edit(LoadEditEvent loadEditEvent) {
        mUserController.edit(loadEditEvent.getEdit(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                post(new LoadedMeEvent(user));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onLoadLocationTags(LoadLocationTagEvent loadLocationTagEvent) {
        mUserController.getLocationTags(new Callback<ArrayList<LocationTag>>() {
            @Override
            public void success(ArrayList<LocationTag> locationTags, Response response) {
                post(new LoadedLocationTagEvent(locationTags));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

}
