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
import zkhaider.com.cooleaf.cooleafapi.entities.SearchResult;
import zkhaider.com.cooleaf.ui.events.LoadPeopleEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadUsersEvent;
import zkhaider.com.cooleaf.mvp.search.events.LoadedGlobalSearchEvent;
import zkhaider.com.cooleaf.ui.fragments.PeopleFragment;
import zkhaider.com.cooleaf.utils.AutoCompleteHelper;

/**
 * Created by Haider on 12/24/2014.
 */
public class ListPeopleActivity extends DrawerActivity {

    public static final String TAG = ListPeopleActivity.class.getSimpleName();

    private FrameLayout mParentLayout;
    private AutoCompleteTextView mAutoCompleteTextView;
    private static SearchView mSearchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView();
        showPeopleFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void inflateView() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.mainContent);
        mParentLayout = frameLayout;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = inflater.inflate(R.layout.activity_list_people, null, false);
        frameLayout.addView(activityView);
    }

    @Subscribe
    public void onUsersLoaded(LoadPeopleEvent loadPeopleEvent) {
        mBus.post(new LoadUsersEvent(loadPeopleEvent.getPage()));
    }

    private void showPeopleFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        PeopleFragment fragment = new PeopleFragment();
        fragment.setColor(getResources().getColor(R.color.colorPrimary));
        fragment.setBus(mBus);
        fragment.setTitle("People");
        ft.replace(R.id.peopleContainer, fragment);
        ft.commit();
    }

}