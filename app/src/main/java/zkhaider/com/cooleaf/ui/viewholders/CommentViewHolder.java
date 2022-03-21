package zkhaider.com.cooleaf.ui.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Post;
import zkhaider.com.cooleaf.cooleafapi.utils.APIConfig;
import zkhaider.com.cooleaf.ui.listeners.OnImageClickListener;
import zkhaider.com.cooleaf.ui.listeners.OnLongPressListener;
import zkhaider.com.cooleaf.ui.listeners.OnPostCardTouchListener;
import zkhaider.com.cooleaf.ui.roundedpicture.CircleTransform;

/**
 * Created by ZkHaider on 10/6/15.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    // Comment
    private ImageView mItemImage;
    private TextView mItemName;
    private TextView mItemContent;
    private TextView mItemDate;
    private TextView mItemComments;
    private ImageView mItemAttachmentImage;
    private TextView mItemShowAttachment;

    // Listeners
    private OnPostCardTouchListener mOnPostCardTouchListener;
    private OnImageClickListener mOnImageClickListener;
    private OnLongPressListener mOnLongPressListener;

    // Variables for View
    private int mOriginalHeight = 0;
    private boolean mIsViewExpanded = false;

    public CommentViewHolder(View itemView) {
        super(itemView);
        initCommentViews(itemView);
    }

    public void setOnPostCardTouchListener(OnPostCardTouchListener touchListener) {
        this.mOnPostCardTouchListener = touchListener;
    }

    public void setOnImageClickListener(OnImageClickListener clickListener) {
        this.mOnImageClickListener = clickListener;
    }

    public void setOnLongPressListener(OnLongPressListener onLongPressListener) {
        this.mOnLongPressListener = onLongPressListener;
    }

    private void initCommentViews(View view) {
        mItemImage = (ImageView) view.findViewById(R.id.itemImage);
        mItemName = (TextView) view.findViewById(R.id.itemName);
        mItemContent = (TextView) view.findViewById(R.id.itemContent);
        mItemComments = (TextView) view.findViewById(R.id.itemComments);
        mItemDate = (TextView) view.findViewById(R.id.itemDate);
        mItemAttachmentImage = (ImageView) view.findViewById(R.id.itemAttachmentImage);
        mItemShowAttachment = (TextView) view.findViewById(R.id.showItemAttachment);
        mItemShowAttachment.setVisibility(View.INVISIBLE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPostCardTouchListener != null)
                    mOnPostCardTouchListener.onCardTap(v, getAdapterPosition());
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
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

    public void bind(Post post, Context context) {
        mContext = context;
        initializePost(post);
    }

    private void initializePost(Post post) {
        // Set Image
        String imageUrl = APIConfig.getBaseAPIUrl() + post.getUserPicture().getOriginal();
        Picasso.with(mContext)
                .load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new CircleTransform())
                .into(mItemImage);

        // Set Content
        String content = post.getContent();
        mItemContent.setText(content);

        // Set the image
        if (post.getAttachment() != null) {

            this.itemView.getLayoutParams().height =
                    mContext.getResources().getDimensionPixelSize(R.dimen.comment_attachment_height);

            mItemAttachmentImage.setVisibility(View.VISIBLE);
            String attachmentUrl = APIConfig.getBaseAPIUrl() + post.getAttachment().getOriginal();
            Picasso.with(mContext)
                    .load(attachmentUrl)
                    .fit()
                    .centerCrop()
                    .into(mItemAttachmentImage);
        } else {
            mItemShowAttachment.setVisibility(View.GONE);
            mItemAttachmentImage.setVisibility(View.GONE);
        }

        // Convert date to time ago and set it to textview
        String date = post.getTimeAgo();
        mItemDate.setText(date);
    }

}
