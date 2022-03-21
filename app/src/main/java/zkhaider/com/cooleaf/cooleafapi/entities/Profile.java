package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;


/**
 * Created by kcoleman on 2/1/15.
 */
public class Profile {

    @SerializedName("gender")
    private String mGender;
    public String getGender() {
        return mGender;
    }

    @SerializedName("picture")
    private Picture mPicture;
    public Picture getPicture() {
        return mPicture;
    }

    @SerializedName("settings")
    private Settings mSettings;
    public Settings getSettings() {
        return mSettings;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public void setPicture(Picture picture) {
        mPicture = picture;
    }

    public void setSettings(Settings settings) {
        mSettings = settings;
    }
}
