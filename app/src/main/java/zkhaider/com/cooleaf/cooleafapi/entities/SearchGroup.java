package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZkHaider on 6/8/15.
 */
public class SearchGroup {

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

    @SerializedName("image_url")
    private String mImageUrl;
    public String getLargePath() {
        return mImageUrl.replace("{{SIZE}}", "500x200");
    }
    public String getSmallPath() {
        return mImageUrl.replace("{{SIZE}}", "164x164");
    }

    @SerializedName("members")
    private List<Participant> mMembers;
    public List<Participant> getMembers() {
        return mMembers;
    }

}
