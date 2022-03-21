package zkhaider.com.cooleaf.mvp.search.subscribers;

import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zkhaider.com.cooleaf.base.BaseSubscriber;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchQuery;
import zkhaider.com.cooleaf.mvp.events.controllers.EventController;
import zkhaider.com.cooleaf.mvp.events.controllers.EventDetailController;
import zkhaider.com.cooleaf.mvp.feeds.controllers.FeedController;
import zkhaider.com.cooleaf.mvp.interests.controllers.InterestController;
import zkhaider.com.cooleaf.mvp.search.events.GetEventFromSearch;
import zkhaider.com.cooleaf.mvp.search.events.GetFeedFromSearch;
import zkhaider.com.cooleaf.mvp.search.events.GetInterstFromSearch;
import zkhaider.com.cooleaf.mvp.search.events.GotEventFromSearch;
import zkhaider.com.cooleaf.mvp.search.events.GotFeedFromSearch;
import zkhaider.com.cooleaf.mvp.search.events.GotInterestFromSearch;
import zkhaider.com.cooleaf.mvp.search.events.LoadGlobalSearchEvent;
import zkhaider.com.cooleaf.cooleafapi.events.LoadedErrorEvent;
import zkhaider.com.cooleaf.mvp.search.events.LoadSearchEvent;
import zkhaider.com.cooleaf.mvp.search.events.LoadedGlobalSearchEvent;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchResult;
import zkhaider.com.cooleaf.mvp.search.controllers.SearchController;
import zkhaider.com.cooleaf.mvp.search.events.LoadedSearchEvent;
import zkhaider.com.cooleaf.mvp.search.events.SearchErrorEvent;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class SearchSubscriber extends BaseSubscriber {

    private SearchController mSearchController = new SearchController();
    private EventDetailController mEventDetailController = new EventDetailController();
    private InterestController mInterestController = new InterestController();
    private FeedController mFeedController = new FeedController();

    @Subscribe
    public void onLoadGlobalSearchEvent(LoadGlobalSearchEvent loadGlobalSearchEvent) {

        final String query = loadGlobalSearchEvent.getQuery();
        final boolean isSubmit = loadGlobalSearchEvent.isSubmit();

        mSearchController.globalSearch(query, 1, 25, new Callback<SearchResult>() {
            @Override
            public void success(SearchResult searchResult, Response response) {
                post(new LoadedGlobalSearchEvent(searchResult, isSubmit, query));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onGetEventFromSearch(GetEventFromSearch getEventFromSearch) {

        int eventId = getEventFromSearch.getEventId();

        mEventDetailController.getEvent(eventId, new Callback<Event>() {
            @Override
            public void success(Event event, Response response) {
                post(new GotEventFromSearch(event));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onGetInterestFromSearch(GetInterstFromSearch getInterstFromSearch) {

        int interestId = getInterstFromSearch.getInterestId();

        mInterestController.getInterest(interestId, new Callback<Interest>() {
            @Override
            public void success(Interest interest, Response response) {
                post(new GotInterestFromSearch(interest));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }


    @Subscribe
    public void onGetFeedFromSearch(GetFeedFromSearch getFeedFromSearch) {

        int feedId = getFeedFromSearch.getFeedId();

        mFeedController.getFeed(feedId, new Callback<Post>() {
            @Override
            public void success(Post post, Response response) {
                Log.d("SearchSubscriber", "success onGetFeedFromSearch");
                post(new GotFeedFromSearch(post));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

    @Subscribe
    public void onLoadSearchEVent(LoadSearchEvent loadSearchEvent) {

        // Get parameters
        String query = loadSearchEvent.getQuery();
        String scope = loadSearchEvent.getScope();
        int page = loadSearchEvent.getPage();
        int perPage = loadSearchEvent.getPerPage();

        mSearchController.search(query, scope, page, perPage, new Callback<List<SearchQuery>>() {
            @Override
            public void success(List<SearchQuery> searchQueries, Response response) {
                post(new LoadedSearchEvent(searchQueries));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                post(new LoadedErrorEvent(retrofitError));
            }
        });
    }

}
