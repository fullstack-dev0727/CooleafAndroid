package zkhaider.com.cooleaf.ui.events;

import zkhaider.com.cooleaf.cooleafapi.entities.ParentTag;

/**
 * Created by Haider on 3/6/2015.
 */
public class LoadParentTagEvent {

    private ParentTag mParentTag;

    public LoadParentTagEvent(ParentTag parentTag) {
        this.mParentTag = parentTag;
    }

    public ParentTag getParentId() {
        return mParentTag;
    }

}
