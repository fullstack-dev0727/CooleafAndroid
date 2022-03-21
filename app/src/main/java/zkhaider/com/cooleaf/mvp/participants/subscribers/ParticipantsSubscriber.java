package zkhaider.com.cooleaf.mvp.participants.subscribers;

import com.squareup.otto.Subscribe;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zkhaider.com.cooleaf.base.BaseSubscriber;
import zkhaider.com.cooleaf.mvp.participants.events.LoadJoinActivityEvent;
import zkhaider.com.cooleaf.mvp.participants.events.LoadParticipantsEvent;
import zkhaider.com.cooleaf.cooleafapi.events.LoadedErrorEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadedJoinActivityEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedUsersEvent;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.mvp.participants.controllers.ParticipantsController;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class ParticipantsSubscriber extends BaseSubscriber {

    private ParticipantsController mParticipantsController = new ParticipantsController();

    @Subscribe
    public void onLoadParticipants(LoadParticipantsEvent loadParticipantsEvent) {

        int id = loadParticipantsEvent.getId();
        int page = loadParticipantsEvent.getPage();
        int perPage = loadParticipantsEvent.getPerPage();

        mParticipantsController.getParticipants(id, page, perPage, new Callback<List<User>>() {
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
    public void onLoadJoinActivityEvent(LoadJoinActivityEvent joinActivityEvent) {
        mParticipantsController.joinEvents(joinActivityEvent.getSeriesId(),
                joinActivityEvent.getEventsToJoinRequest(), new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        post(new LoadedJoinActivityEvent());
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        post(new LoadedErrorEvent(retrofitError));
                    }
                });
    }

}
