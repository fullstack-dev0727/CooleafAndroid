package zkhaider.com.cooleaf.ui.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Tag;

/**
 * Created by Haider on 3/14/2015.
 */
public class StructureUIHelper {

    private final static String TAG = StructureUIHelper.class.getSimpleName();
    private Context mContext;
    private List<Tag> mTags;
    private LinearLayout mLinearLayout;
    private Resources resources;

    public StructureUIHelper(Context context, List<Tag> tags, LinearLayout linearLayout) {
        this.mContext = context;
        this.mTags = tags;
        this.mLinearLayout = linearLayout;
        resources = mContext.getResources();
    }

    public void initStructures() {
        mLinearLayout.removeAllViews();

        int lineCharacterCount = 0;

        List<Tag> tempTags = mTags;
        if (tempTags != null) {
            LinearLayout rowLayout = new LinearLayout(mContext);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            for (Tag tag : tempTags) {
                if (tag.getActive()) {
                    TextView tv = new TextView(mContext);
                    tv.setId(tag.getId());
                    lineCharacterCount += tag.getName().length() + 4;
                    tv.setText(tag.getName());
                    tv.setPadding(15, 9, 15, 9);
                    tv.setTextColor(mContext.getResources().getColor(R.color.cooleafWhite));

                    if (android.os.Build.VERSION.SDK_INT > 15)
                        tv.setBackground(mContext.getResources().getDrawable(R.drawable.btn_rounded));
                    else
                        tv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_rounded));

                    if(lineCharacterCount > 35)
                    {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        mLinearLayout.addView(rowLayout, params);

                        rowLayout = new LinearLayout(mContext);
                        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                        lineCharacterCount = 0;
                    }
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 10, 0, 10);
                    rowLayout.addView(tv, params);
                }
            }
            if (tempTags.size() > 0) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mLinearLayout.addView(rowLayout, params);
                rowLayout = new LinearLayout(mContext);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            }

            mLinearLayout.invalidate();
        }
    }

}
