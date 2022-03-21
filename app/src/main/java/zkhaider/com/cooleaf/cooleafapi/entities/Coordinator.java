package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Haider on 2/15/2015.
 */
public class Coordinator {

    @SerializedName("id")
    private Integer mId;
    public Integer getId()
    {
        return mId;
    }

    @SerializedName("foreign")
    private boolean mForeign;
    public boolean getForeign() {
        return mForeign;
    }

    @SerializedName("name")
    private String mName;
    public String getName()
    {
        return mName;
    }

    @SerializedName("email")
    private String mEmail;
    public String getEmail()
    {
        return mEmail;
    }

    @SerializedName("profile")
    private Profile mProfile;
    public Profile getProfile() {
        return mProfile;
    }

}
