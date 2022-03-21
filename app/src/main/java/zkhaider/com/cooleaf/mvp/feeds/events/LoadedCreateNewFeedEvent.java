package zkhaider.com.cooleaf.mvp.feeds.events;

import zkhaider.com.cooleaf.cooleafapi.entities.Post;

/**
 * Created by ZkHaider on 7/12/15.
 */
public class LoadedCreateNewFeedEvent {

    private Post mPost;

    public LoadedCreateNewFeedEvent(Post post) {
        this.mPost = post;
    }

    public Post getPost() {
        return mPost;
    }

}
