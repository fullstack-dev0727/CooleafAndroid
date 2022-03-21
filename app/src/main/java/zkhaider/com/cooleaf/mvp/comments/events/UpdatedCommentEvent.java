package zkhaider.com.cooleaf.mvp.comments.events;

import zkhaider.com.cooleaf.cooleafapi.entities.Post;

/**
 * Created by ZkHaider on 7/13/15.
 */
public class UpdatedCommentEvent {

    private Post mPost;

    public UpdatedCommentEvent(Post updatedPost) {
        this.mPost = updatedPost;
    }

    public Post getUpdatedPost() {
        return mPost;
    }

}
