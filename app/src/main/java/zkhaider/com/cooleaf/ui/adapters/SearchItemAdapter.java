package zkhaider.com.cooleaf.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.cooleafapi.utils.APIConfig;
import zkhaider.com.cooleaf.ui.listeners.OnImageClickListener;
import zkhaider.com.cooleaf.ui.listeners.OnLongPressListener;
import zkhaider.com.cooleaf.ui.listeners.OnPostCardTouchListener;
import zkhaider.com.cooleaf.ui.roundedpicture.CircleTransform;

/**
 * Created by ZkHaider on 6/18/15.
 */
public class SearchItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = SearchItemAdapter.class.getSimpleName();
    private List<Event> mSearchEvents;
    private List<Post> mPosts;
    private boolean mIsMinified;
    private Context mContext;
    private OnPostCardTouchListener mOnPostCardTouchListener;
    private OnImageClickListener mOnImageClickListener;
    private OnLongPressListener mOnLongPressListener;

    public SearchItemAdapter(Context context, boolean isMinified) {
        this.mContext = context;
        this.mIsMinified = isMinified;
    }

    public void addPosts(List<Post> posts) {
        if (mPosts != null)
            mPosts.addAll(posts);
    }

    public Post getPost(int position) {
        return mPosts.get(position);
    }

    public List<Post> getComments(int position) {
        return mPosts.get(position).getComments();
    }

    public void setEvents(List<Event> events) {
        this.mSearchEvents = events;
    }

    public Event getEvent(int position) {
        return mSearchEvents.get(position);
    }

    public void setPosts(List<Post> posts) {
        this.mPosts = posts;
    }

    public void setOnPostCardTouchListener(OnPostCardTouchListener postCardTouchListener) {
        this.mOnPostCardTouchListener = postCardTouchListener;
    }

    public void setOnImageClickListener(OnImageClickListener imageClickListener) {
        this.mOnImageClickListener = imageClickListener;
    }

    public void setOnLongPressListener(OnLongPressListener onLongPressListener) {
        this.mOnLongPressListener = onLongPressListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,
                parent, false);

        return new SearchItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SearchItemViewHolder searchItemViewHolder = (SearchItemViewHolder) holder;

        if (mSearchEvents != null) {
            setEvent(position, searchItemViewHolder);
        } else if (mPosts != null) {
            setPost(position, searchItemViewHolder);
        }

    }

    @Override
    public int getItemCount() {
        if (mSearchEvents != null && mSearchEvents.size() >= 2 && mIsMinified)
            return 2;
        else if (mSearchEvents != null)
            return mSearchEvents.size();
        else if (mPosts != null && mPosts.size() >= 2 && mIsMinified)
            return 2;
        else if (mPosts != null)
            return mPosts.size();
        return 0;
    }

    public void addPost(Post post) {
        if (mPosts != null) {
            mPosts.add(0, post);
            notifyItemInserted(0);
        }
    }

    public void removePost(int position) {
        if (mPosts != null) {
            mPosts.remove(position);
            notifyItemRemoved(position);
        }
    }

    /******************************************************************************************
     *  Helper Methods to set an Event or Post to the Viewholder
     ******************************************************************************************/

    private void setEvent(int position, SearchItemViewHolder searchItemViewHolder) {
        // Grab the event
        Event searchEvent = mSearchEvents.get(position);

        // Go ahead and grab the image for the event
        String eventImageUrl = searchEvent.getLargePath();

        // Hook it into Picasso
        Picasso.with(mContext)
                .load(eventImageUrl)
                .fit()
                .centerCrop()
                .transform(new CircleTransform())
                .into(searchItemViewHolder.mItemImage);

        // Get the event name
        String eventName = searchEvent.getName();
        searchItemViewHolder.mItemName.setText(eventName);

        // Get the content
        String eventDescription = searchEvent.getDescription();
        searchItemViewHolder.mItemContent.setText(eventDescription);

        // Get the date
        String timeAgo = searchEvent.getTimeAgo();
        searchItemViewHolder.mItemDate.setText(timeAgo);
    }

    private void setPost(int position, SearchItemViewHolder searchItemViewHolder) {
        // Grab the post
        Post post = mPosts.get(position);

        // Grab the image for the post
        if (post.getUserPicture() != null) {
            String postImage = (APIConfig.getBaseAPIUrl() + post.getUserPicture().getOriginal());

            // Hook it into Picasso
            Picasso.with(mContext)
                    .load(postImage)
                    .fit()
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(searchItemViewHolder.mItemImage);
        }

        // Get the post name
        String postName = (post.getUser() != null) ? post.getUser().getName() : post.getUserName();
        searchItemViewHolder.mItemName.setText(postName);

        // Get the content for the post
        String postContent = post.getContent();
        searchItemViewHolder.mItemContent.setText(postContent);

        // Set the attachment
        if (post.getAttachment() != null) {
            searchItemViewHolder.itemView.getLayoutParams().height = mContext.getResources()
                    .getDimensionPixelSize(R.dimen.comment_attachment_height);

            searchItemViewHolder.mItemAttachmentImage.setVisibility(View.VISIBLE);
            String attachmentUrl = post.getAttachment().getVersions().getThumbURL();
            Picasso.with(mContext)
                    .load(attachmentUrl)
                    .fit()
                    .centerCrop()
                    .into(searchItemViewHolder.mItemAttachmentImage);
        } else {
            searchItemViewHolder.mItemAttachmentImage.setVisibility(View.GONE);
            searchItemViewHolder.itemView.getLayoutParams().height = mContext.getResources()
                    .getDimensionPixelSize(R.dimen.search_item_height);
        }

        // Get the date
        if (post.getUpdatedAt() != null) {
            if (post.getTimeAgo() != null) {
                String timeAgo = post.getTimeAgo();
                searchItemViewHolder.mItemDate.setText(timeAgo);
            }
        }

        // Get the number of comments
        if (post.getComments() != null) {
            int size = post.getComments().size();
            String commentSize = (size > 1) ? " Comments" : " Comment";
            searchItemViewHolder.mItemComments.setText(String.valueOf(size) + commentSize);
        } else {
            int commentCount = post.getCommentCount();
            String commentSize = (commentCount > 1) ? " Comments" : " Comment";
            searchItemViewHolder.mItemComments.setText(String.valueOf(commentCount) + commentSize);
        }
    }


    public class SearchItemViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private ImageView mItemImage;
        private TextView mItemName;
        private TextView mItemContent;
        private TextView mItemDate;
        private TextView mItemComments;
        private ImageView mItemAttachmentImage;

        public SearchItemViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
            initListeners(itemView);
        }

        private void initViews(View itemView) {
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.itemCard);
            mItemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            mItemName = (TextView) itemView.findViewById(R.id.itemName);
            mItemContent = (TextView) itemView.findViewById(R.id.itemContent);
            mItemComments = (TextView) itemView.findViewById(R.id.itemComments);
            mItemDate = (TextView) itemView.findViewById(R.id.itemDate);
            mItemAttachmentImage = (ImageView) itemView.findViewById(R.id.itemAttachmentImage);
            mItemAttachmentImage.setVisibility(View.GONE);
        }

        private void initListeners(View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPostCardTouchListener.onCardTap(v, getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnLongPressListener.onLongPress(getAdapterPosition());
                    return true;
                }
            });
            mItemAttachmentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnImageClickListener.previewImage(getAdapterPosition());
                }
            });
        }

    }

}