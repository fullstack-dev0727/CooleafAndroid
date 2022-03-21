package zkhaider.com.cooleaf.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.gson.Gson;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.ui.fragments.EventFragment;
import zkhaider.com.cooleaf.ui.fragments.PostFragment;

/**
 * Created by ZkHaider on 7/3/15.
 */
public class InterestViewPagerAdapter extends FragmentPagerAdapter {

    private static final int TOTAL_COUNT = 2;
    private static final String FEEDABLE_TYPE = "Interest";

    private AppBarLayout mAppBarLayout;
    private Context mContext;
    private User mUser;
    private Interest mInterest;
    private String[] mTabNames;

    public InterestViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
        Resources resources = mContext.getResources();
        mTabNames = resources.getStringArray(R.array.groupDetailTabNames);
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public void setInterest(Interest interest) {
        this.mInterest = interest;
    }

    public void addPost(Post post) {
        PostFragment.getInstance().addPost(post);
    }

    public void setAppBarLayout(AppBarLayout appBarLayout) {
        this.mAppBarLayout = appBarLayout;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PostFragment postFragment = new PostFragment();
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                bundle.putString("feedable_type", FEEDABLE_TYPE);
                String interestJSON = gson.toJson(mInterest);
                bundle.putString("interest", interestJSON);
                postFragment.setArguments(bundle);
                mAppBarLayout.addOnOffsetChangedListener(postFragment.getOffsetChangedListener());
                return postFragment;
            case 1:
                EventFragment groupEventsFragment = new EventFragment();
                groupEventsFragment.setUser(mUser);
                groupEventsFragment.setInterest(mInterest);
                mAppBarLayout.addOnOffsetChangedListener(groupEventsFragment.getOffsetChangedListener());
                return groupEventsFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return TOTAL_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabNames[position];
    }

}
