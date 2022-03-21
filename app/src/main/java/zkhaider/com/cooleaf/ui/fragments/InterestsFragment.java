package zkhaider.com.cooleaf.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Edit;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.mvp.interests.presenters.InterestPresenter;
import zkhaider.com.cooleaf.ui.activities.DashboardActivity;
import zkhaider.com.cooleaf.ui.adapters.InterestAdapter;

/**
 * Created by kcoleman on 2/2/15.
 * Edited by ZkHaider
 */
public class InterestsFragment extends Fragment {

    private static final String TAG = InterestsFragment.class.getSimpleName();

    @InjectView(R.id.gridView)
    GridView mGridview;

    private InterestAdapter mInterestAdapter;
    private List<Interest> mInterests = new ArrayList<>();
    private List<Integer> mCategoryIds = new ArrayList<>();
    private Edit mEdit;
    private InterestPresenter mInterestPresenter;

    public void setEdit(Edit edit) {
        this.mEdit = edit;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_interests, null);
        ButterKnife.inject(this, root);
        initPresenter();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        initPresenter();
        mInterestPresenter.register();
    }

    @Override
    public void onPause() {
        super.onPause();
        mInterestPresenter.unregister();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.doneRegistration:
                mCategoryIds = mInterestAdapter.getInterestIds();
                mInterestPresenter.saveCategoryIdsToProfile(mCategoryIds);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPresenter() {
        mInterestPresenter = new InterestPresenter(this);
        mInterestPresenter.loadInterests();
        mInterestPresenter.setEdit(mEdit);
    }

    public void onInterestsLoaded(List<Interest> interests) {
        setAdapter(interests);
    }

    private void setAdapter(List<Interest> interests) {
        mInterestAdapter = new InterestAdapter(getActivity());
        mInterestAdapter.setInterests(interests);
        mGridview.setAdapter(mInterestAdapter);
        mGridview.invalidate();
    }

    public void displayError() {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.interests_check)
                .show();
    }

    public void goToDashboard() {
        Intent i = new Intent(getActivity(), DashboardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

}
