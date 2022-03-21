package zkhaider.com.cooleaf.utils;

import java.net.URLConnection;

import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.cooleafapi.utils.APIConfig;

/**
 * Created by ZkHaider on 7/10/15.
 */
public class AttachmentUtil {

    private static final String TAG = AttachmentUtil.class.getSimpleName();

    public static String checkFileType(String url) {
        return URLConnection.guessContentTypeFromName(APIConfig.getBaseAPIUrl() + url);
    }

}
