package zkhaider.com.cooleaf.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.squareup.otto.Subscribe;

import retrofit.client.Response;
import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.base.BaseEventReportingActivity;
import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.cooleafapi.entities.APIError;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.cooleafapi.utils.RetrofitUtility;
import zkhaider.com.cooleaf.cooleafapi.events.LoadedErrorEvent;

/**
 * Created by kcoleman on 2/3/15.
 */
public abstract class FailureActivity extends BaseEventReportingActivity {

    protected static final int REFRESH_EVENT = 5;

    protected BusProvider mBus;
    public static final String TAG = FailureActivity.class.getSimpleName();
    protected Object mBusFailureEventListener;
    private boolean displayingAlert;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBusFailureEventListener = createBusFailureListener();
        mBus = getBus();
    }

    @Override
    public void onResume() {
        super.onResume();
        getBus().register(mBusFailureEventListener);
        getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getBus().register(mBusFailureEventListener);
        getBus().unregister(this);

    }

    private Object createBusFailureListener() {
        return new Object() {
            @Subscribe
            public void onErrorLoaded(final LoadedErrorEvent event) {
                FailureActivity.this.onErrorLoaded(event);
            }
        };
    }

    protected void displayAlert(Context context, String message, final boolean reauthenticate) {

        if (!displayingAlert) {
            if (!((Activity) context).isFinishing()) {
                displayingAlert = true;
                new AlertDialog.Builder(context)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (reauthenticate)
                                    goToUserLogin();
                                displayingAlert = false;
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        }
    }

    protected void goToUserLogin() {
        Intent userLoginIntent = new Intent(this, UserLoginActivity.class);
        userLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        userLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(userLoginIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void onErrorLoaded(LoadedErrorEvent loadedEvent) {
        Response response = loadedEvent.getError().getResponse();
        APIError error = RetrofitUtility.getAPIError(response);

        displayAlert(this, error.getAlert(), needToReauthenticate(response));
    }

    private boolean needToReauthenticate(Response response) {
        return response != null && (response.getStatus() == 401 || response.getStatus() >= 500) && !response.getUrl().contains("authorize");
    }

    protected BusProvider getBus() {
        if (mBus == null)
            mBus = BusProvider.getInstance();
        return mBus;
    }
}
