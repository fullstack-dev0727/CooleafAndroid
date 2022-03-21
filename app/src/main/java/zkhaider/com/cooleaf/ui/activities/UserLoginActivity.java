package zkhaider.com.cooleaf.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.gcm.GCMConfig;
import zkhaider.com.cooleaf.mvp.users.events.LoadedMeEvent;
import zkhaider.com.cooleaf.cooleafapi.entities.Registration;
import zkhaider.com.cooleaf.mvp.registrations.events.LoadedRegistrationCheckEvent;
import zkhaider.com.cooleaf.ui.fragments.ViewPagerFragment;
import zkhaider.com.cooleaf.utils.NetworkHelper;

/**
 * Created by Haider on 12/23/2014.
 */
public class UserLoginActivity extends FailureActivity {

    public static final String TAG = UserLoginActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        getSupportActionBar().hide();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, ViewPagerFragment.newInstance());
        ft.commit();

        if (NetworkHelper.isInternetAvailable(this)) {
            GCMConfig.GCMRegistration_id(this);
        }
    }

    @Subscribe
    public void onLoadedRegistrationCheckEvent(LoadedRegistrationCheckEvent loadedRegistrationCheckEvent) {
        Registration registration = loadedRegistrationCheckEvent.getRegistration();

        Intent i = new Intent(UserLoginActivity.this, BasicInfoActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putString("registration", gson.toJson(registration));
        i.putExtras(bundle);
        startActivity(i);
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Subscribe
    public void onLoadedMeEvent(LoadedMeEvent loadMeEvent) {
        startDashboard();
    }

    private void startDashboard() {
        Intent i = new Intent(UserLoginActivity.this, DashboardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}
