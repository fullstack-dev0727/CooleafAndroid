package zkhaider.com.cooleaf.base;

import zkhaider.com.cooleaf.bus.BusProvider;

/**
 * Created by ZkHaider on 8/21/15.
 */
public abstract class BaseSubscriber implements BaseBusProviderRegistry.EventBusSubscriber {

    private BusProvider mEventBus;

    @Override
    public final Object register(BusProvider eventBus) {
        mEventBus = eventBus;
        mEventBus.register(this);
        return this;
    }

    public final void unregister(BusProvider eventBus) {
        eventBus.unregister(this);
        mEventBus = null;
    }

    protected void post(Object event) {
        if (mEventBus == null) {
            throw new NullPointerException("Subscriber.register() was not called. " +
                    "Is the subscriber registered in the BusProviderRegistry?");
        }
        mEventBus.post(event);
    }

}
