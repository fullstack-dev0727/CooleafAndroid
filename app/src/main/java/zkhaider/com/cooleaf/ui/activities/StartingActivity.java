package zkhaider.com.cooleaf.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import zkhaider.com.cooleaf.R;

/**
 * Created by Haider on 12/23/2014.
 */
public class StartingActivity extends Activity {

    private static final String SPLASH_PREFS = "SplashSetting";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);

        boolean splashShown = getSplashValue();

        if (splashShown) {
            goToMainActivity();
        } else {
            Handler handler = new Handler();
            int splashDisplayLength = 3000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToMainActivity();
                    saveSplashPref();
                }
            }, splashDisplayLength);
        }
    }

    private void goToMainActivity() {
        Intent openMainActivity =  new Intent(StartingActivity.this, MainActivity.class);
        startActivity(openMainActivity);
        finish();
    }

    private void saveSplashPref() {
        SharedPreferences settings = this.getSharedPreferences(SPLASH_PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("splash_shown", true);
        editor.apply();
    }

    private boolean getSplashValue() {
        SharedPreferences settings = getSharedPreferences(SPLASH_PREFS, 0);
        return settings.getBoolean("splash_shown", false);
    }

}
