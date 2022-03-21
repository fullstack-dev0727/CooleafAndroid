package zkhaider.com.cooleaf.ui.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.gson.Gson;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.cooleafapi.entities.EventsToJoinRequest;
import zkhaider.com.cooleaf.cooleafapi.entities.Participant;
import zkhaider.com.cooleaf.mvp.events.events.LoadActivityEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadEventSeries;
import zkhaider.com.cooleaf.mvp.events.events.LoadedActivityEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadedEventsEvent;
import zkhaider.com.cooleaf.mvp.events.events.LoadedJoinActivityEvent;
import zkhaider.com.cooleaf.mvp.participants.events.LoadJoinActivityEvent;
import zkhaider.com.cooleaf.mvp.participants.events.LoadParticipantsEvent;
import zkhaider.com.cooleaf.ui.events.LoadPeopleEvent;
import zkhaider.com.cooleaf.ui.fragments.AddToCalendarFragment;
import zkhaider.com.cooleaf.ui.fragments.PeopleFragment;
import zkhaider.com.cooleaf.ui.helpers.EventHelper;
import zkhaider.com.cooleaf.ui.rippledrawable.RippleDrawable;
import zkhaider.com.cooleaf.ui.roundedpicture.CircleTransform;
import zkhaider.com.cooleaf.utils.DPToPixel;
import zkhaider.com.cooleaf.utils.LocalPreferences;

/**
 * Created by Haider on 2/11/2015.
 */
public class EventDetailActivity extends FailureActivity implements ObservableScrollViewCallbacks {

    public static final String TAG = EventDetailActivity.class.getSimpleName();

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private static final boolean TOOLBAR_IS_STICKY = true;
    private static final String FEEDABLE_TYPE = "Event";
    private static final int PER_PAGE = 25;

    private Resources mResources;
    private View mToolbar;
    private ImageView mEventImage;
    private View mOverlayView;
    private ObservableScrollView mScrollView;
    private ImageButton mCalendarButton;
    private TextView mEventTitleView;
    private TextView mEventRewards;
    private TextView mEventParticipants;
    private TextView mEventDescription;
    private TextView mEventDate;
    private TextView mEventAddress;
    private FloatingActionButton mFab;
    private RelativeLayout mRelativeLayout;
    private ImageView[] participantsProfilePicture;
    private Button mCommentButton;

    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private float mFlexibleRange;
    private int mToolBarColor;
    private boolean mFabIsShown;

    private Event mEvent;
    private List<Event> mEvents;
    private int mUserId;

    private FragmentManager fm;
    private FragmentTransaction ft;

    /********************************************************************************************
     * Activity LifeCycle Methods
     ********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_event_detail);

        mResources = this.getResources();
        fm = getSupportFragmentManager();

        initBundle();
        initStatusBar();
        initActionBar();
        initParticipantsPictures();
        initViews();
        initDimensions();
        initScrollListener();
        initParticipantsListener();
        initCommentButtonListener();

        mBus.post(new LoadParticipantsEvent(mEvent.getId(), 1));
    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.post(new LoadEventSeries(mEvent.getId()));
        refreshEvent();
        mFab.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!fm.popBackStackImmediate()) {
                    Intent resultIntent = getEventRefreshIntent();
                    setResult(REFRESH_EVENT, resultIntent);
                    finish();
                }
                fm.popBackStack();
                break;
            case R.id.action_logout:
                Intent logoutIntent = new Intent(EventDetailActivity.this, UserLoginActivity.class);
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
        inflater.inflate(R.menu.event_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @Override
    public void onBackPressed() {
        if (!fm.popBackStackImmediate()) {
            Intent resultIntent = getEventRefreshIntent();
            setResult(REFRESH_EVENT, resultIntent);
            finish();
        }
        fm.popBackStack();
    }

    /********************************************************************************************
     * Initialization methods
     ********************************************************************************************/

    private void initBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String eventJSON = bundle.getString("event");
            mUserId = bundle.getInt("user_id");
            Gson gson = new Gson();
            mEvent = gson.fromJson(eventJSON, Event.class);
        }
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.eventCardDark));
        }
    }

    private void initActionBar() {

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        mToolbar = findViewById(R.id.toolbar);
        if (!TOOLBAR_IS_STICKY) {
            mToolbar.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void initViews() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.customEventInfoBox);
        mRelativeLayout.setClickable(true);
        RippleDrawable.makeFor(mRelativeLayout, getResources().getColorStateList(R.color.ripple_drawable_view));
        mToolBarColor = getResources().getColor(R.color.eventCard);
        mCalendarButton = (ImageButton) findViewById(R.id.eventDetailCalenderIcon);
        mEventDate = (TextView) findViewById(R.id.customEventDetailDate);
        mEventAddress = (TextView) findViewById(R.id.customEventDetailAddress);
        mEventDescription = (TextView) findViewById(R.id.customEventDetailDescription);
        mEventRewards = (TextView) findViewById(R.id.customEventDetailRewards);
        mEventImage = (ImageView) findViewById(R.id.customEventDetailImage);
        mOverlayView = findViewById(R.id.overlay);
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mEventTitleView = (TextView) findViewById(R.id.customEventDetailTitle);
        mEventParticipants = (TextView) findViewById(R.id.customEventDetailParticipants);
        mCommentButton = (Button) findViewById(R.id.eventCommentsButton);
        setTitle(null);
        mFab = (FloatingActionButton) findViewById(R.id.eventJoinButton);
        ViewHelper.setScaleX(mFab, 1);
        ViewHelper.setScaleY(mFab, 1);
        mFabIsShown = true;
    }

    private void initDimensions() {
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mActionBarSize = getActionBarSize();
        mFlexibleRange = mFlexibleSpaceImageHeight - mActionBarSize - mRelativeLayout.getHeight();
    }

    private void initScrollListener() {
        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 0);
                onScrollChanged(0, false, false);
            }
        });
    }

    private void initParticipantsPictures() {
        int size = 4;
        participantsProfilePicture = new ImageView[size];
        participantsProfilePicture[0] = (ImageView) findViewById(R.id.participant1);
        participantsProfilePicture[1] = (ImageView) findViewById(R.id.participant2);
        participantsProfilePicture[2] = (ImageView) findViewById(R.id.participant3);
        participantsProfilePicture[3] = (ImageView) findViewById(R.id.participant4);
    }

    private void initFabButtonClickListener() {
        // Setup listener for joining unjoining
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEvents.size() > 1) {
                    selectEventsToJoin();
                } else {
                    selectOneEventToJoin();
                }
            }
        });
    }

    private void initParticipantsListener() {
        // Set participants
        if (mEvent.getParticipantsCount() > 0) {
            mEventParticipants.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPeopleFragment();
                }
            });

            for (int i = 0; i < participantsProfilePicture.length; i++) {
                participantsProfilePicture[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPeopleFragment();
                    }
                });
            }
        }
    }

    private void initCommentButtonListener() {
        mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentsActivity();
            }
        });
    }

    /********************************************************************************************
     * Helper methods
     ********************************************************************************************/

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    private void selectOneEventToJoin() {
        List<Integer> eventIds = new ArrayList<>();

        if (!mEvent.getAttending())
            eventIds.add(mEvent.getId());

        mBus.post(new LoadJoinActivityEvent(mEvent.getSeries().getId(), new EventsToJoinRequest(eventIds)));
    }

    private void selectEventsToJoin() {
        final List<Event> events = mEvents;
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

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
            public void onClick(DialogInterface dialog, int position) {
                ListView lw = ((AlertDialog) dialog).getListView();
                SparseBooleanArray checkedItems = lw.getCheckedItemPositions();
                List<Integer> eventIds = new ArrayList<>();

                if (checkedItems != null) {
                    for (int i = 0; i < checkedItems.size(); i++) {
                        if (checkedItems.valueAt(i)) {
                            Event event = events.get(checkedItems.keyAt(i));
                            eventIds.add(event.getId());
                        }
                    }
                }
                mBus.post(new LoadJoinActivityEvent(mEvent.getSeries().getId(), new EventsToJoinRequest(eventIds)));
            }
        });

        adb.setTitle(getString(R.string.selectEventsFromSeries));
        adb.show();
    }

    private void refreshEvent() {

        // Set the event
        setEvent();
        setParticipants();


        if (mEvent.getJoinable() && !mEvent.getPaid() && !mEvent.getAttending()) {
            colorToUnjoin();
            mFab.setIcon(R.drawable.ic_fab_icon);

        } else {
            colorToJoin();
            mFab.setIcon(R.drawable.ic_check_icon);
        }

        initFabButtonClickListener();
    }

    private void setEvent() {
        // Set title
        String eventName = mEvent.getName();
        mEventTitleView.setText(eventName);

        // Set description
        String eventDescription = mEvent.getDescription();
        mEventDescription.setText(eventDescription);

        String address = mEvent.getAddress().hasAddress() ? mEvent.getAddress().displayAddress() : "None";
        mEventAddress.setText(address);

        int showRewards = (mEvent.getRewardPoints() != 0) ? View.VISIBLE : View.GONE;
        mEventRewards.setText(String.valueOf(mEvent.getRewardPoints()) + " Reward Points");
        mEventRewards.setVisibility(showRewards);

        // Set image
        String activityImageUrl = (mEvent.getImage() == null) ? mEvent.getWideUrl() : mEvent.getImage().getWideUrl();
        Picasso.with(this)
                .load(activityImageUrl)
                .fit()
                .centerCrop()
                .into(mEventImage);

        mEventDate.setText(EventHelper.dateRangeToString(mEvent));

        // Set participants
        int participantsCount = mEvent.getParticipantsCount();
        String participantsString = participantsCount == 1 ? " Participant" : " Participants >";
        mEventParticipants.setText(participantsCount + participantsString);
    }

    private void setParticipants() {
        // Set participants pictures
        int count = mEvent.getParticipantsCount() > participantsProfilePicture.length
                ? participantsProfilePicture.length : mEvent.getParticipantsCount();
        if (count > 0) {
            List<Participant> participants = mEvent.getParticipants();
            for (int i = 0; i < count; i++) {
                Participant participant = participants.get(i);
                boolean hasPictureObject = participant.getProfile() != null;
                if (hasPictureObject) {
                    String tempUrl = participant.getProfile().getPicture().getVersions().getThumbURL();
                    participantsProfilePicture[i].setVisibility(View.VISIBLE);

                    Picasso.with(this)
                            .load(tempUrl)
                            .transform(new CircleTransform())
                            .fit()
                            .centerCrop()
                            .into(participantsProfilePicture[i]);
                } else {
                    String tempUrl = participant.getPictureUrl();
                    participantsProfilePicture[i].setVisibility(View.VISIBLE);

                    Picasso.with(this)
                            .load(tempUrl)
                            .transform(new CircleTransform())
                            .fit()
                            .centerCrop()
                            .into(participantsProfilePicture[i]);
                }
            }
        }

        for (int i = count; i < participantsProfilePicture.length; i++) {
            participantsProfilePicture[i].setVisibility(View.GONE);
        }
    }

    private void showPeopleFragment() {

        int color = mResources.getColor(R.color.eventCard);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom,
                R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
        ft.addToBackStack(null);
        PeopleFragment fragment = new PeopleFragment();
        fragment.setBus(mBus);
        fragment.setTitle(getString(R.string.participantsToolbarTitle));
        fragment.setColor(color);
        ft.replace(R.id.eventDetailContainer, fragment);
        ft.commit();
    }

    private void showAddToCalendar() {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        AddToCalendarFragment fragment = new AddToCalendarFragment();
        fragment.setArguments(this, mEvents);

        ft.replace(R.id.add_to_calendar, fragment);
        ft.commit();
    }

    private Intent getEventRefreshIntent() {
        Intent resultIntent = new Intent();
        Gson gson = new Gson();
        resultIntent.putExtra("refresh_event", gson.toJson(mEvent));
        return resultIntent;
    }

    private void colorToJoin() {
        Integer colorFrom = getResources().getColor(R.color.colorPrimary);
        Integer colorTo = getResources().getColor(R.color.cooleafWhite);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFab.setColorNormal((Integer) animation.getAnimatedValue());
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
                mFab.setColorNormal((Integer) animation.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }

    private void showCommentsActivity() {
        int vibrantColor;
        int vibrantColorDark;
        if (Build.VERSION.SDK_INT >= 23) {
            vibrantColor = mResources.getColor(R.color.eventCard, this.getTheme());
            vibrantColorDark = mResources.getColor(R.color.eventCardDark, this.getTheme());
        } else {
            vibrantColor = mResources.getColor(R.color.eventCard);
            vibrantColorDark = mResources.getColor(R.color.eventCardDark);
        }

        Intent i = new Intent(EventDetailActivity.this, EventCommentActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("event_title", mEvent.getName());
        bundle.putInt("vibrant_color", vibrantColor);
        bundle.putInt("dark_vibrant_color", vibrantColorDark);

        LocalPreferences.set("current_event_id", mEvent.getId());

        i.putExtras(bundle);
        startActivity(i);
        this.overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_in_bottom);
    }

    /********************************************************************************************
     * Otto Subscription Methods
     ********************************************************************************************/

    @Subscribe
    public void onLoadedJoinActivityEvent(LoadedJoinActivityEvent loadedJoinActivityEvent) {
        mBus.post(new LoadActivityEvent(mEvent.getId()));
        mBus.post(new LoadEventSeries(mEvent.getId()));
    }

    @Subscribe
    public void onEventLoaded(LoadedActivityEvent loadedEvent) {
        mEvent = loadedEvent.getEvent();
        refreshEvent();
    }

    @Subscribe
    public void onEventSeriesLoaded(LoadedEventsEvent loadedEventSeries) {
        mEvents = loadedEventSeries.getEvents();
        showAddToCalendar();
        mFab.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onUsersLoaded(LoadPeopleEvent loadPeopleEvent) {
        mBus.post(new LoadParticipantsEvent(mEvent.getId(),
                loadPeopleEvent.getPage(),
                loadPeopleEvent.getPerPage()));
    }

    /********************************************************************************************
     * Scroll and parallax implementation using an ObservableScrollView scroll listener
     ********************************************************************************************/

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        float scale = getScale(scrollY, mFlexibleRange);
        int yOffset = mCalendarButton.getTop() + 14;
        int layoutOffSet = mRelativeLayout.getHeight();
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mEventTitleView.getHeight() * scale) - yOffset;
        int titleTranslationY = maxTitleTranslationY - scrollY + layoutOffSet;
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = getFABTranslation(scrollY, maxFabTranslationY);

        // Translate overlay and image
        translateOverlayImage(scrollY);

        // Change alpha of overlay
        adjustOverlayAlpha(scrollY, mFlexibleRange);

        // Scale title text
        adjustTitle(scale);

        // Translate title text
        translateTitle(titleTranslationY);

        // Translate FAB
        translateFAB(layoutOffSet, fabTranslationY);

        // Show/hide FAB
        adjustFAB(scrollY, fabTranslationY);
    }

    private float getScale(int scrollY, float flexibleRange) {
        return 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
    }

    private float getFABTranslation(int scrollY, int maxFabTranslationY) {
        return ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);
    }

    private void translateOverlayImage(float scrollY) {
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mEventImage, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));
    }

    private void adjustOverlayAlpha(int scrollY, float flexibleRange) {
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
        ViewHelper.setAlpha(mRelativeLayout, ScrollUtils.getFloat(flexibleRange / (flexibleRange + scrollY), 0, 1));
    }

    private void adjustTitle(float scale) {
        ViewHelper.setPivotX(mEventTitleView, 0);
        ViewHelper.setPivotY(mEventTitleView, 0);
        ViewHelper.setScaleX(mEventTitleView, scale);
        ViewHelper.setScaleY(mEventTitleView, scale);
    }

    private void translateTitle(int titleTranslationY) {
        // Translate Title text
        if (TOOLBAR_IS_STICKY) {
            titleTranslationY = Math.max(0, titleTranslationY);
        }
        ViewHelper.setTranslationY(mEventTitleView, titleTranslationY);
        ViewHelper.setTranslationX(mEventTitleView, DPToPixel.dpToPx(this, 120));
    }

    private void adjustFAB(int scrollY, float fabTranslationY) {

        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }

        if (TOOLBAR_IS_STICKY) {
            // Change alpha of toolbar background
            if (-scrollY + mFlexibleSpaceImageHeight <= mActionBarSize) {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(1, mToolBarColor));
            } else {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mToolBarColor));
            }
        } else {
            // Translate Toolbar
            if (scrollY < mFlexibleSpaceImageHeight) {
                ViewHelper.setTranslationY(mToolbar, 0);
            } else {
                ViewHelper.setTranslationY(mToolbar, -scrollY);
            }
        }
    }

    private void translateFAB(int layoutOffSet, float fabTranslationY) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
            lp.leftMargin = mOverlayView.getWidth() - mFabMargin - mFab.getWidth();
            lp.topMargin = (int) fabTranslationY;
            mFab.requestLayout();
        } else {
            ViewHelper.setTranslationY(mFab, fabTranslationY);
            ViewHelper.setTranslationY(mFab, fabTranslationY + layoutOffSet);
        }
    }

}
