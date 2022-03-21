package zkhaider.com.cooleaf.mvp.events.events;

/**
 * Created by Haider on 2/18/2015.
 */
public class LoadUsersEventEvent {

    private int mUserId;
    private String mScope;
    private int mPage;

    public LoadUsersEventEvent(int userId, int page) {
        this.mUserId = userId;
        mPage = page;
        this.mScope = "ongoing";
    }

    public LoadUsersEventEvent(int userId, int page, String scope) {
        this.mUserId = userId;
        mPage = page;
        this.mScope = scope;
    }

    public int getUserId() {
        return mUserId;
    }

    public int getPage() {
        return mPage;
    }

    public String getScope() {
        return mScope;
    }

}
