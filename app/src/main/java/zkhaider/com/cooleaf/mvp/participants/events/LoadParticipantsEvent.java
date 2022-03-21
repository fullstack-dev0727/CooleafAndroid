package zkhaider.com.cooleaf.mvp.participants.events;

/**
 * Created by Haider on 2/21/2015.
 */
public class LoadParticipantsEvent {

    private int mEventId;
    private int mPage;
    private int mPerPage;

    public LoadParticipantsEvent(int id, int page) {
        this.mEventId = id;
        this.mPage = page;
        this.mPerPage = 25;
    }

    public LoadParticipantsEvent(int id, int page, int perPage) {
        this.mEventId = id;
        this.mPage = page;
        this.mPerPage = perPage;
    }

    public int getId() {
        return mEventId;
    }

    public int getPage() {
        return mPage;
    }

    public int getPerPage() {
        return mPerPage;
    }

}
