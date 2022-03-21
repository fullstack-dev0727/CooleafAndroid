package zkhaider.com.cooleaf.ui.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Edit;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.Picture;
import zkhaider.com.cooleaf.cooleafapi.entities.Profile;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.mvp.events.events.LoadFragmentEventsEvent;
import zkhaider.com.cooleaf.mvp.feeds.events.LoadedCreateNewFeedEvent;
import zkhaider.com.cooleaf.mvp.filepreviews.events.LoadedUploadFileEvent;
import zkhaider.com.cooleaf.mvp.interests.events.LoadInterestEvent;
import zkhaider.com.cooleaf.mvp.interests.events.LoadInterestEventsEvent;
import zkhaider.com.cooleaf.mvp.interests.events.LoadInterestUsersEvent;
import zkhaider.com.cooleaf.mvp.interests.events.LoadedInterestEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadEditEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedMeEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedUsersEvent;
import zkhaider.com.cooleaf.ui.adapters.InterestViewPagerAdapter;
import zkhaider.com.cooleaf.ui.events.LoadPeopleEvent;
import zkhaider.com.cooleaf.ui.fragments.PeopleFragment;
import zkhaider.com.cooleaf.ui.helpers.PaletteCache;
import zkhaider.com.cooleaf.ui.listeners.AddCommentListener;
import zkhaider.com.cooleaf.ui.roundedpicture.CircleTransform;

/**
 * Created by Haider on 2/11/2015.
 */
public class GroupDetailActivity extends PostActivity implements AddCommentListener {

    public static final String TAG = GroupDetailActivity.class.getSimpleName();

    private static final String FEEDABLE_TYPE = "Interest";

    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private ViewPager mViewPager;
    private InterestViewPagerAdapter mViewPagerAdapter;
    private TabLayout mTabLayout;
    private ImageView mInterestImage;
    private TextView mJoinButton;

    private View mProgressOverlay;
    private RelativeLayout mParticipantsBox;

    private Resources mResources;
    private FragmentManager mFragmentManager;

    private int mVibrantColor;
    private int mDarkVibrantColor;

    private ImageView[] mParticipantsProfilePicture;
    private TextView mInterestMembers;
    private Interest mInterest;
    private User mUser = new User();
    private Edit mEdit = new Edit();
    private List<User> mMembers;
    private List<Integer> mCategoryIds = new ArrayList<>();

    /******************************************************************************************
     * LifeCycle Methods
     ******************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_group_detail);

        setFeedableType(FEEDABLE_TYPE);

        mResources = getResources();
        mFragmentManager = getSupportFragmentManager();
        initBundle();
        initStatusBar();
        initToolbar();
        initCollapsingToolbar();
        initViews();
        initViewPager();
        initMembersPictures();
        initParticipantsTextViewListener();
        mBus.post(new LoadInterestUsersEvent(mInterest.getId(), 1)); // Load the first page on Resume
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshInterest();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_logout:
                Intent logoutIntent = new Intent(GroupDetailActivity.this, UserLoginActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (!mFragmentManager.popBackStackImmediate()) {
            Intent resultIntent = getInterestRefreshIntent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
        mFragmentManager.popBackStack();
    }

    /******************************************************************************************
     * Initialization Methods
     ******************************************************************************************/

    private void initBundle() {
        Bundle bundle = getIntent().getExtras();
        String eventJSON = bundle.getString("interest");
        String userJson = bundle.getString("user");
        Gson gson = new Gson();
        mInterest = gson.fromJson(eventJSON, Interest.class);
        setInterest(mInterest);
        mUser = gson.fromJson(userJson, User.class);

        mEdit.setRole(mUser.getRole());
        mEdit.setProfile(mUser.getProfile());

        for (int i = 0; i < mUser.getCategories().size(); i++) {
            mCategoryIds.add(mUser.getCategories().get(i).getId());
        }

        mEdit.setCategoryIds(mCategoryIds);
        mVibrantColor = bundle.getInt("color");
        mDarkVibrantColor = bundle.getInt("dark_color");
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(mDarkVibrantColor);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.collapsingToolbar).setElevation(0);
        }
    }

    private void initCollapsingToolbar() {
        mCollapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        mCollapsingToolbar.setTitle(mInterest.getName());
        mCollapsingToolbar.setContentScrimColor(mVibrantColor);
    }

    private void initViews() {
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.groupDetailContainer);
        mParticipantsBox = (RelativeLayout) findViewById(R.id.participantsBox);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        if (PaletteCache.getInstance().getVibrantColorCache().containsKey(mInterest.getName())) {
            mAppBarLayout.setBackgroundColor(PaletteCache.getInstance().getVibrantColorCache().get(mInterest.getName()));
            mParticipantsBox.setBackgroundColor(PaletteCache.getInstance().getVibrantColorCache().get(mInterest.getName()));
        } else {
            mAppBarLayout.setBackgroundColor(mVibrantColor);
            mParticipantsBox.setBackgroundColor(mVibrantColor);
        }
        mViewPager = (ViewPager) findViewById(R.id.groupsViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.groupTabs);
        mInterestImage = (ImageView) findViewById(R.id.customEventDetailImage);
        mInterestMembers = (TextView) findViewById(R.id.interestMembersTextView);
        mJoinButton = (TextView) findViewById(R.id.joinButton);
        mFloatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.groupFABMenu);
        mFloatingActionsMenu.bringToFront();
        setTitle(null);

        // Progress indicators
        mProgressOverlay = findViewById(R.id.progressOverlay);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    private void initButtonClickListener() {
        // Setup listener for joining unjoining
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int interestId = mInterest.getId();

                if (!mCategoryIds.contains(interestId)) {
                    mCategoryIds.add(interestId);
                    mEdit.setCategoryIds(mCategoryIds);
                    mBus.post(new LoadEditEvent(mEdit));
                    mProgressOverlay.setVisibility(View.VISIBLE);
                    startProgress();
                } else if (mCategoryIds.contains(interestId)) {
                    int index = mCategoryIds.indexOf(interestId);
                    mCategoryIds.remove(index);
                    mEdit.setCategoryIds(mCategoryIds);
                    mBus.post(new LoadEditEvent(mEdit));
                    mProgressOverlay.setVisibility(View.VISIBLE);
                    startProgress();
                }
            }
        });
        mFloatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                showNewFeedDialog();

            }

            @Override
            public void onMenuCollapsed() {

            }
        });

    }

    private void initViewPager() {
        mViewPagerAdapter = new InterestViewPagerAdapter(this, mFragmentManager);
        mViewPagerAdapter.setInterest(mInterest);
        mViewPagerAdapter.setAppBarLayout(mAppBarLayout);
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        if (PaletteCache.getInstance().getVibrantColorCache().get(mInterest.getName()) != null) {
            mTabLayout.setBackgroundColor(PaletteCache.getInstance().getVibrantColorCache().get(mInterest.getName()));
        } else {
            mTabLayout.setBackgroundColor(mVibrantColor);
        }
    }

    private void initMembersPictures() {
        int size = 4;
        mParticipantsProfilePicture = new ImageView[size];
        mParticipantsProfilePicture[0] = (ImageView) findViewById(R.id.participant1);
        mParticipantsProfilePicture[1] = (ImageView) findViewById(R.id.participant2);
        mParticipantsProfilePicture[2] = (ImageView) findViewById(R.id.participant3);
        mParticipantsProfilePicture[3] = (ImageView) findViewById(R.id.participant4);
    }

    private void initParticipantsTextViewListener() {
        // Set participants
        mInterestMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterest.getUserCount() > 0) {
                    showPeopleFragment();
                }
            }
        });
    }

    /******************************************************************************************
     * Overrident Interface Methods
     ******************************************************************************************/

    @Override
    public void onAddComment(int activityId) {
        addNewCommentToEvent(activityId);
    }

    /******************************************************************************************
     * Otto Subscription Methods
     ******************************************************************************************/

    @Subscribe
    public void onLoadedMeEvent(LoadedMeEvent loadedMeEvent) {
        mUser = loadedMeEvent.getUser();
        mCategoryIds = new ArrayList<>();
        for (Interest interest : mUser.getCategories()) {
            mCategoryIds.add(interest.getId());
        }
        mBus.post(new LoadInterestEvent(mInterest.getId()));
    }

    @Subscribe
    public void onLoadedUsersEvent(LoadedUsersEvent loadedUsersEvent) {
        mMembers = loadedUsersEvent.getUsers();
        refreshMembers();
    }

    @Subscribe
    public void onInterestLoaded(LoadedInterestEvent loadedInterest) {
        mInterest = loadedInterest.getInterest();
        mProgressBar.setVisibility(View.GONE);
        mProgressOverlay.setVisibility(View.INVISIBLE);
        refreshInterest();
    }

    @Subscribe
    public void onLoadPeopleEvent(LoadPeopleEvent loadPeopleEvent) {
        mBus.post(new LoadInterestUsersEvent(mInterest.getId(), loadPeopleEvent.getPage()));
    }

    @Subscribe
    public void onLoadFragmentEventsEvent(LoadFragmentEventsEvent loadFragmentEventsEvent) {
        mBus.post(new LoadInterestEventsEvent(mInterest.getId(),
                loadFragmentEventsEvent.getPage(), loadFragmentEventsEvent.getPerPage()));
    }

    @Subscribe
    public void onLoadedUploadProfilePictureEvent(LoadedUploadFileEvent loadedUploadFileEvent) {
        mFilePreview = loadedUploadFileEvent.getFilePreview();
        aspectBitmap();
    }

    @Subscribe
    public void onLoadedAddCommentEvent(LoadedCreateNewFeedEvent loadedCreateNewFeedEvent) {
        showNewCommentConfirmation();
        mViewPagerAdapter.addPost(loadedCreateNewFeedEvent.getPost());
    }

    /******************************************************************************************
     * Helper Methods
     ******************************************************************************************/

    private void refreshInterest() {

        // Set title
        String name = mInterest.getName();
        mCollapsingToolbar.setTitle(name);

        // Set image
        String interestImage = mInterest.getImage().getWideUrl();
        Picasso.with(this)
                .load(interestImage)
                .fit()
                .centerCrop()
                .into(mInterestImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        //if (mVibrantColor == 0 || mDarkVibrantColor == 0)
                        {
                            PaletteCache cache = PaletteCache.getInstance();
                            if (!cache.getVibrantColorCache().containsKey(mInterest.getName())) {

                                Bitmap bitmap = ((BitmapDrawable) mInterestImage.getDrawable()).getBitmap(); // Ew!
                                Palette palette = Palette.from(bitmap).generate();
                                mVibrantColor = palette.getVibrantColor(ContextCompat.getColor(GroupDetailActivity.this, R.color.dark_purple_700));
                                mDarkVibrantColor = palette.getDarkVibrantColor(ContextCompat.getColor(GroupDetailActivity.this, R.color.dark_purple_900));
                                mTabLayout.setBackgroundColor(mVibrantColor);
                                mAppBarLayout.setBackgroundColor(mVibrantColor);
                                mCollapsingToolbar.setContentScrimColor(mVibrantColor);
                                mParticipantsBox.setBackgroundColor(mVibrantColor);
                                setStatusBarColor(mDarkVibrantColor);

                                PaletteCache.setVibrantColor(mInterest.getName(), mVibrantColor);
                                PaletteCache.setDarkVibrantColor(mInterest.getName(), mDarkVibrantColor);

                            }
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });

        // Set participants
        int membersCount = mInterest.getUserCount();
        String membersString = membersCount == 1 ? " Member >" : " Members >";

        mInterestMembers.setText(Integer.toString(membersCount) + membersString);

        if (mCategoryIds.contains(mInterest.getId())) {
            colorToJoin();
        } else {
            colorToUnjoin();
        }

        initButtonClickListener();
    }

    private Intent getInterestRefreshIntent() {
        Intent resultIntent = new Intent();
        Gson gson = new Gson();
        resultIntent.putExtra("refresh_interest", gson.toJson(mInterest));
        setResult(Activity.RESULT_OK, resultIntent);
        return resultIntent;
    }

    private void refreshMembers() {
        // Set participants pictures
        int membersCount = mInterest.getUserCount();
        int count = membersCount > 4 ? 4 : membersCount;
        String membersString = membersCount == 1 ? " Member >" : " Members >";

        mInterestMembers.setText(Integer.toString(membersCount) + membersString);

        for (int i = 0; i < count && mMembers != null; i++) {
            Profile tempProfile = mMembers.get(i).getProfile();
            Picture tempPicture = tempProfile.getPicture();

            String tempUrl = tempPicture.getVersions().getThumbURL();
            mParticipantsProfilePicture[i].setVisibility(View.VISIBLE);

            Picasso.with(this)
                    .load(tempUrl)
                    .transform(new CircleTransform())
                    .fit()
                    .centerCrop()
                    .into(mParticipantsProfilePicture[i]);

            // Set a listener to go to members
            mParticipantsProfilePicture[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPeopleFragment();
                }
            });
        }

        for (int i = count; i < mParticipantsProfilePicture.length; i++) {
            mParticipantsProfilePicture[i].setVisibility(View.GONE);
        }
    }

    private void showPeopleFragment() {

        mCoordinatorLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        PeopleFragment fragment = new PeopleFragment();
        fragment.setBus(mBus);
        fragment.setTitle(getString(R.string.membersTitle));
        fragment.setColor(mVibrantColor);

        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom,
                        R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom)
                .addToBackStack(null)
                .add(R.id.groupContent, fragment)
                .commit();
    }

    private void colorToJoin() {
        Integer colorFrom = getResources().getColor(R.color.colorPrimary);
        Integer colorTo = getResources().getColor(R.color.cooleafWhite);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mJoinButton.setTextColor((Integer) animation.getAnimatedValue());
                mJoinButton.setText(mResources.getString(R.string.leave));
            }
        });

        colorAnimation.start();
    }

    private void colorToUnjoin() {
        Integer colorFrom = getResources().getColor(R.color.cooleafWhite);
        Integer colorTo = getResources().getColor(R.color.colorPrimary);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mJoinButton.setTextColor((Integer) animation.getAnimatedValue());
                mJoinButton.setText(mResources.getString(R.string.join));
            }
        });

        colorAnimation.start();
    }

    private void showNewCommentConfirmation() {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, getString(R.string.new_feed), Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView tv = (TextView)
                view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(color);
        }
    }

}
