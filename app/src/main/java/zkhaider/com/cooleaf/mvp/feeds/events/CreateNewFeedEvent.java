package zkhaider.com.cooleaf.mvp.feeds.events;

import retrofit.mime.MultipartTypedOutput;

/**
 * Created by ZkHaider on 7/12/15.
 */
public class CreateNewFeedEvent {

    private String mType;
    private int mFeedableId;
    private String mContent;
    private String mFileCache;
    private String mOriginal;
    private String mThumb;

    public CreateNewFeedEvent(String type, int feedableId, String content, String fileCache, String original, String thumb) {
        this.mType = type;
        this.mFeedableId = feedableId;
        this.mContent = content;
        this.mFileCache = fileCache;
        this.mOriginal = original;
        this.mThumb = thumb;
    }

    public String getType() {
        return mType;
    }

    public int getFeedableId() {
        return mFeedableId;
    }

    public String getContent() {
        return mContent;
    }

    public String getFileCache() {
        return mFileCache;
    }

    public String getOriginal() {
        return mOriginal;
    }

    public String getThumb() {
        return mThumb;
    }
}
