package zkhaider.com.cooleaf.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchResult;
import zkhaider.com.cooleaf.mvp.events.events.LoadFragmentEventsEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadMeEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadUsersEventEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedMeEvent;
import zkhaider.com.cooleaf.mvp.search.events.LoadedGlobalSearchEvent;
import zkhaider.com.cooleaf.ui.fragments.EventFragment;
import zkhaider.com.cooleaf.ui.listeners.AddCommentListener;
import zkhaider.com.cooleaf.utils.AutoCompleteHelper;

/**
 * Created by Haider on 12/24/2014.
 */
public class ListEventsActivity extends DrawerActivity implements AddCommentListener {

    public static final String TAG = ListEventsActivity.class.getSimpleName();
    private int page = 0;

    private FrameLayout mParentLayout;
    private AutoCompleteTextView mAutoCompleteTextView;

    /********************************************************************************************
     *  Activity LifeCycle methods
     ********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView();
        initActionBar(R.string.eventsTitle);
        initEventFragment();
        mBus = BusProvider.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /********************************************************************************************
     *  Initialization methods
     ********************************************************************************************/

    private void inflateView() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.mainContent);
        mParentLayout = frameLayout;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = inflater.inflate(R.layout.activity_dashboard, null, false);
        frameLayout.addView(activityView);
    }

    private void initEventFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        EventFragment fragment = new EventFragment();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    /********************************************************************************************
     *  AddCommentListener methods
     ********************************************************************************************/

    @Override
    public void onAddComment(int activityId) {
        addNewCommentToEvent(activityId);
    }

    /********************************************************************************************
     *  Subscription methods
     ********************************************************************************************/

    @Subscribe
    public void onLoadEventFragment(LoadFragmentEventsEvent loadFragmentEventsEvent) {
        page = loadFragmentEventsEvent.getPage();
        mBus.post(new LoadMeEvent());
    }

    @Subscribe
    public void onLoadedMeEvent(LoadedMeEvent loadedMeEvent) {
        mBus.post(new LoadUsersEventEvent(loadedMeEvent.getUser().getId(), page));
    }

}
