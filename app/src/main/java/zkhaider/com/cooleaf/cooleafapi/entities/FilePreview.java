package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import zkhaider.com.cooleaf.cooleafapi.utils.APIConfig;

/**
 * Created by Haider on 2/26/2015.
 */
public class FilePreview {

    @SerializedName("file_cache")
    private String mFileCacheUrl;
    public String getFileCache() {
        return mFileCacheUrl;
    }
    public String getFileCacheUrl() {
        return getURL(getFileCache());
    }

    @SerializedName("original")
    private String mOriginalUrl;
    public String getOriginal() {
        return mOriginalUrl;
    }
    public String getOriginalUrl() {
        return getURL(getOriginal());
    }

    @SerializedName("versions")
    private Versions mVersions;
    public Versions getVersions() {
        return mVersions;
    }

    private String getURL(String path) {
        return APIConfig.getBaseAPIUrl() + path;
    }


}
