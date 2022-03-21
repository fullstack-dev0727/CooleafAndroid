package zkhaider.com.cooleaf.ui.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.ui.adapters.UserLoginViewPagerAdaper;
import zkhaider.com.cooleaf.ui.materialtabs.MaterialTab;
import zkhaider.com.cooleaf.ui.materialtabs.MaterialTabHost;
import zkhaider.com.cooleaf.ui.materialtabs.MaterialTabListener;

/**
 * Created by Haider on 12/24/2014.
 */
public class ViewPagerFragment extends Fragment implements MaterialTabListener {

    private MaterialTabHost materialTabHost;
    private MaterialTab[] materialTabs;
    private ViewPager mViewPager;
    private UserLoginViewPagerAdaper mViewPagerAdapter;

    private String[] tabsTitles;
    private Resources res;

    public static ViewPagerFragment fragment;

    public static Fragment newInstance() {
        if (fragment == null) {
            fragment = new ViewPagerFragment();
        }

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewpager, null);

        res = getResources();
        tabsTitles = res.getStringArray(R.array.signInTabNames);

        // Setup Material Tabs and Listener
        materialTabHost = (MaterialTabHost) root.findViewById(R.id.materialTabHost);
        mViewPager = (ViewPager) root.findViewById(R.id.viewPager);
        mViewPagerAdapter = new UserLoginViewPagerAdaper(getActivity(), getChildFragmentManager());

        materialTabs = new MaterialTab[mViewPagerAdapter.getCount()];

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                materialTabHost.setSelectedNavigationItem(position);
            }
        });
        setupMaterialTabs();

        return root;
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        // when the tab is clicked the pager swipe content to the tab position
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    private void setupMaterialTabs() {
        // Insert the tabs into MaterialTabHost
        for (int i = 0; i < mViewPagerAdapter.getCount(); i++) {
            materialTabs[i] = new MaterialTab(getActivity(), false);
            materialTabs[i].setText(tabsTitles[i]);
            materialTabs[i].setTabListener(this);
            materialTabHost.addTab(materialTabs[i]);
        }
    }


}
