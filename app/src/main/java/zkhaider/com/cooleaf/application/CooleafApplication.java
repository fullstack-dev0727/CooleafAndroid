package zkhaider.com.cooleaf.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import zkhaider.com.cooleaf.bus.BusProviderRegistry;
import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.utils.LocalPreferences;

/**
 * Created by Haider on 12/23/2014.
 */
public class CooleafApplication extends Application {

    private static final String TAG = CooleafApplication.class.getSimpleName();

    private static CooleafApplication sInstance;
    private CooleafAPIClient mClient;
    private BusProviderRegistry mBusProviderRegistry;

    public static CooleafApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        sInstance = this;
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        setupNetworkClient();
        startEventProcessing();
        // Initialize LocalPreferences
        LocalPreferences.init(this);
    }

    public void setupNetworkClient() {
        mClient = new CooleafAPIClient(this);
    }

    public void startEventProcessing() {
        mBusProviderRegistry = new BusProviderRegistry(this);
        mBusProviderRegistry.registerDefaultSubscribers();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        sInstance = null;
        mBusProviderRegistry.unregisterAllSubscribers();
        mBusProviderRegistry = null;
    }

}