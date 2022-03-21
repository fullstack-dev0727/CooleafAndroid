package zkhaider.com.cooleaf.cooleafapi.interfaces;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchQuery;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchResult;

/**
 * Created by ZkHaider on 6/8/15.
 */
public interface ISearch {

    @GET("/v2/search/global.json")
    void globalSearch(@Query("query") String query,
                      @Query("page") int page,
                      @Query("per_page") int per_page, Callback<SearchResult> callback);

    @GET("/v2/search.json")
    void search(@Query("query")     String query,
                @Query("scope")     String scope,
                @Query("page")      int page,
                @Query("per_page")  int perPage,
                                    Callback<List<SearchQuery>> callback);

}
