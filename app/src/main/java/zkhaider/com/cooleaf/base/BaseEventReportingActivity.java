package zkhaider.com.cooleaf.base;

import android.support.v7.app.AppCompatActivity;

import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.bus.BusProviderRegistry;

/**
 * Created by ZkHaider on 8/21/15.
 */
public class BaseEventReportingActivity extends AppCompatActivity
                                                implements BusProviderRegistry.EventBusSubscriber {

    @Override
    public Object register(BusProvider eventBus) {
        eventBus.register(this);
        return this;
    }

    @Override
    public void unregister(BusProvider eventBus) {
        eventBus.unregister(this);
    }

}
