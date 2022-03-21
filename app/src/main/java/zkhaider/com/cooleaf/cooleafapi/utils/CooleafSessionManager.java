package zkhaider.com.cooleaf.cooleafapi.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kcoleman on 2/3/15.
 */
public class CooleafSessionManager {

    private String mSession;
    private static final String PREFS_NAME = "CooleafAPIClient";

    private CooleafSessionManager(){}

    public static void setSession(String session, Context context){
        SharedPreferences settings = context.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("session", session);
        editor.commit();
    }
    public static String getSession(Context context) {
        SharedPreferences settings = context.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        return settings.getString("session", null);
    }

    public static void clearSession(Context context){
        setSession(null,context);
    }

}
