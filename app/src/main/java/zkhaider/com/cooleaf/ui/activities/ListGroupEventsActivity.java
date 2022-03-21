package zkhaider.com.cooleaf.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.squareup.otto.Subscribe;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.mvp.events.events.LoadFragmentEventsEvent;
import zkhaider.com.cooleaf.mvp.interests.events.LoadInterestEventsEvent;
import zkhaider.com.cooleaf.ui.fragments.EventFragment;

/**
 * Created by Haider on 12/24/2014.
 */
public class ListGroupEventsActivity extends DrawerActivity {

    public static final String TAG = ListEventsActivity.class.getSimpleName();
    private int mInterestId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView();
        initActionBar(R.string.group_events_title);
        initEventFragment();
        mBus = BusProvider.getInstance();
        Bundle bundle = getIntent().getExtras();

        mInterestId = bundle.getInt("interest_id");

    }

    @Subscribe
    public void onLoadFragmentEventsEvent(LoadFragmentEventsEvent loadFragmentEventsEvent) {
        mBus.post(new LoadInterestEventsEvent(mInterestId,
                loadFragmentEventsEvent.getPage(), loadFragmentEventsEvent.getPerPage()));
    }

    private void inflateView() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.mainContent);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = inflater.inflate(R.layout.activity_dashboard, null, false);
        frameLayout.addView(activityView);
    }

    private void initEventFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        EventFragment fragment = new EventFragment();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

}
