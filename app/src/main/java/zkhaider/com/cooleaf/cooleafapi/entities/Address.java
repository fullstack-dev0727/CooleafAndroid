package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kcoleman on 2/11/15.
 */
public class Address {

    public boolean hasAddress()
    {
        return displayAddress() != "";
    }

    @SerializedName("address1")
    private String mAddress1;
    public String getAddress1() {
        return mAddress1;
    }

    @SerializedName("address2")
    private String mAddress2;
    public String getAddress2() {
        return mAddress2;
    }

    @SerializedName("city")
    private String mCity;
    public String getCity() {
        return mCity;
    }

    @SerializedName("state")
    private String mState;
    public String getState() {
        return mState;
    }

    @SerializedName("zip")
    private String mZip;
    public String getZip() {
        return mZip;
    }

    @SerializedName("name")
    private String mName;
    public String getName() {
        return mName;
    }

    public String displayAddress()
    {
        String output = "";
        if (getName() != null && getName() != "")
            output += getName() + "\n";
        if (getAddress1() != null && getAddress1() != "")
            output += getAddress1() + "\n";
        if (getAddress2() != null && getAddress2() != "")
            output += getAddress2() + "\n";
        if (getCity() != null && getCity() != "")
            output += getCity() + ", ";
        if (getState() != null && getState() != "")
            output += getState() + " ";
        if (getZip() != null && getZip() != "")
            output += getZip();

        return output;
    }
}