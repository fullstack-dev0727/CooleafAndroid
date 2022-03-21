package zkhaider.com.cooleaf.cooleafapi.entities;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kcoleman on 2/2/15.
 */

public class Organization {

    @SerializedName("id")
    private int mId;
    public int getId()
    {
        return mId;
    }

    @SerializedName("name")
    private String mName;
    public String getName() {
        return mName;
    }

    @SerializedName("subdomain")
    private String mSubdomain;
    public String getSubdomain()
    {
        return mSubdomain;
    }

    @SerializedName("logo")
    private Picture mPicture;
    public Picture getPicture() {
        return mPicture;
    }

    @SerializedName("structures")
    private List<ParentTag> mParentTags = new ArrayList<>();
    private HashMap<Integer, List<Tag>> organizationStructures = null;
    public List<ParentTag> getParentTags() {
        Log.d("Organization class", "getParentTags called");
        return mParentTags;
    }

    public HashMap<Integer, List<Tag>> getOrganizationStructures() {
        Log.d("Organization class", "getOrganizationStructures called");
        organizationStructures = new HashMap<>();
        for (ParentTag parentTag : mParentTags) {
            int parentId = parentTag.getId();

            if (!organizationStructures.containsKey(parentId)) {
                List<Tag> tags = new ArrayList<>();
                organizationStructures.put(parentId, tags);
            }

            List<Tag> tags = parentTag.getAllTags();
            for (Tag tag : tags) {
                organizationStructures.get(parentId).add(tag);
            }
        }

        return organizationStructures;
    }
}