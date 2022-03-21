package zkhaider.com.cooleaf.mvp.comments.events;

/**
 * Created by ZkHaider on 8/14/15.
 */
public class PostEventCommentEvent {

    private String mCommentableType;
    private int mEventId;
    private String mContent;
    private String mFileCache;
    private String mOriginal;
    private String mThumb;

    public PostEventCommentEvent(String commentableType, int eventId, String content, String fileCache,
                                 String original, String thumb) {
        this.mCommentableType = commentableType;
        this.mEventId = eventId;
        this.mContent = content;
        this.mFileCache = fileCache;
        this.mOriginal = original;
        this.mThumb = thumb;
    }

    public String getCommentableType() {
        return mCommentableType;
    }

    public int getEventId() {
        return mEventId;
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
