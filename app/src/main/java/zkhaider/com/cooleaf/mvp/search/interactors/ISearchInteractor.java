package zkhaider.com.cooleaf.mvp.search.interactors;

import java.util.List;

import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchQuery;

/**
 * Created by ZkHaider on 10/4/15.
 */
public interface ISearchInteractor {

    void initSearchResult(List<SearchQuery> results);
    void onSearchError(String message);

}
