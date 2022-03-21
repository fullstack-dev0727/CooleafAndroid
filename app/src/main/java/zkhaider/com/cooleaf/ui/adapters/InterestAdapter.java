package zkhaider.com.cooleaf.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;

/**
 * Created by kcoleman on 2/2/15.
 */
public class InterestAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    public static final String TAG = InterestAdapter.class.getSimpleName();

    private Context mContext;
    private SparseBooleanArray mCheckStates;
    private CheckBox checkBox;
    private List<Interest> mInterests;
    private List<Integer> mInterestIds = new ArrayList<>();

    public InterestAdapter(Context c) {
        mContext = c;
    }

    public void setInterests(List<Interest> interests) {
        this.mInterests = interests;
        mCheckStates = new SparseBooleanArray(mInterests.size());
    }

    public List<Integer> getInterestIds() {
        return mInterestIds;
    }

    public Interest getInterest(int position) {
        return mInterests.get(position);
    }

    public int getCount() {
        return mInterests.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        final Interest interest = mInterests.get(position);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.interest_item, null);
        }

        checkBox = (CheckBox) convertView.findViewById(R.id.interestCheckbox);

        checkBox.setTag(position);
        checkBox.setChecked(mCheckStates.get(position, false));
        checkBox.setOnCheckedChangeListener(this);

        checkBox.setText("#" + interest.getName());
        //checkBox.setChecked(checks[position]);

        final int pos = position;

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBoxChecked = (CheckBox) v.findViewById(R.id.interestCheckbox);
                boolean state = checkBoxChecked.isChecked();
                //checks[pos] = state;
                checkBoxChecked.setChecked(!state);

                int id = interest.getId();
                if (checkBoxChecked.isChecked() && !mInterestIds.contains(id)) {
                    mInterestIds.add(id);
                } else if (!checkBoxChecked.isChecked() && mInterestIds.contains(id)) {
                    int index = mInterestIds.indexOf(id);
                    mInterestIds.remove(index);
                }

                // For checking which ids are in the list
                for (int i = 0; i < mInterestIds.size(); i++) {
                    Log.d(TAG, String.valueOf(mInterestIds.get(i)));
                }

                v.invalidate();
            }
        });

        String url = interest.getImage().getUrl();

        ImageView interestImage = (ImageView) convertView.findViewById(R.id.interestImage);
        Picasso.with(convertView.getContext())
                .load(url)
                .into(interestImage);

        return convertView;
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCheckStates.put((Integer) buttonView.getTag(), isChecked);
    }
}
