package zkhaider.com.cooleaf.mvp.filepreviews.events;

import retrofit.mime.TypedFile;

/**
 * Created by Haider on 2/25/2015.
 */
public class LoadUploadFileEvent {

    private TypedFile photo;

    public LoadUploadFileEvent(TypedFile photo) {
        this.photo = photo;
    }

    public TypedFile getTypedFile() {
        return photo;

    }

}
