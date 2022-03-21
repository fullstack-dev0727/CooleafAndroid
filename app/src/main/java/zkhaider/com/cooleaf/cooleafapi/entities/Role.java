package zkhaider.com.cooleaf.cooleafapi.entities;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by kcoleman on 2/2/15.
 */
public class Role {

    @SerializedName("active")
    private boolean mActive;
    public boolean getActive() {
        return mActive;
    }

    @SerializedName("rights")
    private String mRights;
    public String getRights() {
        return mRights;
    }

    @SerializedName("organization")
    private Organization mOrganization;
    public Organization getOrganization()
    {
        return mOrganization;
    }

    @SerializedName("branch")
    private Branch mBranch;
    public Branch getBranch() {
        return mBranch;
    }

    @SerializedName("department")
    private Department mDepartment;
    public Department getDepartment() {
        return mDepartment;
    }

    @SerializedName("structure_tags")
    private List<Tag> mTags;
    private HashMap<Integer, List<Tag>> myTags = null;
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

    @SerializedName("structures")
    private HashMap<String, List<Integer>> mStructureMap;
    private List<ParentTag> myStructures = null;
    public HashMap<String, List<Integer>> getStructureMap() {
        return mStructureMap;
    }

    public List<ParentTag> getStructures() {
        if (myStructures == null) {
            createMyStructures();
        }
        return myStructures;
    }

    public void setParentTag(ParentTag parentTag) {

        int parentId = parentTag.getId();
        String parsedId = Integer.toString(parentId);

        if (!mStructureMap.containsKey(parsedId)) {
            List<Integer> newList = new ArrayList<>();
            for (Tag tag : parentTag.getAllTags()) {
                int id = tag.getId();
                newList.add(id);
            }
        } else if (mStructureMap.containsKey(parsedId)) {
            mStructureMap.remove(parsedId);
            List<Integer> newList = new ArrayList<>();
            for (Tag tag : parentTag.getAllTags()) {
                int id = tag.getId();
                newList.add(id);
            }
            mStructureMap.put(parsedId, newList);
        }

    }

    public void setStructureMap(HashMap<String, List<Integer>> structureMap) {
        Map<String, List<Integer>> treeMap = new TreeMap<>(structureMap);

        for (Map.Entry<String, List<Integer>> entry : treeMap.entrySet()) {
            Log.d("Role Class", "Key: " + entry.getKey() + " Value: " + entry.getValue().toString());
        }

        this.mStructureMap = structureMap;
    }

    private void createMyStructures() {
        myStructures = new ArrayList<>();
        List<ParentTag> parentTags = mOrganization.getParentTags();
        Map<Integer, ParentTag> map = new HashMap<>();
        for (ParentTag parentTag : parentTags) map.put(parentTag.getId(), parentTag);

        for (String key : mStructureMap.keySet()) {
            ParentTag organizationStructure = map.get(Integer.parseInt(key));

            if (organizationStructure != null) {
                ParentTag newParent = new ParentTag();
                newParent.setTags(organizationStructure.getAllTags());
                newParent.setName(organizationStructure.getName());
                ParentTag myStructure = createMyStructure(newParent, key);
                myStructures.add(myStructure);
            }
        }
    }

    private ParentTag createMyStructure(ParentTag parentTag, String key) {
        List<Tag> tags = parentTag.getAllTags();
        List<Integer> tagIds = mStructureMap.get(key);

        List<Tag> newTags = new ArrayList<>();

        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            boolean hasTagId = tagIds.contains(tag.getId());
            if (hasTagId) {
                newTags.add(tag);
            }
        }
        parentTag.setTags(newTags);

        return parentTag;
    }
}