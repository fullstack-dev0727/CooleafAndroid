package zkhaider.com.cooleaf.mvp.users.events;

import zkhaider.com.cooleaf.cooleafapi.entities.Edit;

/**
 * Created by Haider on 3/3/2015.
 */
public class LoadEditEvent {

    private Edit mEdit;

    public LoadEditEvent(Edit edit) {
        this.mEdit = edit;
    }

    public Edit getEdit() {
        return mEdit;
    }

}
