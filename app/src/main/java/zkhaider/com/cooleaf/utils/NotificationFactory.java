package zkhaider.com.cooleaf.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.ui.activities.MainActivity;

/**
 * Created by ZkHaider on 5/24/15.
 */
public class NotificationFactory {

    private static final int NOTIFICATION_ID = 1;

    private Context mContext;
    private static NotificationFactory mNotificationFactory;
    private Target mLoadedTarget;
    private NotificationManager mNotificationManager;
    private PendingIntent mPendingIntent;
    private NotificationCompat.Builder mBuilder;
    private NotificationCompat.WearableExtender mWearableExtender;
    private NotificationCompat.Builder mWearBuilder;
    private NotificationManagerCompat mNotificationManagerCompat;

    private String mTitle;
    private String mMessage;
    private Event mEvent;

    private NotificationFactory(Context context) {
        this.mContext = context;
    }

    public static NotificationFactory getInstance(Context context) {
        if (mNotificationFactory == null)
            mNotificationFactory = new NotificationFactory(context);
        return mNotificationFactory;
    }

    public void sendNotification(String title, String message, Event event) {

        this.mTitle = title;
        this.mMessage = message;
        this.mEvent = event;

        // Get the notification manager
        mNotificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Build the notification for the user to see
        mBuilder = new NotificationCompat.Builder(mContext)
                        .setContentTitle(title)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentText(message)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .addAction(R.drawable.ic_launcher, "VIEW", mPendingIntent);

        // Build a Wear Notification for the user to see
        mWearableExtender = new NotificationCompat.WearableExtender()
                .setHintShowBackgroundOnly(true);

        mWearBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(title)
                .setContentText(message);

        // Intialize Picasso Target
        mLoadedTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                handleLoadedBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                handleUnloadedBitmap();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        // Load Target into Picasso
        Picasso.with(mContext).load(event.getImage().getUrl()).into(mLoadedTarget);
    }

    private void handleLoadedBitmap(Bitmap bitmap) {

        // Get the pending intent from the Registration Class
        mPendingIntent = PendingIntent.getActivity(mContext, 0,
                new Intent(mContext, MainActivity.class), 0);

        // Send handheld notification
        mBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher))
                .bigPicture(bitmap)
                .setBigContentTitle(mTitle)
                .setSummaryText(mMessage));
        mBuilder.setContentIntent(mPendingIntent);

        Notification handheldNotification = mBuilder.build();

        handheldNotification.defaults |= Notification.DEFAULT_LIGHTS;
        handheldNotification.defaults |= Notification.DEFAULT_VIBRATE;
        handheldNotification.defaults |= Notification.DEFAULT_SOUND;

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        // Send wearable notification
        mWearBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher))
                .bigPicture(bitmap)
                .setBigContentTitle(mTitle)
                .setSummaryText(mMessage));
        mWearBuilder.extend(mWearableExtender);

        Notification wearNotification = mWearBuilder.build();
        mNotificationManagerCompat = NotificationManagerCompat.from(mContext);
        mNotificationManagerCompat.notify(NOTIFICATION_ID, wearNotification);
    }

    private void handleUnloadedBitmap() {
        // Send handheld notification
        mBuilder.setContentIntent(mPendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        // Send wearable notification
        mWearBuilder.extend(mWearableExtender);

        Notification wearNotification = mWearBuilder.build();
        mNotificationManagerCompat = NotificationManagerCompat.from(mContext);
        mNotificationManagerCompat.notify(NOTIFICATION_ID, wearNotification);
    }

}
