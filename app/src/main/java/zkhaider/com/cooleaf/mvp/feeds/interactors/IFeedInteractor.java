package zkhaider.com.cooleaf.mvp.feeds.interactors;

import java.util.List;

import zkhaider.com.cooleaf.cooleafapi.entities.Post;

/**
 * Created by ZkHaider on 8/23/15.
 */
public interface IFeedInteractor {

    void loadedFeeds(List<Post> posts);
    void addPost(Post post);
    void onDeletedFeed();

}
