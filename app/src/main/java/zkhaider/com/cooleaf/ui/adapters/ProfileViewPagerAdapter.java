package zkhaider.com.cooleaf.ui.adapters;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.ui.fragments.EmptyContentFragment;
import zkhaider.com.cooleaf.ui.fragments.EventFragment;
import zkhaider.com.cooleaf.ui.fragments.InformationFragment;
import zkhaider.com.cooleaf.ui.fragments.UsersInterestsFragment;

public class ProfileViewPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = ProfileViewPagerAdapter.class.getSimpleName();
    private static final int COUNT = 3;

    private AppBarLayout mAppBarLayout;
    private Context mContext;
    private User mUser;
    private String[] mProfileTabNames;

    public void setUser(User user) {
        mUser = user;
    }

    public ProfileViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        mProfileTabNames = mContext.getResources().getStringArray(R.array.profileTabNames);
    }

    public void setAppBarLayout(AppBarLayout appBarLayout) {
        this.mAppBarLayout = appBarLayout;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                InformationFragment informationFragment = new InformationFragment();
                informationFragment.setStructures(mUser.getRole().getStructures());
                return informationFragment;
            case 1:
                EventFragment eventFragment = new EventFragment();
                eventFragment.setUser(mUser);
                eventFragment.setShowHeader(false);
                mAppBarLayout.addOnOffsetChangedListener(eventFragment.getOffsetChangedListener());
                return eventFragment;
            case 2:
                UsersInterestsFragment interestFragment = new UsersInterestsFragment();
                interestFragment.setInterests(mUser.getCategories());
                interestFragment.setUser(mUser);
                return interestFragment;
            default:
                return EmptyContentFragment.getInstance();
        }
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mProfileTabNames[position];
    }

}