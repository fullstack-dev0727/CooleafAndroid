package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kcoleman on 2/11/15.
 */
public class TimeZone {

    @SerializedName("name")
    private String mName;
    public String getName() {
        return mName;
    }

    @SerializedName("offset")
    private String mOffset;
    public String getOffset() {
        return mOffset;
    }

    @SerializedName("abbreviation")
    private String mAbbreviation;
    public String getAbbreviation() {
        return mAbbreviation;
    }

    @SerializedName("moment_name")
    private String mMomentName;
    public String getMomentName() {
        return mMomentName;
    }

}
