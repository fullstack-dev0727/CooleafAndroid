package zkhaider.com.cooleaf.mvp.comments.events;

import zkhaider.com.cooleaf.cooleafapi.entities.Post;

/**
 * Created by ZkHaider on 10/7/15.
 */
public class RemovedEventCommentEvent {

    private Post mComment;

    public RemovedEventCommentEvent(Post comment) {
        this.mComment = comment;
    }

    public Post getComment() {
        return mComment;
    }

}
