package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kcoleman on 2/17/15.
 */
public class Department {

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
