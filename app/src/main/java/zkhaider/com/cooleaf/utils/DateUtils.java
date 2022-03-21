package zkhaider.com.cooleaf.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ZkHaider on 7/14/15.
 */
public class DateUtils {

    private static final String TAG = DateUtils.class.getSimpleName();

    public static Date parseDate(String time) {
        if (time != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault());
            try {
                return simpleDateFormat.parse(time);
            } catch (ParseException ex) {
                Log.d(TAG, ex.getMessage());
            }
        }
        return null;
    }

    public static Date parseUnixStamp(long time) {
        return new Date(time * 1000L);
    }

}
