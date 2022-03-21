package zkhaider.com.cooleaf.ui.navdrawer;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.ui.rippledrawable.RippleDrawable;
import zkhaider.com.cooleaf.ui.roundedpicture.CircleTransform;

/**
 * Created by Haider on 2/3/2015.
 */
public class NavDrawerAdapter extends RecyclerView.Adapter<NavDrawerAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<NavMenuItem> mNavMenuItems;
    private User user;

    public NavDrawerAdapter() {
    }
    public NavDrawerAdapter(List<NavMenuItem> navMenuItems, User user) {
        this.mNavMenuItems = navMenuItems;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public NavDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.navdrawer_row_item,
                                            viewGroup, false);
            ViewHolder viewItem = new ViewHolder(v, viewType); // Nav Drawer Row Item

            return viewItem;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.navdrawer_header,
                                            viewGroup, false);
            ViewHolder viewHeader = new ViewHolder(v, viewType); // Nav Drawer Header

            return viewHeader;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(NavDrawerAdapter.ViewHolder viewHolder, int position) {
        if (viewHolder.holderId == 1) {
            viewHolder.rowTitle.setText(mNavMenuItems.get(position - 1).getTitle());
            viewHolder.rowIcon.setImageResource(mNavMenuItems.get(position-1).getIcon());

            viewHolder.mLinearLayout.setClickable(true);
            RippleDrawable.makeFor(viewHolder.mLinearLayout, viewHolder.colorBackgroundStateList, true);
        } else {

            String imageUrl = user.getProfile().getPicture().getVersions().getLargeURL();

            Log.d(NavDrawerAdapter.class.getSimpleName(), "The url for your profile mTypedFile is: " + imageUrl);

            Picasso.with(viewHolder.itemView.getContext())
                    .load(imageUrl)
                    .transform(new CircleTransform())
                    .fit()
                    .centerCrop()
                    .into(viewHolder.profilePicture);

            String personName = user.getName();
            String personTitles = user.getRole().getOrganization().getName();
            int personRewards = user.getRewardPoints();

            viewHolder.personName.setText(personName);
            viewHolder.personTitles.setText(personTitles);

            int showRewards = (personRewards != 0) ? View.VISIBLE : View.GONE;
            viewHolder.personReward.setText(String.valueOf(personRewards) + " Reward Points");
            viewHolder.personReward.setVisibility(showRewards);
        }
    }

    @Override
    public int getItemCount() {
        if (mNavMenuItems != null)
            return mNavMenuItems.size() + 1; // number of items plus the header
        else
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int holderId;

        protected TextView rowTitle;
        protected ImageView rowIcon;
        protected ImageButton profilePicture;
        protected TextView personName;
        protected TextView personTitles;
        protected TextView personReward;
        protected LinearLayout mLinearLayout;

        protected Resources resources;
        protected ColorStateList colorBackgroundStateList;

        public ViewHolder(View v, int viewType) {
            super(v);

            resources = v.getContext().getResources();
            colorBackgroundStateList = resources.getColorStateList(R.color.ripple_drawable_text);

            if (viewType == TYPE_ITEM) {
                rowTitle = (TextView) v.findViewById(R.id.navDrawerRowText);
                rowIcon = (ImageView) v.findViewById(R.id.navDrawerRowIcon);
                mLinearLayout = (LinearLayout) v.findViewById(R.id.navRowLayout);
                holderId = 1;
            } else {
                profilePicture = (ImageButton) v.findViewById(R.id.profilePictureNavDrawer);
                personName = (TextView) v.findViewById(R.id.personName);
                personTitles = (TextView) v.findViewById(R.id.personTitles);
                personReward = (TextView) v.findViewById(R.id.personRewards);
                holderId = 0;
            }

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
