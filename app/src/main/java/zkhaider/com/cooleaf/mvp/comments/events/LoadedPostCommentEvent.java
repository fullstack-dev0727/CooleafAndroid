package zkhaider.com.cooleaf.mvp.comments.events;

import zkhaider.com.cooleaf.cooleafapi.entities.Post;

/**
 * Created by ZkHaider on 7/10/15.
 */
public class LoadedPostCommentEvent {

    private Post mPost;

    public LoadedPostCommentEvent(Post post) {
        this.mPost = post;
    }

    public Post getPost() {
        return mPost;
    }

}
