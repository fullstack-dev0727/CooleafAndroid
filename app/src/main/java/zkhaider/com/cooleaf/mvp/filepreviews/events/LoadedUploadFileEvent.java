package zkhaider.com.cooleaf.mvp.filepreviews.events;

import zkhaider.com.cooleaf.cooleafapi.entities.FilePreview;

/**
 * Created by Haider on 3/7/2015.
 */
public class LoadedUploadFileEvent {

    private FilePreview mFilePreview;

    public LoadedUploadFileEvent(FilePreview filePreview) {
        this.mFilePreview = filePreview;
    }

    public FilePreview getFilePreview() {
        return mFilePreview;
    }
}
