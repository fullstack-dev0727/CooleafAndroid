package zkhaider.com.cooleaf.ui.fragments;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.mvp.events.events.LoadActivityEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadEventSeries;
import zkhaider.com.cooleaf.mvp.participants.events.LoadJoinActivityEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadedActivityEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadedJoinActivityEvent;
import zkhaider.com.cooleaf.mvp.comments.events.LoadedSpecificEventSeries;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.cooleafapi.entities.EventsToJoinRequest;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.mvp.events.events.LoadFragmentEventsEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadedEventsEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedMeEvent;
import zkhaider.com.cooleaf.ui.activities.EventDetailActivity;
import zkhaider.com.cooleaf.ui.activities.MyProfileActivity;
import zkhaider.com.cooleaf.ui.adapters.CooleafEventAdapter;
import zkhaider.com.cooleaf.ui.helpers.EventHelper;
import zkhaider.com.cooleaf.ui.listeners.AddCommentListener;
import zkhaider.com.cooleaf.ui.listeners.OnItemTouchListener;

/**
 * Created by Haider on 12/24/2014.
 */
public class EventFragment extends Fragment implements OnItemTouchListener,
        AppBarLayout.OnOffsetChangedListener {

    public static final String TAG = EventFragment.class.getSimpleName();

    private final static int REVEAL_DURATION = 400;
    private static final int PER_PAGE = 25;

    private Activity mActivity;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private CooleafEventAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private TextView mJoinTextView;
    private Resources mResources;

    private Event mEvent;
    private Interest mInterest;
    private List<Event> mEvents;
    private User mUser;
    private Bus mBus;
    private boolean mShowHeader = false;
    private boolean mIsInterest = false;
    private int mSeriesId;

    // Pagination Variables
    private int page = 1;
    private boolean loading = false;
    int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;

    /********************************************************************************************
     *  Constructor Helper Methods
     ********************************************************************************************/

    public void setUser(User user) {
        this.mUser = user;
    }

    public void setShowHeader(boolean showHeader) {
        this.mShowHeader = showHeader;
    }

    public Parcelable currentState = null;

    public void setInterest(Interest interest) {
        this.mInterest = interest;
        this.mIsInterest = mInterest != null;
    }

    /********************************************************************************************
     *  Fragment LifeCycle Methods
     ********************************************************************************************/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = inflater.inflate(R.layout.event_fragment, null);
        mResources = getActivity().getResources();
        initBus();
        initViews(root);
        initSwipeRefreshLayout();
        initPaginationListener();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
        refreshContent();
    }

    @Override
    public void onPause() {
        super.onPause();
        currentState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBus.unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Gson gson = new Gson();
            String eventJson = data.getStringExtra("refresh_event");
            if (eventJson != null) {
                Event event = gson.fromJson(eventJson, Event.class);
                refreshEvent(event);
            }
        }
    }

    /********************************************************************************************
     *  Fragment Initialization Methods
     ********************************************************************************************/

    private void initBus() {
        mBus = BusProvider.getInstance();
    }

    private void initViews(View root) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.dashboardSwipeToRefresh);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.dashboardRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new CooleafEventAdapter();
        mAdapter.setShowHeader(mShowHeader);
        mAdapter.setOnItemTouchListener(this);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                refreshContent();
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initPaginationListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                lastVisibleItem = mLayoutManager.findLastCompletelyVisibleItemPosition();

                int pageMultiplier = page * PER_PAGE;

                if (!loading) {
                    if ((pageMultiplier == totalItemCount) && (lastVisibleItem == pageMultiplier - 1)) {
                        loading = true;
                        page += 1;
                        mBus.post(new LoadFragmentEventsEvent(page, PER_PAGE));
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                }
            }
        });
    }

    /********************************************************************************************
     *  Overriden Interface Methods
     ********************************************************************************************/

    @Override
    public void onCardViewTap(View view, int position) {
        int offset = mShowHeader ? 1 : 0;
        if (mShowHeader) {
            if (position == 0)
                goToMyProfile(mAdapter.getUser());
            else
                goToEventDetail(view, mAdapter.getEvent(position - offset));
        } else {
            goToEventDetail(view, mAdapter.getEvent(position));
        }
    }

    @Override
    public void onJoin(TextView view, int position) {

        mJoinTextView = view;

        int offset = mShowHeader ? 1 : 0;
        if (mShowHeader)
            mEvent = mAdapter.getEvent(position - offset);
        else
            mEvent = mAdapter.getEvent(position - offset);

        int size = mEvent.getSeries().getEventIds().size();
        if (size > 1) {
            mSeriesId = mEvent.getSeries().getId();
            mBus.post(new LoadEventSeries(mEvent.getId(), true));
        } else {
            selectOneEventToJoin(mEvent);
        }
    }

    @Override
    public void onAddComment(View view, int position) {
       int offset = mShowHeader ? 1 : 0;
        if (mShowHeader)
            mEvent = mAdapter.getEvent(position - offset);
        else
            mEvent = mAdapter.getEvent(position - offset);

        try {
            ((AddCommentListener) mActivity).onAddComment(mEvent.getId());
        } catch (ClassCastException e) {
            Log.e(TAG, "Error: ", e);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        mSwipeRefreshLayout.setEnabled(i == 0);
    }

    /********************************************************************************************
     *  Otto Subscription Methods
     ********************************************************************************************/

    @Subscribe
    public void onMeLoaded(LoadedMeEvent loadedMeEvent) {
        mUser = loadedMeEvent.getUser();
        mAdapter.setUser(mUser);
        mSwipeRefreshLayout.setRefreshing(false);
        invalidateRecycler();
        if (currentState != null)
            mRecyclerView.getLayoutManager().onRestoreInstanceState(currentState);
    }

    @Subscribe
    public void onLoadedEventsEvent(LoadedEventsEvent loadedEventsEvent) {
        List<Event> events = loadedEventsEvent.getEvents();
        if (page != 1) {
            mAdapter.addEvents(events);
            mEvents.addAll(events);
        } else {
            mAdapter.setEvents(events);
            mEvents = events;
        }

        invalidateRecycler();
        if (currentState != null)
            mRecyclerView.getLayoutManager().onRestoreInstanceState(currentState);
        mSwipeRefreshLayout.setRefreshing(false);
        loading = false;
    }

    @Subscribe
    public void onLoadedSpecificEventSeries(LoadedSpecificEventSeries specificEventSeries) {
        selectEventsToJoin(specificEventSeries.getEvents());
    }

    @Subscribe
    public void onLoadedJoinActivityEvent(LoadedJoinActivityEvent loadedJoinActivityEvent) {
        mBus.post(new LoadActivityEvent(mEvent.getId()));
    }

    @Subscribe
    public void onEventLoaded(LoadedActivityEvent loadedEvent) {
        mEvent = loadedEvent.getEvent();
        mJoinTextView.setText(mEvent.getAttending() ? "Leave" : "Join");
        mAdapter.replaceItem(mEvent);
        showEventConfirmation(mEvent);
    }

    /********************************************************************************************
     *  Helper Methods
     ********************************************************************************************/

    public void replaceEvent(Event freshEvent) {
        mAdapter.replaceItem(freshEvent);
    }

    private void goToMyProfile(User user) {
        Intent i = new Intent(getActivity(), MyProfileActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putString("user", gson.toJson(user));
        i.putExtras(bundle);
        getActivity().startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void goToEventDetail(View view, Event event) {
        Intent i = new Intent(getActivity(), EventDetailActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putString("event", gson.toJson(event));
        //bundle.putInt("user_id", mUser.getId());
        i.putExtras(bundle);

        //Shared elements
        ImageButton eventImage = (ImageButton) view.findViewById(R.id.eventImage);

        if (Build.VERSION.SDK_INT >= 21) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                    eventImage, "hero_event"
            );
            getActivity().startActivity(i, options.toBundle());
        } else {
            startActivityForResult(i, 0);
        }
    }

    public void refreshEvent(Event freshEvent) {
        for (int i = 0; i < mEvents.size(); i++) {
            Event selectedEvent = mEvents.get(i);

            if (selectedEvent.getId() == freshEvent.getId()) {
                mEvents.set(i, freshEvent);
                break;
            }
        }
        mAdapter.setEvents(mEvents);
    }

    private void refreshContent() {
        if (page == 1) {
            page = 1;
            mBus.post(new LoadFragmentEventsEvent(page, PER_PAGE));
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void invalidateRecycler() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.invalidate();
    }

    public CooleafEventAdapter getAdapter() {
        return mAdapter;
    }

    private void selectOneEventToJoin(Event event) {
        List<Integer> eventIds = new ArrayList<>();

        if (!event.getAttending())
            eventIds.add(event.getId());

        mBus.post(new LoadJoinActivityEvent(event.getSeries().getId(), new EventsToJoinRequest(eventIds)));
    }

    private void selectEventsToJoin(List<Event> seriesEvents) {
        final List<Event> events = seriesEvents;
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

        boolean[] checkedPositions = new boolean[events.size()];
        CharSequence[] csEventStartTimes = new CharSequence[events.size()];

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            csEventStartTimes[i] = EventHelper.dateRangeToString(event);
            checkedPositions[i] = event.getAttending();
        }

        adb.setMultiChoiceItems(csEventStartTimes, checkedPositions, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

            }
        });

        adb.setNegativeButton(android.R.string.cancel, null);
        adb.setPositiveButton(R.string.series_join_events, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lv = ((AlertDialog) dialog).getListView();
                SparseBooleanArray checkedItems = lv.getCheckedItemPositions();
                List<Integer> eventsIds = new ArrayList<>(checkedItems.size());

                if (checkedItems != null) {
                    for (int i = 0; i < checkedItems.size(); i++) {
                        if (checkedItems.valueAt(i)) {
                            Event event = events.get(checkedItems.keyAt(i));
                            eventsIds.add(event.getId());
                        }
                    }
                }

                mBus.post(new LoadJoinActivityEvent(mSeriesId, new EventsToJoinRequest(eventsIds)));
            }
        });

        adb.setTitle(getString(R.string.selectEventsFromSeries));
        adb.show();
    }

    private void showEventConfirmation(Event event) {
        if (event.getAttending()) {
            Snackbar snackbar = Snackbar.make(mRecyclerView, mResources.getString(R.string.event_joined), Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            TextView tv = (TextView)
                    view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar.make(mRecyclerView, getString(R.string.left_event), Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            TextView tv = (TextView)
                    view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    public AppBarLayout.OnOffsetChangedListener getOffsetChangedListener() {
        return this;
    }

}
