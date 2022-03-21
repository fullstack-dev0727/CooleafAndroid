package zkhaider.com.cooleaf.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import zkhaider.com.cooleaf.ui.fragments.LoginFragment;
import zkhaider.com.cooleaf.ui.fragments.SignUpFragment;

/**
 * Created by Haider on 12/23/2014.
 */
public class UserLoginViewPagerAdaper extends FragmentPagerAdapter {

    private Context mContext;
    public static int totalPage = 0;

    public UserLoginViewPagerAdaper(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return LoginFragment.newInstance();
            case 1:
                return SignUpFragment.newInstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
