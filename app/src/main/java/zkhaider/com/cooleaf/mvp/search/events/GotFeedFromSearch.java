package zkhaider.com.cooleaf.mvp.search.events;

import zkhaider.com.cooleaf.cooleafapi.entities.Post;

/**
 * Created by ZkHaider on 10/9/15.
 */
public class GotFeedFromSearch {

    private Post mPost;

    public GotFeedFromSearch(Post post) {
        this.mPost = post;
    }

    public Post getPost() {
        return mPost;
    }

}
