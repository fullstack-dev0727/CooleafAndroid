package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kcoleman on 2/2/15.
 */
public class Image {

    @SerializedName("url")
    private String mUrl;
    public String getUrl() {
        return "http:" + mUrl.replace("{{SIZE}}","164x164");
    }
    public String getWideUrl() {
        if (mUrl.contains("{{SIZE}}"))
            return "http:" + mUrl.replace("{{SIZE}}","1600x400");
        else
            return mUrl;
    }
    public String getSquareUrl() {
        return "http:" + mUrl.replace("{{SIZE}}", "164x164");
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}
