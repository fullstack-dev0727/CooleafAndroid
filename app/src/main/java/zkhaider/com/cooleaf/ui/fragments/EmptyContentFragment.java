package zkhaider.com.cooleaf.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zkhaider.com.cooleaf.R;

/**
 * Created by ZkHaider on 7/16/15.
 */
public class EmptyContentFragment extends Fragment {

    private static EmptyContentFragment mEmptyContentFragment;

    public static EmptyContentFragment getInstance() {
        if (mEmptyContentFragment == null)
            mEmptyContentFragment = new EmptyContentFragment();
        return mEmptyContentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_no_events, null);
        return root;
    }
}
