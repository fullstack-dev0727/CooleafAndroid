package zkhaider.com.cooleaf.mvp.comments.events;

/**
 * Created by ZkHaider on 8/14/15.
 */
public class UpdateEventCommentEvent {

    private String mCommentableType;
    private int mCommentableId;
    private int mCommentId;
    private String mContent;
    private String mFileCache;
    private String mOriginal;
    private String mThumb;
    private boolean mRemoveAttachment;

    public UpdateEventCommentEvent(String commentableType, int commentableId, int commentId, String content,
                                   String fileCache, String original, String thumb,
                                   boolean removeAttachment) {
        this.mCommentableType = commentableType;
        this.mCommentableId = commentableId;
        this.mCommentId = commentId;
        this.mContent = content;
        this.mFileCache = fileCache;
        this.mOriginal = original;
        this.mThumb = thumb;
        this.mRemoveAttachment = removeAttachment;
    }

    public String getCommentableType() {
        return mCommentableType;
    }

    public int getCommentableId() {
        return mCommentableId;
    }

    public int getCommentId() {
        return mCommentId;
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

    public boolean removeAttachment() {
        return mRemoveAttachment;
    }

}
