package zkhaider.com.cooleaf.ui.listeners;

import android.view.View;
import android.widget.TextView;

/**
 * Created by ZkHaider on 7/17/15.
 */
public interface OnItemTouchListener {

    void onCardViewTap(View view, int position);
    void onJoin(TextView view, int position);
    void onAddComment(View view, int position);

}
