package zkhaider.com.cooleaf.mvp.comments.events;

/**
 * Created by ZkHaider on 7/12/15.
 */
public class RemoveCommentEvent {

    private int mCommentId;

    public RemoveCommentEvent(int commentId) {
        this.mCommentId = commentId;
    }

    public int getCommentId() {
        return mCommentId;
    }

}
