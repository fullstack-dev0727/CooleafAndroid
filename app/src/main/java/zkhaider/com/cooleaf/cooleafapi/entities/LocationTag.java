package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Haider on 2/18/2015.
 */
public class LocationTag {

    @SerializedName("id")
    private int mId;
    public int getId() {
        return mId;
    }

    @SerializedName("name")
    private String mName;
    public String getName() {
        return mName;
    }

    @SerializedName("type")
    private String mType;
    public String getType() {
        return mType;
    }

    @SerializedName("parent_id")
    private int mParentId;
    public int getParentId() {
        return mParentId;
    }

    @SerializedName("default")
    private boolean mDefault;
    public boolean getDefault() {
        return mDefault;
    }

}
