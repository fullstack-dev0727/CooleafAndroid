package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import zkhaider.com.cooleaf.cooleafapi.utils.APIConfig;

/**
 * Created by kcoleman on 2/1/15.
 */
public class Versions {

    private String getURL(String path) {
        return APIConfig.getBaseAPIUrl() + path;
    }

    @SerializedName("thumb")
    private String mThumb;
    public String getThumb()
    {
        return mThumb;
    }
    public String getThumbURL() { return getURL(getThumb()); }


    @SerializedName("mobile")
    private String mMobile;
    public String getMobile()
    {
        return mMobile;
    }
    public String getMobileURL() { return getURL(getMobile()); }


    @SerializedName("icon")
    private String mIcon;
    public String getIcon()
    {
        return mIcon;
    }
    public String getIconURL() { return getURL(getIcon()); }


    @SerializedName("small")
    private String mSmall;
    public String getSmall()
    {
        return mSmall;
    }
    public String getSmallURL() { return getURL(getSmall()); }


    @SerializedName("medium")
    private String mMedium;
    public String getMedium()
    {
        return mMedium;
    }
    public String getMediumURL() { return getURL(getMedium()); }


    @SerializedName("large")
    private String mLarge;
    public String getLarge()
    {
        return mLarge;
    }
    public String getLargeURL() { return getURL(getLarge()); }


    @SerializedName("big")
    private String mBig;
    public String getBig()
    {
        return mBig;
    }
    public String getBigURL() { return getURL(getBig()); }


    @SerializedName("main")
    private String mMain;
    public String getMain() {
        return mMain;
    }
    public String getMainURL() {
        return getURL(getMain());
    }

    @SerializedName("cover")
    private String mCover;
    public String getCover() {
        return mCover;
    }
    public String getCoverURL() {
        return getURL(getCover());
    }

    public void setThumb(String thumb) {
        mThumb = thumb;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public void setSmall(String small) {
        mSmall = small;
    }

    public void setMedium(String medium) {
        mMedium = medium;
    }

    public void setLarge(String large) {
        mLarge = large;
    }

    public void setBig(String big) {
        mBig = big;
    }

    public void setMain(String main) {
        mMain = main;
    }

    public void setCover(String cover) {
        mCover = cover;
    }
}
