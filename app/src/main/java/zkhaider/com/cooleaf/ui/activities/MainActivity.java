package zkhaider.com.cooleaf.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.gcm.GCMConfig;
import zkhaider.com.cooleaf.utils.LocalPreferences;
import zkhaider.com.cooleaf.utils.NetworkHelper;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String PREFS_NAME = "CooleafAPIClient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String session = settings.getString("session", null);

        String deviceID = LocalPreferences.getString("gcm_token");

        if (NetworkHelper.isInternetAvailable(this) && deviceID == null) {
            GCMConfig.GCMRegistration_id(this);
        }

        if (session == null || !LocalPreferences.getBoolean(GCMConfig.SEND_TOKEN_TO_SERVER)) {
            goToUserLogin();
        } else {
            goToDashboard();
        }
    }

    private void goToUserLogin() {
        Intent userLoginIntent = new Intent(this, UserLoginActivity.class);
        userLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        userLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(userLoginIntent);
    }

    private void goToDashboard() {
        Intent userLoginIntent = new Intent(this, DashboardActivity.class);
        userLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        userLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(userLoginIntent);
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

        return super.onOptionsItemSelected(item);
    }

}
