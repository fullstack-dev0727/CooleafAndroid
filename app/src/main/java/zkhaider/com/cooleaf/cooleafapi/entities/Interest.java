package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kcoleman on 2/2/15.
 */
public class Interest {

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

    @SerializedName("active")
    private boolean mActive;
    public boolean getActive() {
        return mActive;
    }

    @SerializedName("parent_type")
    private String mParentType;
    public String getParentType() {
        return mParentType;
    }

    @SerializedName("image")
    private Image mImage;
    public Image getImage() {
        return mImage;
    }

    @SerializedName("users_count")
    private int mUserCount;
    public int getUserCount() {
        return mUserCount;
    }

}
