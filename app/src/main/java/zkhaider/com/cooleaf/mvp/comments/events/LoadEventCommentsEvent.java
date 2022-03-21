package zkhaider.com.cooleaf.mvp.comments.events;

/**
 * Created by ZkHaider on 8/14/15.
 */
public class LoadEventCommentsEvent {

    private String mCommentableType;
    private int mEventId;
    private int mPage;
    private int mPerPage;

    public LoadEventCommentsEvent(String commentableType, int eventId, int page, int perPage) {
        this.mCommentableType = commentableType;
        this.mEventId = eventId;
        this.mPage = page;
        this.mPerPage = perPage;
    }

    public String getCommentableType() {
        return mCommentableType;
    }

    public int getEventId() {
        return mEventId;
    }

    public int getPage() {
        return mPage;
    }

    public int getPerPage() {
        return mPerPage;
    }

}
