package zkhaider.com.cooleaf.ui.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.ParentTag;
import zkhaider.com.cooleaf.cooleafapi.entities.Tag;
import zkhaider.com.cooleaf.ui.helpers.StructureUIHelper;

/**
 * Created by Haider on 2/4/2015.
 */
public class InformationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = InformationAdapter.class.getSimpleName();

    private List<ParentTag> mParentTags;
    private Context mContext;

    public InformationAdapter(Context context) {
        mContext = context;
    }

    protected static boolean[] mOpenContainers;


    public void setStructures(List<ParentTag> parentTags) {
        mParentTags = parentTags;
        mOpenContainers = new boolean[parentTags.size()];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.structure_item,
                viewGroup, false);
        return new StructureViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        ((StructureViewHolder) viewHolder).structureTagsLayout.setVisibility(View.GONE);
        showStructure(((StructureViewHolder) viewHolder), position);
    }

    private void showStructure(final InformationAdapter.StructureViewHolder viewHolder, int position) {

        ParentTag parentTag = mParentTags.get(position);

        viewHolder.structureName.setText(parentTag.getName());

        List<Tag> tags = parentTag.getAllTags();
        LinearLayout rowLayout = new LinearLayout(mContext);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        StructureUIHelper uiHelper = new StructureUIHelper(mContext, tags, viewHolder.structureTagsLayout);
        uiHelper.initStructures();
        if (mOpenContainers[position])
            viewHolder.structureTagsLayout.setVisibility(View.VISIBLE);
        else
            viewHolder.structureTagsLayout.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        if (mParentTags != null)
            return mParentTags.size();
        return 0;
    }

    public static class StructureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // For structures
        protected TextView structureName;
        protected ImageView arrowImageView;
        protected LinearLayout structureTagsLayout;

        private int mOriginalHeight = 0;
        private boolean mIsViewExpanded = false;

        public StructureViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            structureName = (TextView) v.findViewById(R.id.structureName);
            structureTagsLayout = (LinearLayout) v.findViewById(R.id.structureTagsLayout);
            arrowImageView = (ImageView) v.findViewById(R.id.arrowButton);
            structureTagsLayout.setVisibility(View.GONE);
        }


        private void rotateDown() {
            RotateAnimation anim = new RotateAnimation(0f, 90f, 0f, 0f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(200);
            anim.setFillAfter(true);

            arrowImageView.startAnimation(anim);
        }

        private void rotateUp() {
            RotateAnimation anim = new RotateAnimation(90f, 0f, 0f, 0f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(200);
            anim.setFillAfter(false);

            arrowImageView.startAnimation(anim);
        }

        private void animateUp() {
            Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out

            a.setDuration(200);
            // Set a listener to the animation and configure onAnimationEnd
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    structureTagsLayout.setVisibility(View.GONE);
                    structureTagsLayout.setEnabled(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            // Set the animation on the custom view
            structureTagsLayout.startAnimation(a);
        }

        private void setOriginalHeight(View view) {
            if (mOriginalHeight == 0) {
                mOriginalHeight = view.getHeight();
            }
        }

        @Override
        public void onClick(final View view) {
            setOriginalHeight(view);
            // Declare a ValueAnimator object

            ValueAnimator valueAnimator;
            if (!mOpenContainers[getAdapterPosition()]) {
                mIsViewExpanded = true;
                mOpenContainers[getAdapterPosition()] = true;
                rotateDown();
                structureTagsLayout.setVisibility(View.VISIBLE);
                valueAnimator = ValueAnimator.ofInt(mOriginalHeight, mOriginalHeight + (int) (structureTagsLayout.getChildCount() * mOriginalHeight * .7)); // These values in this method can be changed to expand however much you like
            } else {
                mIsViewExpanded = false;
                mOpenContainers[getAdapterPosition()] = false;
                rotateUp();
                animateUp();
                valueAnimator = ValueAnimator.ofInt(mOriginalHeight + (int) (mOriginalHeight), mOriginalHeight);
            }

            valueAnimator.setDuration(200);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    view.getLayoutParams().height = value.intValue();
                    view.requestLayout();
                }
            });
            valueAnimator.start();
        }
    }

}