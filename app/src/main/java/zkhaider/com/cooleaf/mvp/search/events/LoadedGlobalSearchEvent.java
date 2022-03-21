package zkhaider.com.cooleaf.mvp.search.events;

import zkhaider.com.cooleaf.cooleafapi.entities.SearchResult;

/**
 * Created by ZkHaider on 6/9/15.
 */
public class LoadedGlobalSearchEvent {

    private SearchResult mSearchResult;
    private boolean mIsSubmit;
    private String mQuery;

    public LoadedGlobalSearchEvent(SearchResult searchResult, boolean isSubmit, String query) {
        this.mSearchResult = searchResult;
        this.mIsSubmit = isSubmit;
        this.mQuery = query;
    }

    public SearchResult getSearchResult() {
        return mSearchResult;
    }

    public boolean isSubmit() {
        return mIsSubmit;
    }

    public String getQuery() {
        return mQuery;
    }
}
