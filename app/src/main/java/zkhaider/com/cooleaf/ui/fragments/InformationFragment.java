package zkhaider.com.cooleaf.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.ParentTag;
import zkhaider.com.cooleaf.ui.adapters.InformationAdapter;

/***
 * Created by Haider on 2/17/2015.
 */
public class InformationFragment extends Fragment {

    public static InformationFragment mInformationFragment;
    public static final String TAG = InformationFragment.class.getSimpleName();

    private InformationAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<ParentTag> mParentTags;

    public static InformationFragment getInstance() {
        if (mInformationFragment == null)
            mInformationFragment = new InformationFragment();
        return mInformationFragment;
    }

    public void setStructures(List<ParentTag> parentTags) {
        mParentTags = parentTags;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_information, parent, false);
        initViews(root);
        initRecyclerView();
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews(View root) {
        mRecyclerView = (RecyclerView) root.findViewById(R.id.structuresRecyclerView);
    }

    private void initRecyclerView() {
        // Set LinearLayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // Set Adapter
        mAdapter = new InformationAdapter(getActivity());
        mAdapter.setStructures(mParentTags);

        // Set RecyclerView
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(false);
    }

}
