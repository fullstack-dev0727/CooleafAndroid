package zkhaider.com.cooleaf.cooleafapi.interfaces;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;
import zkhaider.com.cooleaf.cooleafapi.entities.FilePreview;

/**
 * Created by ZkHaider on 5/24/15.
 */
public interface IFilePreview {

    @Multipart
    @POST("/v2/file_previews.json")
    void uploadProfilePhoto(@Part("file") TypedFile photo,
                            @Part("uploader") String uploader,
                            Callback<FilePreview> callback);

}
