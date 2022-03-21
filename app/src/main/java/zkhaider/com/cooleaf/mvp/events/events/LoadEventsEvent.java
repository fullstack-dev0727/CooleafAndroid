package zkhaider.com.cooleaf.mvp.events.events;

/**
 * Created by kcoleman on 3/4/15.
 */
public class LoadEventsEvent {

    private int mPage;
    public int getPage()
    {
        return mPage;
    }
    public LoadEventsEvent(int page)
    {
        mPage = page;
    }
}
