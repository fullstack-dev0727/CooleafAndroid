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
import zkhaider.com.cooleaf.ui.listeners.OnPostCardTouchListener;
import zkhaider.com.cooleaf.ui.roundedpicture.CircleTransform;

/**
 * Created by ZkHaider on 10/6/15.
 */
public class FeedHeaderViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    // Header
    private ImageView mFeedOwnerImage;
    private TextView mFeedContent;
    private TextView mFeedTitle;
    private TextView mFeedDate;
    private TextView mFeedComments;
    private ImageView mAttachmentImage;
    private TextView mShowAttachment;

    // Listeners
    private OnPostCardTouchListener mOnPostCardTouchListener;
    private OnImageClickListener mOnImageClickListener;

    private int mNumberComments;

    public FeedHeaderViewHolder(View itemView) {
        super(itemView);
        initHeaderViews(itemView);
    }

    public void setOnPostCardTouchListener(OnPostCardTouchListener touchListener) {
        this.mOnPostCardTouchListener = touchListener;
    }

    public void setOnImageClickListener(OnImageClickListener clickListener) {
        this.mOnImageClickListener = clickListener;
    }

    private void initHeaderViews(View view) {
        mFeedOwnerImage = (ImageView) view.findViewById(R.id.feedOwnerImage);
        mFeedTitle = (TextView) view.findViewById(R.id.feedOwnerTitle);
        mFeedContent = (TextView) view.findViewById(R.id.feedOwnerContent);
        mFeedDate = (TextView) view.findViewById(R.id.feedDate);
        mFeedComments = (TextView) view.findViewById(R.id.feedComments);
        mAttachmentImage = (ImageView) view.findViewById(R.id.attachmentImage);
        mShowAttachment = (TextView) view.findViewById(R.id.showAttachment);
        mShowAttachment.setVisibility(View.INVISIBLE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPostCardTouchListener.onCardTap(v, getAdapterPosition());
            }
        });

        mAttachmentImage.setOnClickListener(new View.OnClickListener() {
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
                .into(mFeedOwnerImage);

        mFeedTitle.setText(post.getUserName());

            // Set Content
            String content = post.getContent();
            mFeedContent.setText(content);

            // Set the image
            if (post.getAttachment() != null) {

                this.itemView.getLayoutParams().height =
                        mContext.getResources().getDimensionPixelSize(R.dimen.post_attachment_height);

                mAttachmentImage.setVisibility(View.VISIBLE);
                String attachmentUrl = APIConfig.getBaseAPIUrl() + post.getAttachment().getOriginal();
                Picasso.with(mContext)
                        .load(attachmentUrl)
                        .fit()
                    .centerCrop()
                    .into(mAttachmentImage);
        } else {
            mShowAttachment.setVisibility(View.GONE);
            mAttachmentImage.setVisibility(View.GONE);
        }

        // Convert date to time ago and set it to textview
        String date = post.getTimeAgo();
        mFeedDate.setText(date);

        // Set Number of Comment
        int sizeComments = post.getComments().size();
        mNumberComments = sizeComments;
        String numberComments = (sizeComments > 1) ?
                String.valueOf(sizeComments) + " Comments" :
                String.valueOf(sizeComments) + " Comment";
        mFeedComments.setText(numberComments);
    }

    public TextView getFeedCommentsTextView() {
        return mFeedComments;
    }

    public int getFeedComments() {
        return mNumberComments;
    }

    public void setFeedComments(int numberComments) {
        String commentsString = (numberComments > 1) ?
                String.valueOf(numberComments) + " Comments" :
                String.valueOf(numberComments) + " Comment";
        mFeedComments.setText(numberComments);
    }

}
