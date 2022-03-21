package zkhaider.com.cooleaf.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.ui.activities.GroupDetailActivity;
import zkhaider.com.cooleaf.ui.adapters.UsersInterestsAdapter;
import zkhaider.com.cooleaf.ui.listeners.OnItemTouchListener;

/**
 * Created by kcoleman on 2/2/15.
 */
public class UsersInterestsFragment extends Fragment implements OnItemTouchListener {

    private static final String TAG = UsersInterestsFragment.class.getSimpleName();

    private UsersInterestsAdapter mInterestAdapter;
    private List<Interest> mInterests;
    private User mUser;

    public void setInterests(List<Interest> interests) {
        mInterests = interests;
        Collections.reverse(mInterests);
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile_gridview, parent, false);

        if (mInterests == null || mInterests.size() == 0)
            return root;

        mInterestAdapter = new UsersInterestsAdapter(getActivity(), mInterests);
        mInterestAdapter.setOnItemTouchListener(this);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.profileInterestsRecyclerView);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(mInterestAdapter);

        return root;
    }

    @Override
    public void onCardViewTap(View view, int position) {
        goToGroupDetail(position);
    }

    @Override
    public void onJoin(TextView view, int position) {
        // Do nothing
    }

    @Override
    public void onAddComment(View view, int position) {
        // Do nothing
    }

    private void goToGroupDetail(int position) {

        Interest interest = mInterestAdapter.getInterest(position);

        int color = mInterestAdapter.getVibrantColor(position);
        int colorDark = mInterestAdapter.getDarkVibrantColor(position);

        Intent i = new Intent(getActivity(), GroupDetailActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putInt("color", color);
        bundle.putInt("dark_color", colorDark);
        bundle.putString("interest", gson.toJson(interest));
        bundle.putString("user", gson.toJson(mUser));
        i.putExtras(bundle);
        startActivity(i);
    }

}
