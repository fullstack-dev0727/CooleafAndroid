package zkhaider.com.cooleaf.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.ui.fragments.EventCommentsFragment;
import zkhaider.com.cooleaf.utils.LocalPreferences;

/**
 * Created by ZkHaider on 8/14/15.
 */
public class EventCommentActivity extends CameraActivity {

    private int mEventId;
    private String mEventTitle;
    private int mVibrantColor;
    private int mVibrantDarkColor;

    /********************************************************************************************
     *  Activity Lifecycle methods
     ********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        initBundle();
        initStatusBar();
        initToolbar();
        initCommentFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                this.overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
                break;
            case R.id.action_logout:
                Intent logoutIntent = new Intent(EventCommentActivity.this, UserLoginActivity.class);
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
        finish();
        this.overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
    }

    /********************************************************************************************
     *  Initialization methods
     ********************************************************************************************/

    private void initBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mEventId = bundle.getInt("event_id");
            mEventTitle = bundle.getString("event_title");

            mVibrantColor = bundle.getInt("vibrant_color");
            mVibrantDarkColor = bundle.getInt("dark_vibrant_color");
        }
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(mVibrantDarkColor);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.commentToolbar);
        toolbar.setBackgroundColor(mVibrantColor);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mEventTitle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getSupportActionBar().setElevation(0);
            }
        }
    }

    private void initCommentFragment() {
        EventCommentsFragment fragment = new EventCommentsFragment();
        fragment.setVibrantColor(mVibrantColor);

        // Set feed id
        int currentEventId = LocalPreferences.getInt("current_event_id");
        fragment.setEventId(currentEventId);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.commentContent, fragment)
                .commit();
    }

}
