package zkhaider.com.cooleaf.mvp.filepreviews.presenters;

import com.squareup.otto.Subscribe;

import retrofit.mime.TypedFile;
import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.mvp.filepreviews.events.LoadUploadFileEvent;
import zkhaider.com.cooleaf.mvp.filepreviews.events.LoadedUploadFileEvent;
import zkhaider.com.cooleaf.mvp.filepreviews.interactors.IFilePreviewInteractor;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class FilePreviewPresenter {

    private IFilePreviewInteractor mFilePreviewInteractor;

    /*********************************************************************************************
     * Initialization
     *********************************************************************************************/

    public FilePreviewPresenter(IFilePreviewInteractor interactor) {
        this.mFilePreviewInteractor = interactor;
    }

    /*********************************************************************************************
     * BusProvider Methods
     *********************************************************************************************/

    public void registerOnBus() {
        BusProvider.getInstance().register(this);
    }

    public void unregisterOnBus() {
        BusProvider.getInstance().unregister(this);
    }

    /*********************************************************************************************
     * uploadPhoto
     *********************************************************************************************/

    public void uploadPhoto(TypedFile typedFile) {
        BusProvider.getInstance().post(new LoadUploadFileEvent(typedFile));
    }

    /*********************************************************************************************
     * Subscription Events
     *********************************************************************************************/

    @Subscribe
    public void onLoadedUploadFileEvent(LoadedUploadFileEvent loadedUploadFileEvent) {
        if (mFilePreviewInteractor != null)
            mFilePreviewInteractor.initFilePreviews(loadedUploadFileEvent.getFilePreview());
    }

}
