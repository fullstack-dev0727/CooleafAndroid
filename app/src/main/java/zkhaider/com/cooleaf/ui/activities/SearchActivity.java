package zkhaider.com.cooleaf.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.ui.adapters.SearchViewPagerAdapter;

/**
 * Created by ZkHaider on 10/4/15.
 */
public class SearchActivity extends FailureActivity {

    private ViewPager mViewPager;
    private TabLayout mSearchTabLayout;

    /********************************************************************************************
     *  Activity LifeCycle methods
     ********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        initViewPager();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

//    @Override
//    public void onBackPressed() {
//        if (!getSupportFragmentManager().popBackStackImmediate()) {
//            finish();
//        }
//        getSupportFragmentManager().popBackStack();
//    }

    /********************************************************************************************
     *  Initialization methods
     ********************************************************************************************/

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.searchViewPager);
        mSearchTabLayout = (TabLayout) findViewById(R.id.searchTabs);
    }

    private void initViewPager() {
        SearchViewPagerAdapter viewPagerAdapter = new SearchViewPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(viewPagerAdapter);
        mSearchTabLayout.setupWithViewPager(mViewPager);
    }

}
