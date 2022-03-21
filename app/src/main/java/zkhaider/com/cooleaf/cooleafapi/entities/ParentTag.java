package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haider on 2/28/2015.
 */
public class ParentTag {

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
    public void setName(String name) { mName = name; }

    @SerializedName("tags")
    private List<Tag> mTags = new ArrayList<>();
    public List<Tag> getAllTags() {
        return mTags;
    }

    public void setTags(List<Tag> tags) {
        this.mTags = tags;
    }

    @SerializedName("required")
    private boolean mRequired;
    public boolean getRequired() {
        return mRequired;
    }

    @SerializedName("primary")
    private boolean mPrimary;
    public boolean getPrimary() {
        return mPrimary;
    }

}
