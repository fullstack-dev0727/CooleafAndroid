package zkhaider.com.cooleaf.base;

import android.support.v4.app.Fragment;

import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.bus.BusProviderRegistry;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class BaseEventReportingFragment extends Fragment
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
