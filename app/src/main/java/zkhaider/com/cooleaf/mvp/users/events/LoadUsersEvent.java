package zkhaider.com.cooleaf.mvp.users.events;

/**
 * Created by kcoleman on 2/17/15.
 */
public class LoadUsersEvent {
    private int mPage;
    public int getPage()
    {
        return mPage;
    }
    public LoadUsersEvent()
    {
        mPage = 1;
    }
    public LoadUsersEvent(int page)
    {
        mPage = page;
    }
}
