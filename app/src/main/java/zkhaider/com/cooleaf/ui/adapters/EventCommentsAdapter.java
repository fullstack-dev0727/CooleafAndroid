package zkhaider.com.cooleaf.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.ui.listeners.OnImageClickListener;
import zkhaider.com.cooleaf.ui.listeners.OnLongPressListener;
import zkhaider.com.cooleaf.ui.listeners.OnPostCardTouchListener;
import zkhaider.com.cooleaf.ui.viewholders.CommentViewHolder;

/**
 * Created by ZkHaider on 10/7/15.
 */
public class EventCommentsAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private Context mContext;
    private List<Post> mPosts;
    private OnPostCardTouchListener mOnPostCardTouchListener;
    private OnImageClickListener mOnImageClickListener;
    private OnLongPressListener mOnLongPressListener;

    public EventCommentsAdapter(Context context) {
        this.mContext = context;
    }

    public void setComments(List<Post> posts) {
        if (posts != null) {
            this.mPosts = new ArrayList<>(posts);
        }
    }

    public void addCommments(List<Post> posts) {
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
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,
                parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        // Set listeners
        holder.setOnPostCardTouchListener(mOnPostCardTouchListener);
        holder.setOnImageClickListener(mOnImageClickListener);
        holder.setOnLongPressListener(mOnLongPressListener);

        // Get the post
        Post post = mPosts.get(position);

        // Bind post and context
        holder.bind(post, mContext);
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
        return post;
    }

    public void addItem(Post post) {
        mPosts.add(post);
        int index = mPosts.size() - 1;
        notifyItemInserted(index);
    }

    public void addItem(int position, Post post) {
        mPosts.add(position, post);
        notifyItemInserted(position);
    }

    public Post getComment(int position) {
        return mPosts.get(position);
    }

}
