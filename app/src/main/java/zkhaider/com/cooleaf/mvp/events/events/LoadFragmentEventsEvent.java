package zkhaider.com.cooleaf.mvp.events.events;

/**
 * Created by kcoleman on 3/9/15.
 */
public class LoadFragmentEventsEvent {

    private int mPage;
    private int mPerPage;

    public LoadFragmentEventsEvent(int page) {
        mPage = page;
    }

    public LoadFragmentEventsEvent(int page, int perPage) {
        this.mPage = page;
        this.mPerPage = perPage;
    }

    public int getPage()
    {
        return mPage;
    }

    public int getPerPage() {
        return mPerPage;
    }
}
