package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kcoleman on 2/17/15.
 */
public class EventsToJoinRequest {

    @SerializedName("ids")
    private List<Integer> mEventIds;
    public EventsToJoinRequest(List<Integer> eventIds)
    {
        mEventIds = eventIds;
    }

}
