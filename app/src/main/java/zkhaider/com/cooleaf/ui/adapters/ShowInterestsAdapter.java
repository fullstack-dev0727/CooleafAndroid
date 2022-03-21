package zkhaider.com.cooleaf.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;

/**
 * Created by kcoleman on 2/2/15.
 */
public class ShowInterestsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Interest> mInterests;

    public ShowInterestsAdapter(Context c, List<Interest> interests) {
        mContext = c;
        mInterests = interests;
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

        Interest interest = mInterests.get(position);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.interest_item, null);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        String url = interest.getImage().getUrl();

        ImageView interestImage = (ImageView) convertView.findViewById(R.id.interestImage);
        Picasso.with(convertView.getContext())
                .load(url)
                .into(interestImage);

        return convertView;
    }

}
