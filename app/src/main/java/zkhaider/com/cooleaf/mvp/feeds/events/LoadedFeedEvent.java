package zkhaider.com.cooleaf.mvp.feeds.events;

import zkhaider.com.cooleaf.cooleafapi.entities.Post;

/**
 * Created by ZkHaider on 10/6/15.
 */
public class LoadedFeedEvent {

    private Post mFeed;

    public LoadedFeedEvent(Post feed) {
        this.mFeed = feed;
    }

    public Post getFeed() {
        return mFeed;
    }

}
