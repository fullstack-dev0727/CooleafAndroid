package zkhaider.com.cooleaf.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.ui.fragments.SearchFragment;

/**
 * Created by ZkHaider on 10/5/15.
 */
public class SearchViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final int COUNT = 5;

    private Context mContext;
    private String[] mSearchTabNames;

    public SearchViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        mSearchTabNames = new String[COUNT];
        mSearchTabNames = mContext.getResources().getStringArray(R.array.searchTabNames);
    }

    @Override
    public Fragment getItem(int position) {

        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setContext(mContext);

        switch (position) {
            case 0:
                searchFragment.setScope(null);
                return searchFragment;
            case 1:
                searchFragment.setScope(mSearchTabNames[position].toLowerCase());
                return searchFragment;
            case 2:
                searchFragment.setScope(mSearchTabNames[position].toLowerCase());
                return searchFragment;
            case 3:
                searchFragment.setScope(mSearchTabNames[position].toLowerCase());
                return searchFragment;
            case 4:
                searchFragment.setScope(mSearchTabNames[position].toLowerCase());
                return searchFragment;
            default:
                return searchFragment;
        }
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mSearchTabNames[position];
    }

}
