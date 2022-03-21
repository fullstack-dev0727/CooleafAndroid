package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZkHaider on 4/1/15.
 */
public class GCMNotification {

    @SerializedName("from")
    private String mFrom;
    public String getFrom() {
        return mFrom;
    }

    @SerializedName("key1")
    private String mTitle;
    public String getTitle() {
        return mTitle;
    }

    @SerializedName("key2")
    private String mMessage;
    public String getMessage() {
        return mMessage;
    }

}
