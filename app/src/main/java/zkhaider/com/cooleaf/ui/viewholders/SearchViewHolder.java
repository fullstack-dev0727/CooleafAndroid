package zkhaider.com.cooleaf.ui.viewholders;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchQuery;
import zkhaider.com.cooleaf.ui.helpers.UIConstants;
import zkhaider.com.cooleaf.ui.listeners.OnItemTouchListener;
import zkhaider.com.cooleaf.ui.listeners.OnSearchItemTouchListener;
import zkhaider.com.cooleaf.ui.roundedpicture.CircleTransform;

/**
 * Created by ZkHaider on 10/5/15.
 */
public class SearchViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    private ImageView mItemImage;
    private TextView mItemName;
    private TextView mItemContent;
    private TextView mItemDate;
    private TextView mItemComments;
    private ImageView mItemAttachmentImage;

    private OnSearchItemTouchListener mOnSearchItemTouchListener;

    public SearchViewHolder(View itemView) {
        super(itemView);
        initViews(itemView);
        initListener(itemView);
    }

    public void setOnSearchItemTouchListener(OnSearchItemTouchListener itemTouchListener) {
        this.mOnSearchItemTouchListener = itemTouchListener;
    }

    public void bind(SearchQuery query, Context context) {
        mContext = context;
        initializeQuery(query);
    }

    private void initViews(View itemView) {
        mItemImage = (ImageView) itemView.findViewById(R.id.itemImage);
        mItemName = (TextView) itemView.findViewById(R.id.itemName);
        mItemContent = (TextView) itemView.findViewById(R.id.itemContent);
        mItemComments = (TextView) itemView.findViewById(R.id.itemComments);
        mItemDate = (TextView) itemView.findViewById(R.id.itemDate);
        mItemAttachmentImage = (ImageView) itemView.findViewById(R.id.itemAttachmentImage);
        mItemAttachmentImage.setVisibility(View.GONE);
    }

    private void initListener(View itemView) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSearchItemTouchListener != null)
                    mOnSearchItemTouchListener.onCardTap(v, getAdapterPosition());
            }
        });
    }

    private void initializeQuery(SearchQuery query) {

        // Set the image
        Picasso.with(mContext)
                .load(query.getImageUrl())
                .transform(new CircleTransform())
                .fit()
                .centerCrop()
                .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_profile_placeholder))
                .into(mItemImage);

        // Set tags
        if (query.getTags() != null && query.getTags().size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (String tag : query.getTags()) {
                builder.append("#");
                builder.append(tag);
                builder.append(" ");
            }
            String tags = builder.toString();
            mItemContent.setText(tags);
        }

        String name = query.getName();
        String type = query.getType();

        switch (query.getType()) {
            case UIConstants.USER:
                // Set the name and type
                mItemName.setText(name);
                mItemDate.setText(type);

                // Set tags
                if (query.getTags() != null && query.getTags().size() > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (String tag : query.getTags()) {
                        builder.append("#");
                        builder.append(tag);
                        builder.append(" ");
                    }
                    String tags = builder.toString();
                    mItemContent.setText(tags);
                }
                break;
            case UIConstants.EVENT:
                // Set the name and type
                mItemName.setText(name);
                mItemDate.setText(type);

                // Set content
                String content = query.getContent();
                mItemContent.setText(content);

                // Set tags
                if (query.getTags() != null && query.getTags().size() > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (String tag : query.getTags()) {
                        builder.append("#");
                        builder.append(tag);
                        builder.append(" ");
                    }
                    String tags = builder.toString();
                    mItemComments.setText(tags);
                }
                break;
            case UIConstants.GROUP:
                // Set the name and type
                mItemName.setText(name);
                mItemDate.setText(type);

                // Set tags
                if (query.getTags() != null && query.getTags().size() > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (String tag : query.getTags()) {
                        builder.append("#");
                        builder.append(tag);
                        builder.append(" ");
                    }
                    String tags = builder.toString();
                    mItemComments.setText(tags);
                }
                break;
            case UIConstants.FEED:
                // Set the name and type
                mItemName.setText(name);
                mItemDate.setText(type);

                // Set content
                String feedContent = query.getContent();
                mItemContent.setText(feedContent);

                // Set tags
                if (query.getTags() != null && query.getTags().size() > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (String tag : query.getTags()) {
                        builder.append("#");
                        builder.append(tag);
                        builder.append(" ");
                    }
                    String tags = builder.toString();
                    mItemComments.setText(tags);
                }
                break;
            default:
                break;
        }
    }

}
