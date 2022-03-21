package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Haider on 2/18/2015.
 */
public class Branch {

    @SerializedName("name")
    private String mName;
    public String getName() {
        return mName;
    }

    @SerializedName("default")
    private boolean mDefault;
    public boolean getDefault() {
        return mDefault;
    }

}
