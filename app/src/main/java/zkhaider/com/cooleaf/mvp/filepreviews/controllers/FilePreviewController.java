package zkhaider.com.cooleaf.mvp.filepreviews.controllers;

import retrofit.Callback;
import retrofit.mime.TypedFile;
import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.cooleafapi.interfaces.IFilePreview;
import zkhaider.com.cooleaf.cooleafapi.entities.FilePreview;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class FilePreviewController {

    public void uploadPhoto(TypedFile photo, String uploader, Callback<FilePreview> callback) {
        IFilePreview uploadPhotoInterface = CooleafAPIClient.getAsynsRestAdapter().create(IFilePreview.class);
        uploadPhotoInterface.uploadProfilePhoto(photo, uploader, callback);
    }

}
