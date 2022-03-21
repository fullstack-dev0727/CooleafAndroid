package zkhaider.com.cooleaf.gcm;

import android.content.Context;

import com.google.android.gcm.GCMRegistrar;

import zkhaider.com.cooleaf.utils.LocalPreferences;

/**
 * Created by ZkHaider on 5/27/15.
 */
public class GCMConfig {

    private static final String SENDER_ID = "476458294068";
    private static final String EXTRA_MESSAGE = "message";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int NOTIFICATION_ID = 1;
    public static final String SEND_TOKEN_TO_SERVER = "sentTokenToServer";

    // Sender constants
    public static final String NAME = "name";
    public static final String API_KEYS = "apiKeys";
    public static final String APP_TOKENS = "appTokens";
    public static final String OTHER_TOKENS = "otherTokens";
    public static final String TOPICS = "topics";
    public static final String GROUPS = "groups";
    // DeviceGroup constants
    public static final String NOTIFICATION_KEY_NAME = "notificationKeyName";
    public static final String NOTIFICATION_KEY = "notificationKey";
    public static final String TOKENS = "tokens";
    // TaskTracker constants
    public static final String TAG = "TAG";
    public static final String WINDOW_START_ELAPSED_SECS = "WINDOW_START_ELAPSED_SECS";
    public static final String WINDOW_STOP_ELAPSED_SECS = "WINDOW_STOP_ELAPSED_SECS";
    public static final String PERIOD = "PERIOD";
    public static final String FLEX = "FLEX";
    public static final String CREATED_AT_ELAPSED_SECS = "CREATED_AT_ELAPSED_SECS";
    public static final String CANCELLED = "CANCELLED";
    public static final String EXECUTED = "EXECUTED";

    public static String getSenderId() {
        return SENDER_ID;
    }

    public static String getExtraMessage() {
        return EXTRA_MESSAGE;
    }

    public static String getPropertyRegId() {
        return PROPERTY_REG_ID;
    }

    public static String getPropertyAppVersion() {
        return PROPERTY_APP_VERSION;
    }

    public static int getPlayServicesResolutionRequest() {
        return PLAY_SERVICES_RESOLUTION_REQUEST;
    }

    public static int getNotificationId() {
        return NOTIFICATION_ID;
    }

    public static String getTOKENS() {
        return TOKENS;
    }

    public static void GCMRegistration_id(Context context) {
        try {
            GCMRegistrar.checkDevice(context);
            GCMRegistrar.checkManifest(context);

            final String regId = GCMRegistrar.getRegistrationId(context);

            if (regId.equals("")) {
                if (GCMRegistrar.isRegistered(context))
                    GCMRegistrar.unregister(context);
                GCMRegistrar.register(context, GCMConfig.getSenderId());
            } else {
                LocalPreferences.set("gcm_token", regId);
            }
        } catch (Exception e) {
        }
    }
}
