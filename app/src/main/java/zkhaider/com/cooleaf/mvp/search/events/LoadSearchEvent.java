package zkhaider.com.cooleaf.mvp.search.events;

/**
 * Created by ZkHaider on 10/4/15.
 */
public class LoadSearchEvent {

    private String mQuery;
    private String mScope;
    private int mPage;
    private int mPerPage;

    public LoadSearchEvent(String query, String scope, int page, int perPage) {
        this.mQuery = query;
        this.mScope = scope;
        this.mPage = page;
        this.mPerPage = perPage;
    }

    public String getQuery() {
        return mQuery;
    }

    public String getScope() {
        return mScope;
    }

    public int getPage() {
        return mPage;
    }

    public int getPerPage() {
        return mPerPage;
    }

}
