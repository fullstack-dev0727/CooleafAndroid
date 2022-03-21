package zkhaider.com.cooleaf.mvp.comments.events;

import retrofit.mime.TypedFile;

/**
 * Created by ZkHaider on 7/10/15.
 */
public class CreateNewCommentEvent {

    private int mFeedableId;
    private String mContent;
    private String mFileCache;
    private String mOriginal;
    private String mThumb;

    public CreateNewCommentEvent(int feedableId, String content, String fileCache,
                                 String original, String thumb) {
        this.mFeedableId = feedableId;
        this.mContent = content;
        this.mFileCache = fileCache;
        this.mOriginal = original;
        this.mThumb = thumb;
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
