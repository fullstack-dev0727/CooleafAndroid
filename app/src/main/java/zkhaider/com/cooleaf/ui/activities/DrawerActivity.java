package zkhaider.com.cooleaf.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.mvp.authentication.events.DeauthorizeEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadMeEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedMeEvent;
import zkhaider.com.cooleaf.ui.listeners.RecyclerItemClickListener;
import zkhaider.com.cooleaf.ui.navdrawer.NavDrawerAdapter;
import zkhaider.com.cooleaf.ui.navdrawer.NavMenuItem;
import zkhaider.com.cooleaf.utils.LocalPreferences;

/**
 * Created by Haider on 2/3/2015.
 */
public class DrawerActivity extends PostActivity implements RecyclerItemClickListener.OnItemClickListener {

    private static final String TAG = DrawerActivity.class.getSimpleName();
    private Resources resources;
    private List<NavMenuItem> navMenuItems;
    private NavDrawerAdapter mAdapter;
    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout = null;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_frame);

        // Nullify the background
        getWindow().setBackgroundDrawable(null);

        initResources();
        moveDrawerToTop();
        initDrawer();
        initItemTouchListener();
        subscribeToLoadedEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    void subscribeToLoadedEvent() {
        Object busEventListener = new Object() {
            @Subscribe
            public void onReceiveLoginEvent(LoadedMeEvent loadedMeEvent) {
                User user = loadedMeEvent.getUser();
                mUser = user;
                // Get user id and store in LocalPreferences
                LocalPreferences.set("user_id", user.getId().intValue());

                // Save user object in localpreferences
                Gson gson = new Gson();
                LocalPreferences.set("user", gson.toJson(mUser));
                setAdapter(user);
            }
        };

        mBus.register(busEventListener);
    }

    private void initResources() {
        resources = getResources();
        String[] navDrawerTitles = resources.getStringArray(R.array.navDrawerTitles);
        TypedArray navDrawerIcons = resources.obtainTypedArray(R.array.navDrawerIcons);

        int size = navDrawerIcons.length();
        navMenuItems = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            navMenuItems.add(new NavMenuItem(navDrawerTitles[i], navDrawerIcons.getResourceId(i, -1)));
        }
        navDrawerIcons.recycle();
    }

    private void moveDrawerToTop() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.activity_drawer, null); // Null is important

        // HACK: "steal" the first child of activity_drawer
        ViewGroup mActivityDrawerLayout = (ViewGroup) getWindow().getDecorView();
        View child = mActivityDrawerLayout.getChildAt(0); // Grabs first child in activity_drawer.xml
        mActivityDrawerLayout.removeView(child);
        LinearLayout container = (LinearLayout) drawer.findViewById(R.id.navDrawerContent);
        container.addView(child, 0);
        drawer.findViewById(R.id.navRecyclerView).setPadding(0, getStatusBarHeight(), 0, 0);

        // Make navbar replace the first child
        mActivityDrawerLayout.addView(drawer);
    }

    protected void initActionBar(int title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        if (Build.VERSION.SDK_INT >= 21)
            actionBar.setElevation(12);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        recyclerView = (RecyclerView) findViewById(R.id.navRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new NavDrawerAdapter());

        mDrawerLayout.setDrawerListener(createDrawerToggle());
        mActionBarDrawerToggle.syncState();

        mBus.post(new LoadMeEvent());
    }

    private void initItemTouchListener() {
        final GestureDetector mGestureDetector = new GestureDetector(DrawerActivity.this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mGestureDetector.onTouchEvent(e)) {
                    mDrawerLayout.closeDrawers();
                    int position = recyclerView.getChildAdapterPosition(child);
                    switch (position) {
                        case 0:
                            startProfileActivity();
                            break;
                        case 1:
                            startDashboardActivity();
                            break;
                        case 2:
                            startListEventsActivity();
                            break;
                        case 3:
                            startListGroupsActivity();
                            break;
                        case 4:
                            startPeopleActivity();
                            break;
                        case 5:
                            startProfileActivity();
                            break;
                    }

                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }

        });
    }

    private void startProfileActivity() {
        User user = mAdapter.getUser();
        Intent profileIntent = new Intent(this, MyProfileActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putString("user", gson.toJson(user));
        profileIntent.putExtras(bundle);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(profileIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void startListEventsActivity() {
        Intent listEventsIntent = new Intent(this, ListEventsActivity.class);
        listEventsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        listEventsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        listEventsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(listEventsIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void startDashboardActivity() {
        Intent dashboardIntent = new Intent(this, DashboardActivity.class);
        dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(dashboardIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void startListGroupsActivity() {
        Intent groupsIntent = new Intent(this, ListGroupsActivity.class);
        groupsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        groupsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        groupsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(groupsIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void startPeopleActivity() {
        Intent peopleIntent = new Intent(this, ListPeopleActivity.class);
        peopleIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        peopleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        peopleIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(peopleIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private DrawerLayout.DrawerListener createDrawerToggle() {
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
                // Code here executes what will happen after drawer is opened
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                syncState();
                // Code here executes what will happen after drawer is closed
            }
        }; // Drawer toggle object is implemented
        return mActionBarDrawerToggle;
    }

    @Override
    public void onItemClick(View view, int position) {
        mDrawerLayout.closeDrawer(recyclerView);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
                    if (!mDrawerLayout.isDrawerOpen(recyclerView)) {
                        mDrawerLayout.openDrawer(recyclerView);
                    } else if (mDrawerLayout.isDrawerOpen(recyclerView)) {
                        mDrawerLayout.closeDrawer(recyclerView);
                    }
                }
                break;
            case R.id.action_search:
                goToSearchActivity();
                break;
            case R.id.action_logout:
                goToUserLogin();
                mBus.post(new DeauthorizeEvent());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setAdapter(User user) {
        mAdapter = new NavDrawerAdapter(navMenuItems,
                user);
        recyclerView.setAdapter(mAdapter);
    }

    public User getUser() {
        return mUser;
    }

    private void goToSearchActivity() {
        Intent i = new Intent(DrawerActivity.this, SearchActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

}
