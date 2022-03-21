package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZkHaider on 10/4/15.
 */

/***
 * JSON Representation:
 {
    "id": 1460,
    "name": "hmmm",
    "type": "User",
    "tags": [
        "Duluth (Satellite Blvd)",
        "Marketing",
        "General"
     ],
     "url": "http://api.staging.do.cooleaf.monterail.eu/v2/users/1460",
     "image_url": "http://assets.staging.do.cooleaf.monterail.eu/profile_pictures/uni-profile_ios-default.png"
 }
 */
public class SearchQuery {

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

    @SerializedName("content")
    private String mContent;
    public String getContent() {
        return mContent;
    }

    @SerializedName("type")
    private String mType;
    public String getType() {
        return mType;
    }

    @SerializedName("tags")
    private List<String> mTags;
    public List<String> getTags() {
        return mTags;
    }

    @SerializedName("url")
    private String mUrl;
    public String getUrl() {
        return mUrl;
    }

    @SerializedName("image_url")
    private String mImageUrl;
    public String getImageUrl() {
        return mImageUrl;
    }

}
