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
 * Created by ZkHaider on 8/14/15.
 */
public interface IComments {

    @GET("/v2/comments/{commentable_type}/{commentable_id}.json")
    void getComments(@Path("commentable_type")              String commentableType,
                     @Path("commentable_id")                int commentableId,
                     @Query("page")                         int page,
                     @Query("per_page")                     int perPage,
                     Callback<List<Post>> callback);

    @Multipart
    @POST("/v2/comments/{commentable_type}/{commentable_id}.json")
    void postComment(@Path("commentable_type")              String commentableType,
                     @Path("commentable_id")                int commentableId,
                     @Part("content")                       String content,
                     @Part("attachment[file_cache]")        String fileCache,
                     @Part("attachment[original]")          String original,
                     @Part("attachment[versions][thumb]")   String thumb,
                     Callback<Post> callback);

    @Multipart
    @PUT("/v2/comments/{commentable_type}/{commentable_id}/{id}.json")
    void updateComment(@Path("commentable_type")            String commentableType,
                       @Path("commentable_id")              int commentableId,
                       @Path("id")                          int id,
                       @Part("content")                     String content,
                       @Part("attachment[file_cache]")      String fileCache,
                       @Part("attachment[original]")        String original,
                       @Part("attachment[versions][thumb]") String thumb,
                       @Part("remove_attachment")           boolean removeAttachment,
                       Callback<Post> callback);

    @DELETE("/v2/comments/{commentable_type}/{commentable_id}/{id}.json")
    void deleteComment(@Path("commentable_type")            String commentableType,
                       @Path("commentable_id")              int commentableId,
                       @Path("id")                          int id,
                       Callback<Post> callback);

}
