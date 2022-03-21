package zkhaider.com.cooleaf.mvp.feeds.events;

import java.util.List;

import zkhaider.com.cooleaf.cooleafapi.entities.Post;

/**
 * Created by ZkHaider on 7/7/15.
 */
public class LoadedFeedsEvent {

    private List<Post> mPosts;
    private int mPage;

    public LoadedFeedsEvent(List<Post> posts, int page) {
        this.mPosts = posts;
        this.mPage = page;
    }

    public List<Post> getPosts() {
        return mPosts;
    }

    public int getPage() {
        return mPage;
    }

}
