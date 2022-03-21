package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haider on 3/3/2015.
 */
public class Edit {

    @SerializedName("category_ids")
    private List<Integer> mCategoryIds = new ArrayList<>();
    public List<Integer> getCategoryIds() {
        return mCategoryIds;
    }
    public void setCategoryIds(List<Integer> categoryIds) {
        this.mCategoryIds = categoryIds;
    }

    @SerializedName("email")
    private String mEmail;
    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    @SerializedName("file_cache")
    private String mFileCache;
    public String getFileCache() {
        return mFileCache;
    }

    public void setFileCache(String fileCache) {
        this.mFileCache = fileCache;
    }

    @SerializedName("id")
    private int mId;
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    @SerializedName("name")
    private String mName;
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    @SerializedName("profile")
    private Profile mProfile;
    public Profile getProfile() {
        return mProfile;
    }

    public void setProfile(Profile profile) {
        this.mProfile = profile;
    }

    @SerializedName("removed_picture")
    private boolean mRemovedPicture;
    public boolean getRemovedPicture() {
        return mRemovedPicture;
    }

    public void setRemovedPicture(boolean removedPicture) {
        this.mRemovedPicture = removedPicture;
    }

    @SerializedName("reward_points")
    private int mRewardPoints;
    public int getRewardPoints() {
        return mRewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.mRewardPoints = rewardPoints;
    }

    @SerializedName("role")
    private Role mRole;
    public Role getRole() {
        return mRole;
    }

    public void setRole(Role role) {
        this.mRole = role;
    }

}
