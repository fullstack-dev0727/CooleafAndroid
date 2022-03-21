package zkhaider.com.cooleaf.mvp.filepreviews.subscribers;

import com.squareup.otto.Subscribe;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import zkhaider.com.cooleaf.base.BaseSubscriber;
import zkhaider.com.cooleaf.mvp.filepreviews.events.LoadUploadFileEvent;
import zkhaider.com.cooleaf.cooleafapi.events.LoadedErrorEvent;
import zkhaider.com.cooleaf.mvp.filepreviews.events.LoadedUploadFileEvent;
import zkhaider.com.cooleaf.cooleafapi.entities.FilePreview;
import zkhaider.com.cooleaf.cooleafapi.utils.APIConfig;
import zkhaider.com.cooleaf.mvp.filepreviews.controllers.FilePreviewController;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class FilePreviewSubscriber extends BaseSubscriber {

    private FilePreviewController mFilePreviewController = new FilePreviewController();

    @Subscribe
    public void uploadFileEvent(LoadUploadFileEvent loadUploadFileEvent) {
        TypedFile file = loadUploadFileEvent.getTypedFile();
        mFilePreviewController.uploadPhoto(file, APIConfig.getUploaderPath(), new Callback<FilePreview>() {
            @Override
            public void success(FilePreview filePreview, Response response) {
                post(new LoadedUploadFileEvent(filePreview));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

}
