package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Haider on 2/4/2015.
 */
public class Participant {

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

    @SerializedName("profile")
    private Profile mProfile;
    public Profile getProfile() {
        return mProfile;
    }

    @SerializedName("picture_url")
    private String mPictureUrl;
    public String getPictureUrl() {
        return mPictureUrl;
    }

    @SerializedName("primary_tag_names")
    private List<String> mPrimaryTagNames;
    public List<String> getPrimaryTagNames() {
        return mPrimaryTagNames;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setProfile(Profile profile) {
        mProfile = profile;
    }

    public void setPictureUrl(String pictureUrl) {
        mPictureUrl = pictureUrl;
    }

    public void setPrimaryTagNames(List<String> primaryTagNames) {
        mPrimaryTagNames = primaryTagNames;
    }

}
