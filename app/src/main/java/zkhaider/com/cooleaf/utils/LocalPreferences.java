package zkhaider.com.cooleaf.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ZkHaider on 10/6/15.
 */
public class LocalPreferences {

    private static Context mContext;

    public static void init(Context initContext) {
        mContext = initContext;
    }

    public static void set(String prefKey, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt( prefKey, value );
        edit.apply();
    }

    public static void set(String prefKey, long value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putLong( prefKey, value );
        edit.apply();
    }

    public static void set(String prefKey, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString( prefKey, value );
        edit.apply();
    }

    public static void set(String prefKey, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean( prefKey, value );
        edit.apply();
    }

    public static boolean hasPreference(String prefKey) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.contains(prefKey);
    }

    public static int getInt(String prefKey) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getInt(prefKey, 0);
    }

    public static int getInt(String prefKey, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getInt(prefKey, value);
    }

    public static boolean getBoolean(String prefKey) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getBoolean(prefKey, false);
    }

    public static boolean getBoolean(String prefKey, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getBoolean(prefKey, value);
    }

    public static long getLong(String prefKey, long value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getLong(prefKey, value);
    }

    public static long getLong(String prefKey) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        try {
            return preferences.getLong(prefKey, 0L);
        } catch (ClassCastException classEx) {
            Integer intValue = preferences.getInt(prefKey, 0);
            return intValue.longValue();
        }
    }

    public static String getString(String prefKey) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString(prefKey, null);
    }

    public static String getString(String prefKey, String defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString(prefKey, defaultValue);
    }

}
