package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZkHaider on 6/8/15.
 */
public class SearchResult {

    @SerializedName("people")
    private List<Participant> mPeople;
    public List<Participant> getPeople() {
        return mPeople;
    }

    @SerializedName("events")
    private List<Event> mEvents;
    public List<Event> getEvents() {
        return mEvents;
    }

    @SerializedName("groups")
    private List<SearchGroup> mGroups;
    public List<SearchGroup> getGroups() {
        return mGroups;
    }

    @SerializedName("posts")
    private List<Post> mPosts;
    public List<Post> getPosts() {
        return mPosts;
    }

}
