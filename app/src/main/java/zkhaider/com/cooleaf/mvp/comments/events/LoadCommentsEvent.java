package zkhaider.com.cooleaf.mvp.comments.events;

/**
 * Created by ZkHaider on 7/8/15.
 */
public class LoadCommentsEvent {

    private int mPosition;

    public LoadCommentsEvent(int position) {
        this.mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }
}
