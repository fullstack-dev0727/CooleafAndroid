package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Haider on 2/28/2015.
 */
public class Registration {

    public static final String TAG = Registration.class.getSimpleName();

    @SerializedName("name")
    private String mName;
    public String getName() {
        return mName;
    }

    @SerializedName("email")
    private String mEmail;
    public String getEmail() {
        return mEmail;
    }

    @SerializedName("token")
    private String mToken;
    public String getToken() {
        return mToken;
    }

    @SerializedName("gender")
    private String mGender;
    public String getGender() {
        return mGender;
    }

    @SerializedName("structures")
    private List<Tag> mTags;
    private HashMap<Integer, List<Tag>> myTags = null;
    private List<Integer> mCategoryIds = new ArrayList<>();
    public List<Tag> getTags() {
        return mTags;
    }

    public HashMap<Integer, List<Tag>> createMyTags() {
        myTags = new HashMap<>();

        for (Tag tag : mTags) {
            int parentId = tag.getParentId();
            if (!myTags.containsKey(parentId)) {
                List<Tag> newList = new ArrayList<>();
                myTags.put(parentId, newList);
            }

            myTags.get(parentId).add(tag);
        }

        return myTags;
    }

    public List<Integer> getCategoryIds() {
        mCategoryIds = new ArrayList<>();
        for (Tag tag : mTags) {
            int tagId = tag.getId();
            mCategoryIds.add(tagId);
        }

        return mCategoryIds;
    }

    @SerializedName("all_structures")
    private List<ParentTag> mParentTags;
    public List<ParentTag> getParentTags() {
        return mParentTags;
    }

    @SerializedName("chosen_structure_ids")
    private HashMap<String, List<Integer>> chosenStructures;
    public HashMap<String, List<Integer>> getChosenStructures() {
        return chosenStructures;
    }

}
