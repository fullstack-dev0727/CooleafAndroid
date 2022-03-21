package zkhaider.com.cooleaf.mvp.comments.events;

import zkhaider.com.cooleaf.cooleafapi.entities.Post;

/**
 * Created by ZkHaider on 8/14/15.
 */
public class UpdatedEventCommentEvent {

    private Post mPost;

    public UpdatedEventCommentEvent(Post post) {
        this.mPost = post;
    }

    public Post getPost() {
        return mPost;
    }

}
