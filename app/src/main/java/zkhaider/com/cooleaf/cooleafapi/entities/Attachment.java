package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZkHaider on 7/10/15.
 */
public class Attachment {

    @SerializedName("original")
    private String mOriginal;
    public String getOriginal() {
        return mOriginal;
    }

    @SerializedName("versions")
    private Versions mVersions;
    public Versions getVersions() {
        return mVersions;
    }

}
