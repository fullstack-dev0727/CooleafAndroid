package zkhaider.com.cooleaf.mvp.search.presenters;

import android.util.Log;

import com.squareup.otto.Subscribe;

import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.mvp.search.events.GetEventFromSearch;
import zkhaider.com.cooleaf.mvp.search.events.GetFeedFromSearch;
import zkhaider.com.cooleaf.mvp.search.events.GetInterstFromSearch;
import zkhaider.com.cooleaf.mvp.search.events.GotEventFromSearch;
import zkhaider.com.cooleaf.mvp.search.events.GotFeedFromSearch;
import zkhaider.com.cooleaf.mvp.search.events.GotInterestFromSearch;
import zkhaider.com.cooleaf.mvp.search.events.LoadSearchEvent;
import zkhaider.com.cooleaf.mvp.search.events.LoadedSearchEvent;
import zkhaider.com.cooleaf.mvp.search.events.SearchErrorEvent;
import zkhaider.com.cooleaf.mvp.search.interactors.ISearchInteractor;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class SearchPresenter {

    private ISearchInteractor mSearchInteractor;

    /*********************************************************************************************
     * Initialization
     *********************************************************************************************/

    public SearchPresenter(ISearchInteractor searchInteractor) {
        this.mSearchInteractor = searchInteractor;
    }

    /*********************************************************************************************
     * BusProvider Methods
     *********************************************************************************************/

    public void registerOnBus() {
        BusProvider.getInstance().register(this);
    }

    public void unregisterOnBus() {
        BusProvider.getInstance().unregister(this);
    }

    /*********************************************************************************************
     * searchQuery
     *********************************************************************************************/

    public void searchQuery(String query, String scope, int page, int perPage) {
        BusProvider.getInstance().post(new LoadSearchEvent(query, scope, page, perPage));
    }

    /*********************************************************************************************
     * Subscription Events
     *********************************************************************************************/

    @Subscribe
    public void onLoadedSearchEvent(LoadedSearchEvent loadedSearchEvent) {
        if (mSearchInteractor != null)
            mSearchInteractor.initSearchResult(loadedSearchEvent.getSearchQueries());
    }

    @Subscribe
    public void onSearchErrorEvent(SearchErrorEvent searchErrorEvent) {
        if (mSearchInteractor != null)
            mSearchInteractor.onSearchError(searchErrorEvent.getMessage());
    }

}
