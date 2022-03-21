package zkhaider.com.cooleaf.mvp.events.subscribers;

import com.squareup.otto.Subscribe;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zkhaider.com.cooleaf.base.BaseSubscriber;
import zkhaider.com.cooleaf.mvp.events.events.LoadEventSeries;
import zkhaider.com.cooleaf.mvp.events.events.LoadEventsEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadUsersEventEvent;
import zkhaider.com.cooleaf.cooleafapi.events.LoadedErrorEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadedEventsEvent;
import zkhaider.com.cooleaf.mvp.comments.events.LoadedSpecificEventSeries;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.mvp.events.controllers.EventController;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class EventSubscriber extends BaseSubscriber {

    private EventController mEventController = new EventController();

    @Subscribe
    public void onLoadEventsEvent(LoadEventsEvent loadEventsEvent) {
        mEventController.getEvents(loadEventsEvent.getPage(), new Callback<List<Event>>() {
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

    @Subscribe
    public void onLoadSeriesEvent(LoadEventSeries series) {
        final boolean isDashboard = series.isSpecificEvent();
        mEventController.getSeriesEvents(series.getId(), new Callback<List<Event>>() {
            @Override
            public void success(List<Event> events, Response response) {
                if (isDashboard)
                    post(new LoadedSpecificEventSeries(events));
                else
                    post(new LoadedEventsEvent(events));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }


    @Subscribe
    public void onLoadUsersEventEvent(LoadUsersEventEvent loadUsersEventEvent) {
        mEventController.getUserEvents(loadUsersEventEvent.getUserId(), loadUsersEventEvent.getScope(),
                new Callback<List<Event>>() {
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
