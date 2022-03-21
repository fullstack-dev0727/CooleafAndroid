package zkhaider.com.cooleaf.mvp.users.events;

/**
 * Created by Haider on 2/18/2015.
 */
public class LoadSpecificUserEvent {

    private int mId;

    public LoadSpecificUserEvent(int id) {
        this.mId = id;
    }

    public int getId() {
        return mId;
    }

}
