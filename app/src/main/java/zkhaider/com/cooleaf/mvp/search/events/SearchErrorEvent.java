package zkhaider.com.cooleaf.mvp.search.events;

/**
 * Created by ZkHaider on 10/5/15.
 */
public class SearchErrorEvent {

    private String mMessage;

    public SearchErrorEvent(String message) {
        this.mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

}
