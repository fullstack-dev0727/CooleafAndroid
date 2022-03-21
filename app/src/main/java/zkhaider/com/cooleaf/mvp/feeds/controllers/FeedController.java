package zkhaider.com.cooleaf.mvp.feeds.controllers;

import java.util.List;

import retrofit.Callback;
import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.cooleafapi.interfaces.IFeeds;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class FeedController {

    public void getSpecificFeed(String type, int id, int page, int perPage, Callback<List<Post>> callback) {
        IFeeds feeds = CooleafAPIClient.getAsynsRestAdapter().create(IFeeds.class);
        feeds.getSpecificFeed(type, id, page, perPage, callback);
    }

    public void getFeed(int feedId, Callback<Post> callback) {
        IFeeds feeds = CooleafAPIClient.getAsynsRestAdapter().create(IFeeds.class);
        feeds.getFeed(feedId, callback);
    }

    public void addNewFeed(String type, int id, String content, String fileCache, String original,
                           String thumb, Callback<Post> callback) {
        IFeeds feeds = CooleafAPIClient.getAsynsRestAdapter().create(IFeeds.class);
        feeds.addNewFeed(type, id, content, fileCache, original, thumb, callback);
    }

    public void postComment(int feedableId, String content, String fileCache, String original,
                            String thumb, Callback<Post> callback) {
        IFeeds feeds = CooleafAPIClient.getAsynsRestAdapter().create(IFeeds.class);
        feeds.postComment(feedableId, content, fileCache, original, thumb, callback);
    }

    public void updateComment(int commentId, String content, String fileCache, String orignial,
                              String thumb, boolean removeAttachment, Callback<Post> callback) {
        IFeeds feeds = CooleafAPIClient.getAsynsRestAdapter().create(IFeeds.class);
        feeds.updateComment(commentId, content, fileCache, orignial, thumb, removeAttachment, callback);
    }

    public void deleteFeed(int feedId, Callback<Void> callback) {
        IFeeds feeds = CooleafAPIClient.getAsynsRestAdapter().create(IFeeds.class);
        feeds.deleteFeed(feedId, callback);
    }

    public void deleteComment(int commentId, Callback<Post> callback) {
        IFeeds feeds = CooleafAPIClient.getAsynsRestAdapter().create(IFeeds.class);
        feeds.deleteComment(commentId, callback);
    }

}
