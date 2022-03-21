package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haider on 3/5/2015.
 */
public class RegistrationRequest {

    @SerializedName("token")
    private String mToken;
    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }

    @SerializedName("name")
    private String mName;
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    @SerializedName("organization_id")
    private int mOrganizationId;
    public int getOrganizationId() {
        return mOrganizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.mOrganizationId = organizationId;
    }

    @SerializedName("organization_subdomain")
    private String mOrganizationSubdomain;
    public String getOrganizationSubdomain() {
        return mOrganizationSubdomain;
    }

    @SerializedName("gender")
    private String mGender;
    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        this.mGender = gender;
    }

    @SerializedName("password")
    private String mPassword;
    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    @SerializedName("tag_ids")
    private List<Integer> mTags = new ArrayList<>();
    public List<Integer> getTags() {
        return mTags;
    }

    public void setTagIds(List<Integer> tags) {
        mTags = tags;
    }

}
