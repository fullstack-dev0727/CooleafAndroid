package zkhaider.com.cooleaf.mvp.feeds.interactors;

import java.util.List;

import zkhaider.com.cooleaf.cooleafapi.entities.Post;

/**
 * Created by ZkHaider on 10/6/15.
 */
public interface IFeedDetailInteractor {

    void initFeed(Post feed);
    void addedComment(Post comment);
    void updatedComment(Post comment);
    void deletedComment(Post comment);

}
