package zkhaider.com.cooleaf.mvp.search.events;

import java.util.List;

import zkhaider.com.cooleaf.cooleafapi.entities.SearchQuery;

/**
 * Created by ZkHaider on 10/4/15.
 */
public class LoadedSearchEvent {

    private List<SearchQuery> mSearchQueries;

    public LoadedSearchEvent(List<SearchQuery> searchQueries) {
        this.mSearchQueries = searchQueries;
    }

    public List<SearchQuery> getSearchQueries() {
        return mSearchQueries;
    }

}
