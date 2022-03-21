package zkhaider.com.cooleaf.mvp.comments.controllers;

import java.util.List;

import retrofit.Callback;
import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.cooleafapi.interfaces.IComments;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class CommentController {

    public void getComments(String commentableType, int commentableId, int page, int perPage,
                            Callback<List<Post>> callback) {
        IComments comments = CooleafAPIClient.getAsynsRestAdapter().create(IComments.class);
        comments.getComments(commentableType, commentableId, page, perPage, callback);
    }

    public void postComment(String commentableType, int commentableId, String content,
                            String fileCache, String original, String thumb, Callback<Post> callback) {
        IComments comments = CooleafAPIClient.getAsynsRestAdapter().create(IComments.class);
        comments.postComment(commentableType, commentableId, content, fileCache, original, thumb, callback);
    }

    public void updateComment(String commentableType, int commentableId, int commentId, String content,
                              String fileCache, String original, String thumb, boolean removeAttachment,
                              Callback<Post> callback) {
        IComments comments = CooleafAPIClient.getAsynsRestAdapter().create(IComments.class);
        comments.updateComment(commentableType, commentableId, commentId, content,
                fileCache, original, thumb, removeAttachment, callback);
    }

    public void deleteComment(String commentableType, int commentableId, int commentId, Callback<Post> callback) {
        IComments comments = CooleafAPIClient.getAsynsRestAdapter().create(IComments.class);
        comments.deleteComment(commentableType, commentableId, commentId, callback);
    }

}
