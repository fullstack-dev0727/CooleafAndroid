package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kcoleman on 2/1/15.
 */

public class User {

    @SerializedName("id")
    private Integer mId;
    public Integer getId()
    {
        return mId;
    }

    @SerializedName("name")
    private String mName;
    public String getName()
    {
        return mName;
    }

    @SerializedName("email")
    private String mEmail;
    public String getEmail()
    {
        return mEmail;
    }

    @SerializedName("categories")
    private List<Interest> mInterests;
    public List<Interest> getCategories()
    {
        return mInterests;
    }

    @SerializedName("role")
    private Role mRole;
    public Role getRole()
    {
        return mRole;
    }

    @SerializedName("reward_points")
    private int mRewardPoints;
    public int getRewardPoints() {
        return mRewardPoints;
    }

    @SerializedName("profile")
    private Profile mProfile;
    public Profile getProfile() {
        return mProfile;
    }

    public String getShortName()
    {
        if (getName().length() > 18) {
            return getName().substring(0, 18) + "...";
        }
        return getName();
    }

    public void setId(Integer id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setInterests(List<Interest> interests) {
        mInterests = interests;
    }

    public void setRole(Role role) {
        mRole = role;
    }

    public void setRewardPoints(int rewardPoints) {
        mRewardPoints = rewardPoints;
    }

    public void setProfile(Profile profile) {
        mProfile = profile;
    }

}
