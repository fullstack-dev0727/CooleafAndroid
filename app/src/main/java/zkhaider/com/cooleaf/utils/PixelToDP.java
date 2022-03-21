package zkhaider.com.cooleaf.utils;

import android.content.Context;

/**
 * Created by Haider on 3/15/2015.
 */
public class PixelToDP {

    private Context mContext;
    private int mDp;

    public PixelToDP(Context context, int sizeInDp) {
        this.mContext = context;
        float scale = context.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
        this.mDp = dpAsPixels;
    }

    public int getDp() {
        return mDp;
    }


}
