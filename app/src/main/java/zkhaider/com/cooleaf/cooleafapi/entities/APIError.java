package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kcoleman on 2/1/15.
 */
public class APIError {

    public APIError(String message, int status)
    {
        mMessage = message;
        mStatus = status;
    }

    private int mStatus;

    @SerializedName("message")
    private String mMessage;
    public String getMessage()
    {
        return mMessage;
    }

    @SerializedName("error")
    private String mError;
    public String getError()
    {
        return mError;
    }

    public boolean needToReauthenticate()
    {
        return mStatus == 401 || mStatus >= 500;
    }


    public String getAlert() {
        if(mMessage != null)
            return mMessage;
        if(mError != null)
            return mError;
        return null;
    }
}
