package zkhaider.com.cooleaf.cooleafapi.utils;

/**
 * Created by kcoleman on 2/1/15.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class CooleafAPICookieManager extends CookieManager {

    private static String PREFS_NAME = "CooleafAPIClient";
    private Context mContext;

    public CooleafAPICookieManager(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void put(URI uri, Map<String, List<String>> stringListMap) throws IOException {
        super.put(uri, stringListMap);
        if (stringListMap != null && stringListMap.get("Set-Cookie") != null)
            for (String data : stringListMap.get("Set-Cookie")) {
                if (data.contains("_cooleaf_session")) {
                    CooleafSessionManager.setSession(getCookieData(data), mContext);
                }
            }
    }
    private String getCookieData(String data)
    {
        return data.replace("_cooleaf_session=","");
    }
}