package zkhaider.com.cooleaf.base;

import android.content.Context;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by ZkHaider on 8/21/15.
 */
public abstract class BaseAPIClient {

    protected static BaseAPIClient INSTANCE;

    protected final Context mApplicationContext;

    protected BaseAPIClient(Context applicationContext) {
        this.mApplicationContext = applicationContext;
        INSTANCE = this;
    }

    public static BaseAPIClient getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("No instance of APIClient. " +
                    "Create new instance by subclassing BaseAPIClient.");
        }
        return INSTANCE;
    }

    public static void setInstance(BaseAPIClient instance) {
        INSTANCE = instance;
    }

    protected abstract RestAdapter setupRestAdapter();
    protected abstract RequestInterceptor getRequestInterceptor();
    protected abstract void catchAuthenticationCookie();

}
