package zkhaider.com.cooleaf.mvp.events.events;

/**
 * Created by Haider on 2/12/2015.
 */
public class LoadActivityEvent {

    private int mId;

    public LoadActivityEvent(int id) {
        this.mId = id;
    }

    public int getId() {
        return mId;
    }
}
