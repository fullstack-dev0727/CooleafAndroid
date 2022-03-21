package zkhaider.com.cooleaf.mvp.comments.events;

/**
 * Created by ZkHaider on 10/7/15.
 */
public class RemoveEventCommentEvent {

    private String mCommentableType;
    private int mCommentableId;
    private int mCommentId;

    public RemoveEventCommentEvent(String commentableType, int commentableId, int commentId) {
        this.mCommentableType = commentableType;
        this.mCommentableId = commentableId;
        this.mCommentId = commentId;
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

}
