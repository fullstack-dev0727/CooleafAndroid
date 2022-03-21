package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kcoleman on 2/1/15.
 */
public class Picture {

    @SerializedName("original")
    private String mOriginal;
    public String getOriginal() {
        return mOriginal;
    }

    @SerializedName("versions")
    private Versions mVersions;
    public Versions getVersions()
    {
        return mVersions;
    }

    public void setOriginal(String original) {
        mOriginal = original;
    }

    public void setVersions(Versions versions) {
        mVersions = versions;
    }
}