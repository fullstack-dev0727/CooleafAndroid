package zkhaider.com.cooleaf.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.ui.listeners.OnItemTouchListener;

/***
 * Created by kcoleman on 2/2/15.
 */
public class UsersInterestsAdapter extends RecyclerView.Adapter<UsersInterestsAdapter.InterestViewHolder> {

    public static final String TAG = InterestAdapter.class.getSimpleName();

    private Resources mResources;
    private Context mContext;
    private List<Interest> mInterests;
    private List<Integer> mInterestIds = new ArrayList<>();
    private int[] mVibrantColors;
    private int[] mDarkVibrantColors;
    private int mVibrantColor;
    private int mDarkVibrantColor;
    private OnItemTouchListener mOnItemTouchListener;

    public UsersInterestsAdapter(Context c, List<Interest> interests) {
        mContext = c;
        mInterests = interests;
        mResources = c.getResources();
        mVibrantColors = new int[mInterests.size()];
        mDarkVibrantColors = new int[mInterests.size()];
    }

    public List<Integer> getInterestIds() {
        return mInterestIds;
    }

    public Interest getInterest(int position) {
        return mInterests.get(position);
    }

    public int getVibrantColor(int position) {
        return mVibrantColors[position];
    }

    public int getDarkVibrantColor(int position) {
        return mDarkVibrantColors[position];
    }

    public void setOnItemTouchListener(OnItemTouchListener onItemTouchListener) {
        this.mOnItemTouchListener = onItemTouchListener;
    }

    @Override
    public InterestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.users_interest_item, parent, false);
        return new InterestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final InterestViewHolder holder, final int position) {

        final Interest interest =  mInterests.get(position);

        // Set the name
        String url = interest.getImage().getUrl();
        String name = "# " + interest.getName();
        holder.mInterestTextView.setText(name);

        // Set the image
        Picasso.with(mContext)
                .load(url)
                .fit()
                .centerCrop()
                .into(holder.mInterestImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) holder.mInterestImageView
                                .getDrawable()).getBitmap(); // Ew!
                        Palette palette = Palette.from(bitmap).generate();
                        mVibrantColor = palette.getVibrantColor(mResources.getColor(R.color.dark_purple_700));
                        mVibrantColors[position] = mVibrantColor;
                        mDarkVibrantColor = palette.getDarkVibrantColor(mResources.getColor(R.color.dark_purple_900));
                        mDarkVibrantColors[position] = mDarkVibrantColor;
                        holder.mLinearLayout.setBackgroundColor(mVibrantColor);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return mInterests.size();
    }

    public class InterestViewHolder extends RecyclerView.ViewHolder {

        private ImageView mInterestImageView;
        private TextView mInterestTextView;
        private LinearLayout mLinearLayout;

        public InterestViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
            initListeners(itemView);
        }

        private void initViews(View view) {
            mInterestImageView = (ImageView) view.findViewById(R.id.interestImage);
            mInterestTextView = (TextView) view.findViewById(R.id.interestTitle);
            mLinearLayout = (LinearLayout) view.findViewById(R.id.titleLayout);
        }

        private void initListeners(View view) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemTouchListener.onCardViewTap(v, getAdapterPosition());
                }
            });
        }

    }

}
