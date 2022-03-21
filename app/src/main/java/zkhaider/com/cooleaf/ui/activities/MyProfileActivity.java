package zkhaider.com.cooleaf.ui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.mvp.authentication.events.DeauthorizeEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadFragmentEventsEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadUsersEventEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadSpecificUserEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedSpecificUserEvent;
import zkhaider.com.cooleaf.ui.adapters.ProfileViewPagerAdapter;
import zkhaider.com.cooleaf.ui.listeners.AddCommentListener;
import zkhaider.com.cooleaf.ui.picassotransformations.AltBlurTransformation;
import zkhaider.com.cooleaf.ui.picassotransformations.wasabeef.BrightnessFilterTransformation;
import zkhaider.com.cooleaf.ui.roundedpicture.CircleTransform;

/**
 * Created by Haider on 2/17/2015.
 */
public class MyProfileActivity extends PostActivity implements AddCommentListener {

    public static final String TAG = MyProfileActivity.class.getSimpleName();
    private static final String PAST = "past";

    // Views
    private AppBarLayout mAppBarLayout;
    private ImageView mBackgroundImage;
    private ImageView mProfilePicture;
    private TextView  mProfileUserName;
    private TextView  mProfileUserRewards;
    private ViewPager mViewPager;
    private TabLayout mProfileTabLayout;
    private Resources mResources;

    private User mUser;

    /*********************************************************************************************
     *  Activity LifeCycle Methods
     *********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_profile);
        mResources = getResources();
        initToolBar();
        initViews();
        initBundle();
    }

    @Override
    public void onResume() {
        super.onResume();
        initBundle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_logout:
                goToUserLogin();
                mBus.post(new DeauthorizeEvent());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onAddComment(int activityId) {
        addNewCommentToEvent(activityId);
    }

    /*********************************************************************************************
     *  Initialization Methods
     *********************************************************************************************/

    private void initToolBar() {
        Toolbar profileToolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(profileToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setElevation(0);
            setTitle(null);
        }
    }

    private void initViews() {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayoutProfile);
        mBackgroundImage = (ImageView) findViewById(R.id.backgroundImageProfile);
        mProfilePicture = (ImageView) findViewById(R.id.userProfilePicture);
        mProfileUserName = (TextView) findViewById(R.id.profileUserName);
        mProfileUserRewards = (TextView) findViewById(R.id.profileUserRewards);
        mProfileTabLayout = (TabLayout) findViewById(R.id.profileTabs);
        mViewPager = (ViewPager) findViewById(R.id.profilePager);
    }

    private void initBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("user")) {
            String userJSON = bundle.getString("user");
            initJSON(bundle, userJSON);
        } else {
            initJSON(bundle, null);
        }

    }

    private void initJSON(Bundle bundle, String userJSON) {
        if (userJSON != null) {
            Gson gson = new Gson();
            mUser = gson.fromJson(userJSON, User.class);
            initViewPager();
            showFAB();
        } else {
            int userId = bundle.getInt("user_id");
            mBus.post(new LoadSpecificUserEvent(userId));
        }
    }

    private void initViewPager() {
        ProfileViewPagerAdapter profileViewPagerAdapter = new ProfileViewPagerAdapter(this, getSupportFragmentManager());
        profileViewPagerAdapter.setUser(mUser);
        profileViewPagerAdapter.setAppBarLayout(mAppBarLayout);
        mViewPager.setAdapter(profileViewPagerAdapter);
        mProfileTabLayout.setupWithViewPager(mViewPager);
        initUser();
    }

    public void initUser() {
        // Apparently Monterail lists the interests in backwards alphabetical order....
        List<Interest> categories = mUser.getCategories();
        Collections.reverse(categories);

        String imageUrl = mUser.getProfile().getPicture().getVersions().getBigURL();

        Picasso.with(this)
                .load(imageUrl)
                .transform(new AltBlurTransformation(this, 200))
                .transform(new BrightnessFilterTransformation(this, -0.3f))
                .fit()
                .centerCrop()
                .into(mBackgroundImage);

        Picasso.with(this)
                .load(imageUrl)
                .transform(new CircleTransform())
                .fit()
                .centerCrop()
                .into(mProfilePicture);

        mProfileUserName.setText(mUser.getName());


        int showRewards = (mUser.getRewardPoints() != 0) ? View.VISIBLE : View.GONE;
        mProfileUserRewards.setText(String.valueOf(mUser.getRewardPoints()) + " Reward Points");
        mProfileUserRewards.setVisibility(showRewards);
    }

    private void initFabListener(FloatingActionButton floatingActionButton) {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditProfileActivity();
            }
        });
    }

    /*********************************************************************************************
     *  Otto Subscription Methods
     *********************************************************************************************/

    @Subscribe
    public void onLoadFragmentEventsEvent(LoadFragmentEventsEvent loadFragmentEventsEvent) {
        mBus.post(new LoadUsersEventEvent(mUser.getId(), loadFragmentEventsEvent.getPage(), PAST));
    }

    @Subscribe
    public void onLoadedSpecificUserEvent(LoadedSpecificUserEvent loadedSpecificUserEvent) {
        mUser = loadedSpecificUserEvent.getUser();
        initViewPager();
    }

    /*********************************************************************************************
     *  Helper Methods
     *********************************************************************************************/

    private void goToEditProfileActivity() {
        Intent i = new Intent(MyProfileActivity.this, EditProfileActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putString("user", gson.toJson(mUser));
        i.putExtras(bundle);
        startActivity(i);
    }

    private void showFAB() {

        FloatingActionButton editProfileButton = new FloatingActionButton(this);
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editProfileButton.setLayoutParams(layoutParams);
        layoutParams.rightMargin = mResources.getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        ((CoordinatorLayout) findViewById(R.id.profileCoordinatorLayout)).addView(editProfileButton);

        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) editProfileButton.getLayoutParams();
        p.setAnchorId(R.id.appBarLayoutProfile);
        p.anchorGravity = Gravity.BOTTOM | Gravity.END;
        editProfileButton.setLayoutParams(p);
        editProfileButton.setVisibility(View.VISIBLE);
        editProfileButton.setDrawingCacheBackgroundColor(mResources.getColor(R.color.fab_color));
        editProfileButton.setBackgroundTintList(ColorStateList.valueOf(mResources.getColor(R.color.fab_color)));
        editProfileButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_profile_button));

        initFabListener(editProfileButton);
    }

}