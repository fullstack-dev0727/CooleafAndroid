package zkhaider.com.cooleaf.mvp.events.presenters;

import com.squareup.otto.Subscribe;

import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.mvp.events.events.LoadActivityEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadedActivityEvent;
import zkhaider.com.cooleaf.mvp.events.interactors.IEventInteractor;
import zkhaider.com.cooleaf.mvp.events.interactors.IEventSearchInteractor;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class EventPresenter {

    private IEventInteractor mEventInteractor;
    private IEventSearchInteractor mEventSearchInteractor;

    /*********************************************************************************************
     * Initialization
     *********************************************************************************************/

    public EventPresenter(IEventInteractor eventInteractor) {
        this.mEventInteractor = eventInteractor;
    }

    public EventPresenter(IEventSearchInteractor searchInteractor) {
        this.mEventSearchInteractor = searchInteractor;
    }

    /*********************************************************************************************
     * BusProvider Methods
     *********************************************************************************************/

    public void registerOnBus() {
        BusProvider.getInstance().register(this);
    }

    public void unregisterOnBus() {
        BusProvider.getInstance().unregister(this);
    }

    /*********************************************************************************************
     * getEvent
     *********************************************************************************************/

    public void getEvent(int eventId) {
        BusProvider.postEvent(new LoadActivityEvent(eventId));
    }

    /*********************************************************************************************
     * Subscription Events
     *********************************************************************************************/

    @Subscribe
    public void onLoadedActivityEvent(LoadedActivityEvent loadedActivityEvent) {
        if (mEventSearchInteractor != null)
            mEventSearchInteractor.gotEventFromSearch(loadedActivityEvent.getEvent());
    }

}
