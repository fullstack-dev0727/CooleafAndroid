package zkhaider.com.cooleaf.cooleafapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.net.CookieHandler;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import zkhaider.com.cooleaf.base.BaseAPIClient;
import zkhaider.com.cooleaf.cooleafapi.utils.APIConfig;
import zkhaider.com.cooleaf.cooleafapi.utils.CooleafAPICookieManager;
import zkhaider.com.cooleaf.cooleafapi.utils.CooleafSessionManager;

/**
 * Created by kcoleman on 11/18/14.
 * Refactored and improved by ZkHaider
 */
public class CooleafAPIClient extends BaseAPIClient {

    public static final String TAG = CooleafAPIClient.class.getSimpleName();
    
    private static CooleafAPIClient sCooleafAPIClient;
    private static Context mContext;
    private static RestAdapter mAsyncRestAdapter;

    public CooleafAPIClient(Context applicationContext) {
        super(applicationContext);
        mContext = applicationContext;
        catchAuthenticationCookie();

        mAsyncRestAdapter = setupRestAdapter();
    }

    public static CooleafAPIClient getInstance() {
        if (sCooleafAPIClient == null)
            sCooleafAPIClient = new CooleafAPIClient(getContext());
        return sCooleafAPIClient;
    }

    @Override
    protected RestAdapter setupRestAdapter() {
        return new RestAdapter.Builder()
                .setEndpoint(APIConfig.getAPIUrl())
                .setRequestInterceptor(getRequestInterceptor())
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();
    }

    @Override
    protected RequestInterceptor getRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Cookie", "_cooleaf_session=" + CooleafSessionManager.getSession(mContext));
                request.addHeader("X-Organization", getOrganization());
            }
        };
    }

    public static RestAdapter getAsynsRestAdapter() {
        return mAsyncRestAdapter;
    }

    @Override
    protected void catchAuthenticationCookie() {
        CooleafAPICookieManager cookieManager = new CooleafAPICookieManager(mApplicationContext);
        CookieHandler.setDefault(cookieManager);
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setOrganization(String organization) {
        setOrganizationInSharedPrefs(organization);
    }

    private static void setOrganizationInSharedPrefs(String organization) {
        SharedPreferences settings = mContext.getSharedPreferences(APIConfig.getPrefsName(), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("organization", organization);
        editor.apply();
    }

    private String getOrganization() {
        Log.d(TAG, "getOrganization() : mOrganization is not null");
        SharedPreferences settings = mContext.getSharedPreferences(APIConfig.getPrefsName(), 0);
        return settings.getString("organization", null);
    }

}