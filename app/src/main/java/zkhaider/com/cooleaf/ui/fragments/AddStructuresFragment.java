package zkhaider.com.cooleaf.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.cooleafapi.entities.ParentTag;
import zkhaider.com.cooleaf.cooleafapi.entities.Tag;
import zkhaider.com.cooleaf.ui.events.LoadParentTagEvent;

/**
 * Created by Haider on 3/5/2015.
 */
public class AddStructuresFragment extends Fragment {

    public static final String TAG = AddStructuresFragment.class.getSimpleName();

    private Context mContext;
    private ParentTag mParentTag;
    private List<Tag> mTags;
    private List<Tag> mMyTags;
    private List<Integer> myIds = new ArrayList<>();
    private TextView title;
    private boolean mRequired;

    public void setArguments(Context context, ParentTag parentTag, boolean required,
                             List<Tag> myTags) {
        mContext = context;
        mParentTag = parentTag;
        this.mRequired = required;
        mTags = parentTag.getAllTags();


        if (myTags == null) {
            myTags = new ArrayList<>();
        }
        mMyTags = myTags;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup parent, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pick_structures, null);

        title = (TextView) root.findViewById(R.id.structureTagName);
        title.setText("Pick " + mParentTag.getName());

        final ImageButton button = (ImageButton) root.findViewById(R.id.structureEditButton);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);

                final boolean[] checkedPositions = new boolean[mTags.size()];
                CharSequence[] csTagNamesArray = new CharSequence[mTags.size()];

                // Get ids
                List<Integer> ids = new ArrayList<>();
                for (int i = 0; i < mTags.size(); i++) {
                    ids.add(mTags.get(i).getId());
                }

                // Get ids from myTagList
                for (int i = 0; i < mMyTags.size(); i++) {
                    myIds.add(mMyTags.get(i).getId());
                }

                // Initialize names
                for (int i = 0; i < mTags.size(); i++) {
                    csTagNamesArray[i] = mTags.get(i).getName();
                }

                for (Integer id : ids) {
                    if (myIds.contains(id)) {
                        int index = ids.indexOf(id);
                        checkedPositions[index] = mTags.get(index).getActive();
                    } else {
                        int index = ids.indexOf(id);
                        checkedPositions[index] = false;
                    }
                }

                for (int i = 0; i < checkedPositions.length; i++) {
                    mTags.get(i).setActive(checkedPositions[i]);
                }

                adb.setMultiChoiceItems(csTagNamesArray, checkedPositions, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        mTags.get(which).setActive(isChecked);
                        if (isChecked) {
                            if (!myIds.contains(mTags.get(which).getId()))
                                myIds.add(mTags.get(which).getId());
                            checkedPositions[which] = isChecked;
                        } else if (!isChecked) {
                            if (myIds.contains(mTags.get(which).getId())) {
                                int index = myIds.indexOf(mTags.get(which).getId());
                                myIds.remove(index);
                                checkedPositions[which] = !isChecked;
                            }
                        }

                    }
                });

                adb.setNegativeButton(android.R.string.cancel, null);
                adb.setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {

                        mParentTag.setTags(mTags);
                        BusProvider.getInstance().post(new LoadParentTagEvent(mParentTag));

                    }
                });

                adb.setTitle("Pick your " + mParentTag.getName());
                adb.show();
            }
        });

        return root;
    }

}
