package zkhaider.com.cooleaf.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadedCreateNewFeedEvent;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchResult;
import zkhaider.com.cooleaf.mvp.events.events.LoadEventsEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadFragmentEventsEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadMeEvent;
import zkhaider.com.cooleaf.mvp.search.events.LoadedGlobalSearchEvent;
import zkhaider.com.cooleaf.ui.fragments.EventFragment;
import zkhaider.com.cooleaf.ui.listeners.AddCommentListener;
import zkhaider.com.cooleaf.utils.AutoCompleteHelper;
import zkhaider.com.cooleaf.utils.LocalPreferences;

/**
 * Created by Haider on 12/24/2014.
 */
public class DashboardActivity extends DrawerActivity implements AddCommentListener {

    public static final String TAG = DashboardActivity.class.getSimpleName();
    public static final String COMMENTABLE_TYPE = "Event";

    private FrameLayout mParentLayout;
    private EventFragment mEventFragment;

    /********************************************************************************************
     *  Activity LifeCycle methods
     ********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCommentableType(COMMENTABLE_TYPE);

        inflateView();
        initActionBar(R.string.dashboardTitle);
        initEventFragment();

        mBus = BusProvider.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case REFRESH_EVENT:
                // Go ahead and refresh the new event - works now
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    if (bundle.get("refresh_event") != null) {
                        Gson gson = new Gson();
                        String freshEventJSON = bundle.get("refresh_event").toString();
                        Event freshEvent = gson.fromJson(freshEventJSON, Event.class);
                        mEventFragment.replaceEvent(freshEvent);
                    }
                }
                break;
            default:
                // do nothing
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /********************************************************************************************
     *  Initialization methods
     ********************************************************************************************/

    private void initEventFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mEventFragment = new EventFragment();
        mEventFragment.setShowHeader(true);
        ft.replace(R.id.container, mEventFragment);
        ft.commit();
    }

    /********************************************************************************************
     *  Overriden Interface methods
     ********************************************************************************************/

    @Override
    public void onAddComment(int activityId) {
        addNewCommentToEvent(activityId);
    }

    /********************************************************************************************
     *  Otto Subscription methods
     ********************************************************************************************/

    @Subscribe
    public void onLoadEventFragment(LoadFragmentEventsEvent loadFragmentEventsEvent) {
        mBus.post(new LoadEventsEvent(loadFragmentEventsEvent.getPage()));
        mBus.post(new LoadMeEvent());
    }

    @Subscribe
    public void onLoadedAddCommentEvent(LoadedCreateNewFeedEvent loadedCreateNewFeedEvent) {
        showNewCommentConfirmation();
    }

    /********************************************************************************************
     *  Helper methods
     ********************************************************************************************/

    private void inflateView() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.mainContent);
        mParentLayout = frameLayout;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = inflater.inflate(R.layout.activity_dashboard, null, false);
        frameLayout.addView(activityView);
    }

    private void showNewCommentConfirmation() {
        Snackbar snackbar = Snackbar.make(mParentLayout, getString(R.string.new_comment), Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView tv = (TextView)
                view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

}
