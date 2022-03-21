package zkhaider.com.cooleaf.mvp.comments.interactors;

import java.util.List;

import zkhaider.com.cooleaf.cooleafapi.entities.Post;

/**
 * Created by ZkHaider on 10/7/15.
 */
public interface ICommentInteractor {

    void initComments(List<Post> comments);
    void addedNewComment(Post post);
    void updatedComment(Post post);
    void deletedComment(Post post);

}
