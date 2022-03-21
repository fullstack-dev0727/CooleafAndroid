package zkhaider.com.cooleaf.ui.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.mvp.interests.events.LoadInterestsEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadMeEvent;
import zkhaider.com.cooleaf.mvp.interests.events.LoadedInterestsEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedMeEvent;
import zkhaider.com.cooleaf.ui.adapters.CooleafInterestsAdapter;

/**
 * Created by Haider on 12/24/2014.
 */
public class ListGroupsActivity extends DrawerActivity {

    public static final String TAG = ListEventsActivity.class.getSimpleName();

    private FrameLayout frameLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private CooleafInterestsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Interest> mInterests;
    private User mUser = new User();

    private FrameLayout mParentLayout;
    private AutoCompleteTextView mAutoCompleteTextView;
    private static SearchView mSearchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(R.string.groupsTitle);

        frameLayout = (FrameLayout) findViewById(R.id.mainContent);
        mParentLayout = frameLayout;

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = inflater.inflate(R.layout.activity_list_groups, null, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) activityView.findViewById(R.id.interestsSwipeToRefresh);
        mRecyclerView = (RecyclerView) activityView.findViewById(R.id.interestsRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new CooleafInterestsAdapter();

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        frameLayout.addView(activityView);


        initItemTouchListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityForResult called");
        if (resultCode == RESULT_OK) {
            Gson gson = new Gson();
            String interestJson = data.getStringExtra("refresh_interest");
            Log.d(TAG, interestJson);
            Interest interest = gson.fromJson(interestJson, Interest.class);
            refreshInterest(interest);
        }
    }

    private void refreshInterest(Interest freshInterest) {
        for (int i = 0; i < mInterests.size(); i++) {
            Interest selectedInterest = mInterests.get(i);

            if (selectedInterest.getId() == freshInterest.getId()) {
                mInterests.set(i, freshInterest);
            }

            setInterestsInAdapter(mInterests);
        }
    }

    @Subscribe
    public void onMeLoaded(LoadedMeEvent loadedMeEvent) {
        mUser = loadedMeEvent.getUser();
        mBus.post(new LoadInterestsEvent());
        setUserInAdapter(mUser);
    }

    @Subscribe
    public void onLoadedGroupsEvent(LoadedInterestsEvent loadedInterestsEvent) {
        mInterests = loadedInterestsEvent.getInterests();
        setInterestsInAdapter(mInterests);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void refreshContent() {
        mBus.post(new LoadMeEvent());
    }

    private void setUserInAdapter(User user) {
        // Set userId
        mAdapter.setUser(user);
    }

    private void setInterestsInAdapter(List<Interest> interests) {
        mAdapter.setInterests(interests);
        invalidateRecycler();
    }

    private void invalidateRecycler() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.invalidate();
    }

    private void initItemTouchListener() {
        final GestureDetector mGestureDetector = new GestureDetector(ListGroupsActivity.this,
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());

                if (child != null && mGestureDetector.onTouchEvent(e)) {

                    int position = rv.getChildPosition(child);

                    Log.d(TAG, String.valueOf(position));

                    if (position != 0) {
                        Interest interest = mAdapter.getInterest(position - 1);

                        int color = mAdapter.getVibrantColor(position);
                        int colorDark = mAdapter.getDarkVibrantColor(position);

                        Intent i = new Intent(ListGroupsActivity.this, GroupDetailActivity.class);
                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        bundle.putInt("color", color);
                        bundle.putInt("dark_color", colorDark);
                        bundle.putString("interest", gson.toJson(interest));
                        bundle.putString("user", gson.toJson(mUser));
                        i.putExtras(bundle);

                        ImageButton eventImage = (ImageButton) child.findViewById(R.id.interestImage);

                        if (Build.VERSION.SDK_INT >= 21) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ListGroupsActivity.this,
                                    eventImage, "hero_event"
                            );
                            startActivityForResult(i, RESULT_OK, options.toBundle());
                        } else {
                            startActivityForResult(i, RESULT_OK);
                        }
                        return true;
                    } else {
                        User user = mAdapter.getUser();

                        Intent i = new Intent(ListGroupsActivity.this, MyProfileActivity.class);
                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        bundle.putString("user", gson.toJson(user));
                        i.putExtras(bundle);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                        return true;
                    }
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

}
