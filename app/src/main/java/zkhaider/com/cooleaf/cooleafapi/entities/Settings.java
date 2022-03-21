package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Haider on 3/3/2015.
 */
public class Settings {

    @SerializedName("send_daily_digest")
    private boolean mSendDailyDigest;
    public boolean getSendDailyDigest() {
        return mSendDailyDigest;
    }

    @SerializedName("send_weekly_digest")
    private boolean mSendWeeklyDigest;
    public boolean getSendWeeklyDigest() {
        return mSendWeeklyDigest;
    }

}
