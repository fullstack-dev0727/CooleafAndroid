package zkhaider.com.cooleaf.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.ui.listeners.OnImageClickListener;
import zkhaider.com.cooleaf.ui.listeners.OnLongPressListener;
import zkhaider.com.cooleaf.ui.listeners.OnPostCardTouchListener;
import zkhaider.com.cooleaf.ui.viewholders.CommentViewHolder;
import zkhaider.com.cooleaf.ui.viewholders.FeedHeaderViewHolder;

/**
 * Created by ZkHaider on 10/6/15.
 */
public class FeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_COMMENT_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context mContext;
    private List<Post> mPosts;
    private OnPostCardTouchListener mOnPostCardTouchListener;
    private OnImageClickListener mOnImageClickListener;
    private OnLongPressListener mOnLongPressListener;
    private TextView mHeaderComments;

    private int mNumComments;

    public FeedsAdapter(Context context) {
        this.mContext = context;
    }

    public void setPosts(List<Post> posts) {
        if (posts != null) {
            this.mPosts = new ArrayList<>(posts);
        }
    }

    public void addPosts(List<Post> posts)
    {
        mPosts.addAll(posts);
    }

    public void setOnPostCardTouchListener(OnPostCardTouchListener onPostCardTouchListener) {
        this.mOnPostCardTouchListener = onPostCardTouchListener;
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.mOnImageClickListener = onImageClickListener;
    }

    public void setOnLongPressListener(OnLongPressListener onLongPressListener) {
        this.mOnLongPressListener = onLongPressListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_COMMENT_HEADER: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,
                        parent, false);
                return new FeedHeaderViewHolder(v);
            }
            case TYPE_ITEM: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,
                        parent, false);
                return new CommentViewHolder(v);
            }
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case TYPE_COMMENT_HEADER: {
                // Get FeedHeaderViewHolder
                FeedHeaderViewHolder fhv = (FeedHeaderViewHolder) holder;

                // Set listeners
                fhv.setOnPostCardTouchListener(mOnPostCardTouchListener);
                fhv.setOnImageClickListener(mOnImageClickListener);

                // Get the post
                Post post = mPosts.get(position);

                // Bind post and context
                fhv.bind(post, mContext);

                mHeaderComments = fhv.getFeedCommentsTextView();
                mNumComments = fhv.getFeedComments();

                break;
            }
            case TYPE_ITEM: {
                // Get FeedCommentViewHolder
                CommentViewHolder fcv = (CommentViewHolder) holder;

                // Set listeners
                fcv.setOnPostCardTouchListener(mOnPostCardTouchListener);
                fcv.setOnImageClickListener(mOnImageClickListener);
                fcv.setOnLongPressListener(mOnLongPressListener);

                // Get the post
                Post post = mPosts.get(position);

                // Bind post and context
                fcv.bind(post, mContext);

                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_COMMENT_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        if (mPosts != null)
            return mPosts.size();
        else
            return 0;
    }

    public void updateItem(int position, Post comment) {
        mPosts.remove(position);
        notifyItemRemoved(position);
        mPosts.add(position, comment);
        notifyItemInserted(position);
    }

    public Post removeItem(int position) {
        final Post post = mPosts.remove(position);
        notifyItemRemoved(position);
        mNumComments--;
        String numberComments = (mNumComments > 1) ?
                String.valueOf(mNumComments) + " Comments" :
                String.valueOf(mNumComments) + " Comment";
        mHeaderComments.setText(numberComments);
        return post;
    }

    public void addItem(Post post) {
        mPosts.add(post);
        int index = mPosts.size() - 1;
        notifyItemInserted(index);
        mNumComments++;
        String numberComments = (mNumComments > 1) ?
                String.valueOf(mNumComments) + " Comments" :
                String.valueOf(mNumComments) + " Comment";
        mHeaderComments.setText(numberComments);
    }

    public void addItem(int position, Post post) {
        mPosts.add(position, post);
        notifyItemInserted(position);
        mNumComments++;
        String numberComments = (mNumComments > 1) ?
                String.valueOf(mNumComments) + " Comments" :
                String.valueOf(mNumComments) + " Comment";
        mHeaderComments.setText(numberComments);
    }

    public Post getPost(int position) {
        return mPosts.get(position);
    }

}
