package zkhaider.com.cooleaf.ui.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.ui.helpers.PaletteCache;
import zkhaider.com.cooleaf.ui.rippledrawable.RippleDrawable;
import zkhaider.com.cooleaf.ui.roundedpicture.CircleTransform;

/**
 * Created by Haider on 2/4/2015.
 */
public class CooleafInterestsAdapter extends RecyclerView.Adapter<CooleafInterestsAdapter.ActivityViewHolder> {

    public static final String TAG = CooleafInterestsAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Resources mResources;
    private Context mContext;
    private User mUser;
    private List<Interest> mInterests;
    private int[] mVibrantColors;
    private int[] mDarkVibrantColors;
    private int mVibrantColor;
    private int mDarkVibrantColor;

    public int getVibrantColor(int position) {
        return mVibrantColors[position];
    }

    public int getDarkVibrantColor(int position) {
        return mDarkVibrantColors[position];
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user)
    {
        mUser = user;
    }

    public void setInterests(List<Interest> interests) {
        mInterests = interests;
        int size = mInterests.size();
        mVibrantColors = new int[size + 1];
        mDarkVibrantColors = new int[size + 1];
    }

    public Interest getInterest(int position) {
        return mInterests.get(position);
    }

    @Override
    public CooleafInterestsAdapter.ActivityViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_item,
                    viewGroup, false);
            mContext = v.getContext();
            mResources = mContext.getResources();
            return new ActivityViewHolder(v, viewType); // Row Item
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.header_profile,
                    viewGroup, false);
            return new ActivityViewHolder(v, viewType); // Header
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final CooleafInterestsAdapter.ActivityViewHolder viewHolder, final int position) {
        if (viewHolder.holderId == 1 && mInterests != null) {
            // Initialize Row Item Stuff
            final Interest interest = mInterests.get(position - 1);

            int membersCount = interest.getUserCount();
            String membersString = membersCount == 1 ? " Member" : " Members";

            viewHolder.interestTitle.setText("#" + interest.getName());

            viewHolder.numOfMembers.setText(Integer.toString(membersCount) + membersString);

            String interestImageUrl = interest.getImage().getWideUrl();

            Picasso.with(viewHolder.itemView.getContext())
                    .load(interestImageUrl)
                    .fit()
                    .centerCrop()
                    .into(viewHolder.interestImage, new Callback() {
                        @Override
                        public void onSuccess() {

                            PaletteCache cache = PaletteCache.getInstance();

                            if (!cache.getVibrantColorCache().containsKey(interest.getName())) {

                                Bitmap bitmap = ((BitmapDrawable) viewHolder.interestImage.getDrawable()).getBitmap(); // Ew!
                                Palette palette = Palette.from(bitmap).generate();
                                mVibrantColor = palette.getVibrantColor(ContextCompat.getColor(mContext, R.color.dark_purple_700));
                                mVibrantColors[position] = mVibrantColor;
                                mDarkVibrantColor = palette.getDarkVibrantColor(ContextCompat.getColor(mContext, R.color.dark_purple_900));
                                mDarkVibrantColors[position] = mDarkVibrantColor;
                                viewHolder.mRelativeLayout.setBackgroundColor(mVibrantColor);

                                PaletteCache.setVibrantColor(interest.getName(), mVibrantColor);
                                PaletteCache.setDarkVibrantColor(interest.getName(), mDarkVibrantColor);

                            } else {
                                viewHolder.mRelativeLayout.setBackgroundColor(PaletteCache.getVibrantColor(interest.getName()));
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });

            viewHolder.interestMask.setClickable(true);

            viewHolder.mRelativeLayout.setClickable(true);

            RippleDrawable.makeFor(viewHolder.interestMask, viewHolder.colorBackgroundStateList, true);
            RippleDrawable.makeFor(viewHolder.mRelativeLayout, viewHolder.colorBackgroundStateList, true);

        } else if (mUser != null) {
            String imageUrl = mUser.getProfile().getPicture().getVersions().getLargeURL();

            Picasso.with(viewHolder.itemView.getContext())
                    .load(imageUrl)
                    .transform(new CircleTransform())
                    .fit()
                    .centerCrop()
                    .into(viewHolder.profilePicture);

            String personName = mUser.getName();
            if (mUser.getName().contains(" "))
                personName = mUser.getName().split(" ")[0];

            String personTitles = mUser.getRole().getOrganization().getName();
            int personRewards = mUser.getRewardPoints();
            viewHolder.personName.setText("Hi, " + personName);
            viewHolder.personTitles.setText(personTitles);

            int showRewards = (personRewards != 0) ? View.VISIBLE : View.GONE;
            viewHolder.personReward.setText(String.valueOf(personRewards) + " Reward Points");
            viewHolder.personReward.setVisibility(showRewards);
        }
    }

    @Override
    public int getItemCount() {
        if(mInterests != null && mUser != null)
            return mInterests.size() + 1;
        if(mInterests != null && mUser == null)
            return mInterests.size();
        if(mInterests == null && mUser != null)
            return 1;
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {

        int holderId;

        // For header
        protected ImageButton profilePicture;
        protected TextView personName;
        protected TextView personTitles;
        protected TextView personReward;

        // For row item
        protected ImageButton interestImage;
        protected TextView numOfMembers;
        protected TextView interestTitle;
        protected RelativeLayout mRelativeLayout;
        protected View interestMask;
        private ImageView[] membersProfilePicture;

        // Color state list
        protected Resources resources;
        protected ColorStateList colorViewStateList;
        protected ColorStateList colorBackgroundStateList;

        public ActivityViewHolder(View v, int viewType) {
            super(v);

            resources = v.getContext().getResources();
            colorViewStateList = resources.getColorStateList(R.color.ripple_drawable_text);
            colorBackgroundStateList = resources.getColorStateList(R.color.ripple_drawable_view);

            if (viewType == TYPE_ITEM) {
                interestImage = (ImageButton) v.findViewById(R.id.interestImage);
                numOfMembers = (TextView) v.findViewById(R.id.interestMembers);
                interestTitle = (TextView) v.findViewById(R.id.interestHashtag);
                mRelativeLayout = (RelativeLayout) v.findViewById(R.id.interestInfo);
                interestMask = (View)  v.findViewById(R.id.interestMask);
                holderId = 1;
            } else {
                profilePicture = (ImageButton) v.findViewById(R.id.profilePictureDashboard);
                personName = (TextView) v.findViewById(R.id.personNameDashBoard);
                personTitles = (TextView) v.findViewById(R.id.personTitlesDashboard);
                personReward = (TextView) v.findViewById(R.id.personRewardsDashboard);
                holderId = 0;
            }

        }
    }
}
