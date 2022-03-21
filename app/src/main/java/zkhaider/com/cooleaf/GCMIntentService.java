package zkhaider.com.cooleaf;

/**
 * Created by Petar Vasilev on 10/21/15.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import java.lang.reflect.Method;

import zkhaider.com.cooleaf.gcm.GCMConfig;
import zkhaider.com.cooleaf.ui.activities.MainActivity;
import zkhaider.com.cooleaf.utils.LocalPreferences;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = GCMIntentService.class.getSimpleName();

    public GCMIntentService() {
        super(GCMConfig.getSenderId());
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        LocalPreferences.set("gcm_token", registrationId);
    }

    /**
    * Method called on device un registred
    */
    @Override
    protected void onUnregistered(Context context, String registrationId) {

    }

    /**
    * Method called on Receiving a new message
    */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String from = bundle.getString("from");
        String key1 = bundle.getString("key1");
        String key2 = bundle.getString("key2");
        String sound = bundle.getString("sound");
        showNotification(context, from, key1, key2, sound);
        Log.i(TAG, "Received messages notification");

    }
    /**
    * Method called on receiving a deleted message
    */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        // notifies user
    }
    /**
    * Method called on Error
    */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
    // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }

    public static void showNotification(Context context, String from, String key1, String key2, String sound) {
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("from", from);
        notificationIntent.putExtra("key1", key1);
        notificationIntent.putExtra("key2", key2);
        notificationIntent.putExtra("sound", sound);

        // set intent so it does not start a new activity
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) (System.currentTimeMillis()), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT < 16) {
            Notification notification = new Notification(R.drawable.ic_launcher, key2, when);
            try {
                Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                deprecatedMethod.invoke(notification, context, key1, key2, pendingIntent);
            } catch (Exception e) {}
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults = Notification.DEFAULT_ALL;
            notificationManager.notify((int) when, notification);
        } else {
            Notification notification = new Notification.Builder(context)
                    .setContentTitle(key1)
                    .setContentText(key2).setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pendingIntent).setWhen(when).setAutoCancel(true)
                    .build();

            NotificationCompat.Builder nBuilder;
            nBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(key1)  // <--- add this
                    .setContentText(key2)   // <--- and this
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_MAX);

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(key1);
            inboxStyle.addLine(key2);
            nBuilder.setStyle(inboxStyle);

            nBuilder.setContentIntent(pendingIntent);
            nBuilder.setDeleteIntent(null);
            nBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify((int) when, nBuilder.build());
        }

    }
}