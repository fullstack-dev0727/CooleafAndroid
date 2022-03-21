package zkhaider.com.cooleaf.mvp.events.subscribers;

import com.squareup.otto.Subscribe;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zkhaider.com.cooleaf.base.BaseSubscriber;
import zkhaider.com.cooleaf.mvp.events.events.LoadActivityEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadedActivityEvent;
import zkhaider.com.cooleaf.cooleafapi.events.LoadedErrorEvent;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.mvp.events.controllers.EventDetailController;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class EventDetailSubscriber extends BaseSubscriber {

    private EventDetailController mEventDetailController = new EventDetailController();

    @Subscribe
    public void onLoadActivityEvent(LoadActivityEvent loadActivityEvent) {
        mEventDetailController.getEvent(loadActivityEvent.getId(), new Callback<Event>() {
            @Override
            public void success(Event event, Response response) {
                post(new LoadedActivityEvent(event));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

}
