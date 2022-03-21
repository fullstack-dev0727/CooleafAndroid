package zkhaider.com.cooleaf.cooleafapi.utils;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class APIConfig {

    private static String PREFS_NAME = "CooleafAPIClient";
    private static final String API_URL = "http://testorg.staging.do.cooleaf.monterail.eu/api";
    public static final String BASE_API_URL = "http://testorg.staging.do.cooleaf.monterail.eu";
    private static final String PLATFORM = "android";
    private static final String UPLOADER_PATH = "profile_picture";
    private static final String COMMENTABLE_TYPE = "Event";

    public static String getPrefsName() {
        return PREFS_NAME;
    }

    public static String getAPIUrl() {
        return API_URL;
    }

    public static String getBaseAPIUrl() {
        return BASE_API_URL;
    }

    public static String getPlatform() {
        return PLATFORM;
    }

    public static String getUploaderPath() {
        return UPLOADER_PATH;
    }

    public static String getCommentableType() {
        return COMMENTABLE_TYPE;
    }
}
