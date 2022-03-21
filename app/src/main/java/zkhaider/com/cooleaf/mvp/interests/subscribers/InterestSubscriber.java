package zkhaider.com.cooleaf.mvp.interests.subscribers;

import com.squareup.otto.Subscribe;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zkhaider.com.cooleaf.base.BaseSubscriber;
import zkhaider.com.cooleaf.mvp.interests.events.LoadInterestEvent;
import zkhaider.com.cooleaf.mvp.interests.events.LoadInterestEventsEvent;
import zkhaider.com.cooleaf.mvp.interests.events.LoadInterestUsersEvent;
import zkhaider.com.cooleaf.mvp.interests.events.LoadInterestsEvent;
import zkhaider.com.cooleaf.cooleafapi.events.LoadedErrorEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadedEventsEvent;
import zkhaider.com.cooleaf.mvp.interests.events.LoadedInterestEvent;
import zkhaider.com.cooleaf.mvp.interests.events.LoadedInterestsEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedUsersEvent;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.mvp.interests.controllers.InterestController;

/**
 * Created by ZkHaider on 8/21/15.
 */
public class InterestSubscriber extends BaseSubscriber {

    private InterestController mInterestController = new InterestController();

    @Subscribe
    public void onLoadInterestsEvent(LoadInterestsEvent loadInterestsEvent) {
        mInterestController.getInterests(new Callback<List<Interest>>() {
            @Override
            public void success(List<Interest> interests, Response response) {
                post(new LoadedInterestsEvent(interests));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onLoadInterestsEvent(LoadInterestEvent loadInterestsEvent) {
        mInterestController.getInterest(loadInterestsEvent.getInterestId(), new Callback<Interest>() {
            @Override
            public void success(Interest interest, Response response) {
                post(new LoadedInterestEvent(interest));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onLoadInterestUsersEvent(LoadInterestUsersEvent loadInterestUsersEvent) {

        int interestId = loadInterestUsersEvent.getInterestId();
        int page = loadInterestUsersEvent.getPage();
        int perPage = loadInterestUsersEvent.getPerPage();

        mInterestController.getInterestsUsers(interestId, page, perPage, new Callback<List<User>>() {
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
    public void onLoadInterestEventsEvent(LoadInterestEventsEvent loadInterestEventsEvent) {

        int id = loadInterestEventsEvent.getInterestId();
        int page = loadInterestEventsEvent.getPage();
        int perPage = loadInterestEventsEvent.getPerPage();

        mInterestController.getInterestEvents(id, page, perPage, new Callback<List<Event>>() {
            @Override
            public void success(List<Event> events, Response response) {
                post(new LoadedEventsEvent(events));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }


}
