package zkhaider.com.cooleaf.mvp.comments.events;

/**
 * Created by ZkHaider on 7/13/15.
 */
public class UpdateCommentEvent {

    private int mCommentId;
    private String mContent;
    private String mFileCache;
    private String mOriginal;
    private String mThumb;
    private boolean mRemoveAttachment;

    public UpdateCommentEvent(int commentId, String content, String fileCache, String original,
                              String thumb, boolean removeAttachment) {
        this.mCommentId = commentId;
        this.mContent = content;
        this.mFileCache = fileCache;
        this.mOriginal = original;
        this.mThumb = thumb;
        this.mRemoveAttachment = removeAttachment;
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
