package zkhaider.com.cooleaf.ui.events;

/**
 * Created by kcoleman on 2/17/15.
 */
public class LoadPeopleEvent {

    private int mPage;
    private int mPerPage;

    public LoadPeopleEvent() {
        this.mPage = 1;
    }

    public LoadPeopleEvent(int page) {
        this.mPage = page;
    }

    public LoadPeopleEvent(int page, int perPage) {
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
