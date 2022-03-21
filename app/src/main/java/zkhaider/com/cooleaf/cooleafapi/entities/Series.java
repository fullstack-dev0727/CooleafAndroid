package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Haider on 2/15/2015.
 */
public class Series {

    @SerializedName("id")
    private Integer mId;
    public Integer getId() {
        return mId;
    }

    @SerializedName("event_ids")
    private ArrayList<Integer> mEventIds = new ArrayList<>();
    public ArrayList<Integer> getEventIds() {
        return mEventIds;
    }

}
