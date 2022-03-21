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

import com.google.gson.Gson;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.ui.fragments.FeedDetailFragment;
import zkhaider.com.cooleaf.utils.LocalPreferences;

/**
 * Created by ZkHaider on 7/10/15.
 */

/****
 *  This class can be refactored into a Parent Activity that extends CameraActivity which can then
 *  be used by Comments for Groups and Events
 */
public class CommentActivity extends CameraActivity {

    private static final String TAG = CommentActivity.class.getSimpleName();

    private Post mParentPost;
    private String mEventTitle;
    private int mFeedId;
    private int mDarkVibrantColor;
    private int mVibrantColor;

    /**
     * ***************************************************************************************
     * LifeCycle Methods
     * ****************************************************************************************
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        initBundle();
        initStatusBar();
        initToolbar();

        if (mParentPost != null)
            initFeedDetailFragment();
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
                if (!getSupportFragmentManager().popBackStackImmediate()) {
                    finish();
                    this.overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
                }
                getSupportFragmentManager().popBackStack();
                break;
            case R.id.action_logout:
                Intent logoutIntent = new Intent(CommentActivity.this, UserLoginActivity.class);
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
        if (!getSupportFragmentManager().popBackStackImmediate()) {
            finish();
        }
        getSupportFragmentManager().popBackStack();
    }

    /**
     * ***************************************************************************************
     * Initialization Methods
     * ****************************************************************************************
     */

    private void initBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            // Get Event title if event comments
            mEventTitle = bundle.getString("group_name");

            // Get Parent Post
            String postJSON = bundle.getString("parent_post");
            Gson gson = new Gson();
            mParentPost = gson.fromJson(postJSON, Post.class);
            if (mParentPost != null)
                mFeedId = mParentPost.getFeedId();

            // Get Vibrant Color
            mVibrantColor = bundle.getInt("vibrant_color");
            mDarkVibrantColor = bundle.getInt("dark_vibrant_color");
        }
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

    private void initFeedDetailFragment() {
        FeedDetailFragment fragment = new FeedDetailFragment();
        fragment.setVibrantColor(mVibrantColor);

        // Set feed id
        int currentFeedId = LocalPreferences.getInt("current_feed_id");
        fragment.setFeedId(currentFeedId);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.commentContent, fragment)
                .commit();
    }

}
