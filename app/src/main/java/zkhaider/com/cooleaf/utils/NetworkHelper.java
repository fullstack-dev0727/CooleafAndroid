package zkhaider.com.cooleaf.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;
import android.util.Log;

/**
 * Created by Haider on 2/5/2015.
 */
public class NetworkHelper {

    private static final String TAG = NetworkHelper.class.getSimpleName();

    public static boolean isInternetAvailable(Context context) {

        NetworkInfo networkInfo = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (networkInfo == null) {
            Log.d(TAG, "No Network Connection");
            return false;
        } else {
            if (networkInfo.isConnected()) {
                Log.d(TAG, "Network Connection Available...");
                return true;
            } else {
                Log.d(TAG, "No Network Connection");
                return false;
            }
        }

    }

}
