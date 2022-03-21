package zkhaider.com.cooleaf.base;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zkhaider.com.cooleaf.bus.BusProvider;

/**
 * Created by ZkHaider on 8/21/15.
 */
public abstract class BaseBusProviderRegistry {

    protected static BaseBusProviderRegistry INSTANCE;

    protected final BusProvider mEventBus = BusProvider.getInstance();
    protected final List<EventBusSubscriber> mDefaultEventSubscribers = new ArrayList<>();
    protected final HashMap<Object, EventBusSubscriber> mEventSubscribers = new HashMap<>();
    protected final Context mApplicationContext;

    public interface EventBusSubscriber {
        Object register(BusProvider eventBus);
        void unregister(BusProvider eventBus);
    }

    protected BaseBusProviderRegistry(Context applicationContext) {
        this.mApplicationContext = applicationContext;
        INSTANCE = this;
    }

    public static BaseBusProviderRegistry getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("No instance of EventBusRegistry found. " +
                    "Create new instance by subclassing and setting the INSTANCE.");
        }
        return INSTANCE;
    }

    public static void setInstance(BaseBusProviderRegistry instance) {
        INSTANCE = instance;
    }

    public void registerDefaultSubscribers() {
        onBeforeRegisterDefaultSubscribers();
        mDefaultEventSubscribers.clear();
        mDefaultEventSubscribers.addAll(createDefaultSubscribers());
        for (EventBusSubscriber subscriber : mDefaultEventSubscribers)
            registerSubscriber(subscriber);
    }

    public void unregisterAllSubscribers() {
        onBeforeUnregisterDefaultSubscribers();
        for (Object subscriber : mEventSubscribers.keySet())
            mEventBus.unregister(subscriber);
        mEventSubscribers.clear();
    }

    public void registerSubscriber(EventBusSubscriber subscriber) {
        if (mEventSubscribers.containsValue(subscriber))
            return;
        Object registeredSubscriber = subscriber.register(mEventBus);
        mEventSubscribers.put(registeredSubscriber, subscriber);
    }

    public void unregisterSubscriber(Object subscriber) {
        if (!mEventSubscribers.containsValue(subscriber))
            return;
        EventBusSubscriber visitor = mEventSubscribers.get(subscriber);
        visitor.unregister(mEventBus);
        mEventSubscribers.remove(subscriber);
    }

    protected abstract List<EventBusSubscriber> createDefaultSubscribers();
    protected void onBeforeRegisterDefaultSubscribers() {}
    protected void onBeforeUnregisterDefaultSubscribers() {}

}
