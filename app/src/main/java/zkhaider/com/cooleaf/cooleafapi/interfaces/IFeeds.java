package zkhaider.com.cooleaf.cooleafapi.interfaces;

import java.util.List;

import retrofit.Callback;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;

/**
 * Created by ZkHaider on 7/7/15.
 */
public interface IFeeds {

    @GET("/v2/feeds/all.json")
    void getAllFeeds(@Query("page")     int page,
                     @Query("per_page") int perPage,
                     Callback<List<Post>> callback);

    @GET("/v2/feeds/{type}/{id}.json")
    void getSpecificFeed(@Path("type")          String feedableType,
                         @Path("id")            int feedableId,
                         @Query("page")         int page,
                         @Query("per_page")     int perPage,
                         Callback<List<Post>> callback);

    @GET("/v2/feeds/{id}.json")
    void getFeed(@Path("id")    int feedId,
                                Callback<Post> callback);

    @Multipart
    @POST("/v2/feeds/{type}/{id}/post.json")
    void addNewFeed(@Path("type")                           String feedableType,
                    @Path("id")                             int feedableId,
                    @Part("content")                        String content,
                    @Part("attachment[file_cache]")         String fileCache,
                    @Part("attachment[original]")           String original,
                    @Part("attachment[versions][thumb]")    String thumb,
                    Callback<Post> callback);

    @Multipart
    @POST("/v2/feeds/{feed_id}/comments.json")
    void postComment(@Path("feed_id")                       int feedableId,
                     @Part("content")                       String content,
                     @Part("attachment[file_cache]")        String fileCache,
                     @Part("attachment[original]")          String original,
                     @Part("attachment[versions][thumb]")   String thumb,
                     Callback<Post> callback);

    @Multipart
    @PUT("/v2/feeds/comment/{comment_id}.json")
    void updateComment(@Path("comment_id")                  int commentId,
                       @Part("content")                     String content,
                       @Part("attachment[file_cache]")      String fileCache,
                       @Part("attachment[original]")        String original,
                       @Part("attachment[versions][thumb]") String thumb,
                       @Part("remove_attachment")           boolean removeAttachment,
                       Callback<Post> callback);

    @DELETE("/v2/feeds/{feed_id}.json")
    void deleteFeed(@Path("feed_id")    int feedId,
                                        Callback<Void> callback);

    @DELETE("/v2/feeds/comment/{comment_id}.json")
    void deleteComment(@Path("comment_id")  int commentId,
                       Callback<Post> callback);


}
