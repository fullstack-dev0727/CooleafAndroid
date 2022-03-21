package zkhaider.com.cooleaf.mvp.search.events;

/**
 * Created by ZkHaider on 6/7/15.
 */
public class LoadGlobalSearchEvent {

    private String mQuery;
    private boolean mIsSubmit;

    public LoadGlobalSearchEvent(String query, boolean isSubmit) {
        this.mQuery = query;
        this.mIsSubmit = isSubmit;
    }

    public String getQuery() {
        return mQuery;
    }

    public boolean isSubmit() {
        return mIsSubmit;
    }
}
