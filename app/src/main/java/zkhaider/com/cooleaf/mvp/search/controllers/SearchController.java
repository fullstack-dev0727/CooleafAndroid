package zkhaider.com.cooleaf.mvp.search.controllers;

import java.util.List;

import retrofit.Callback;
import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchQuery;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchResult;
import zkhaider.com.cooleaf.cooleafapi.interfaces.ISearch;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class SearchController {

    public void globalSearch(String query, int page, int per_page, Callback<SearchResult> callback) {
        ISearch search = CooleafAPIClient.getAsynsRestAdapter().create(ISearch.class);
        search.globalSearch(query, page, per_page, callback);
    }

    public void search(String query, String scope, int page, int perPage, Callback<List<SearchQuery>> callback) {
        ISearch search = CooleafAPIClient.getAsynsRestAdapter().create(ISearch.class);
        search.search(query, scope, page, perPage, callback);
    }

}
