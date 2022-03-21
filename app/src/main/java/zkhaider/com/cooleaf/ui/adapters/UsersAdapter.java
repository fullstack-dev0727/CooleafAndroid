package zkhaider.com.cooleaf.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.ui.roundedpicture.CircleTransform;

/**
 * Created by Haider on 2/4/2015.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    public static final String TAG = UsersAdapter.class.getSimpleName();

    private Context mContext;
    private List<User> mUsers = new ArrayList<>();

    public User getUser(int position) {
        return mUsers.get(position);
    }

    public void addUsers(List<User> users)
    {
        mUsers.addAll(users);
    }

    public void setUsers(List<User> users)
    {
        mUsers = users;
    }

    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_item,
                viewGroup, false);
        mContext = v.getContext();
        UserViewHolder viewItem = new UserViewHolder(v); // Row Item

        return viewItem;
    }

    @Override
    public void onBindViewHolder(final UsersAdapter.UserViewHolder viewHolder, int position) {
        User user = mUsers.get(position);

        String name = user.getShortName();
        viewHolder.personName.setText(name);

        String title = user.getRole().getDepartment().getName();
        viewHolder.personTitle.setText(title);

        String userImageUrl = user.getProfile().getPicture().getVersions().getMediumURL();

        Picasso.with(viewHolder.itemView.getContext())
                .load(userImageUrl)
                .transform(new CircleTransform())
                .fit()
                .centerCrop()
                .skipMemoryCache()
                .into(viewHolder.personProfilePicture);
    }

    @Override
    public int getItemCount() {
        if (mUsers != null)
            return mUsers.size();
        else
            return 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        protected ImageView personProfilePicture;
        protected TextView personName;
        protected TextView personTitle;

        public UserViewHolder(View v) {
            super(v);
            personProfilePicture = (ImageView) v.findViewById(R.id.personProfilePicture);
            personTitle = (TextView) v.findViewById(R.id.personTitle);
            personName = (TextView) v.findViewById(R.id.personName);
        }
    }
}
