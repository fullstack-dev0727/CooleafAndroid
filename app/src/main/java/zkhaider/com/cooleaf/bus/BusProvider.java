package zkhaider.com.cooleaf.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Haider on 2/11/2015.
 */
public final class BusProvider extends Bus {

    private static volatile BusProvider mDefaultInstance;
    private final ScheduledExecutorService mExecutorService;

    private BusProvider() {
        super(ThreadEnforcer.ANY);
        mExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    public static BusProvider getInstance() {
        if (mDefaultInstance == null) {
            synchronized (BusProvider.class) {
                if (mDefaultInstance == null)
                    mDefaultInstance = new BusProvider();
            }
        }
        return mDefaultInstance;
    }

    public ScheduledFuture<Object> postDelayed(Object event, long delay) {
        return mExecutorService.schedule(new PostEventCallable(this, event), delay, TimeUnit.MILLISECONDS);
    }

    public static void postEvent(Object event) {
        getInstance().post(event);
    }

    private class PostEventCallable implements Callable<Object> {

        private final BusProvider mEventBus;
        private final Object mEvent;

        public PostEventCallable(BusProvider eventBus, Object event) {
            this.mEventBus = eventBus;
            this.mEvent = event;
        }

        @Override
        public Object call() throws Exception {
            mEventBus.post(mEvent);
            return null;
        }

    }

}
