package zkhaider.com.cooleaf.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.SearchQuery;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.ui.listeners.OnItemTouchListener;
import zkhaider.com.cooleaf.ui.listeners.OnSearchItemTouchListener;
import zkhaider.com.cooleaf.ui.viewholders.SearchViewHolder;

/**
 * Created by ZkHaider on 10/4/15.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private OnSearchItemTouchListener mOnSearchItemTouchListener;
    private Context mContext;
    private List<SearchQuery> mQueries = new ArrayList<>();

    public SearchAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,
                parent, false);
        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        if (mQueries != null) {
            final SearchQuery query = mQueries.get(position);
            holder.setOnSearchItemTouchListener(mOnSearchItemTouchListener);
            holder.bind(query, mContext);
        }
    }

    @Override
    public int getItemCount() {
        return mQueries.size();
    }

    public void clearData() {
        int size = this.mQueries.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.mQueries.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void setOnSearchItemTouchListener(OnSearchItemTouchListener onSearchItemTouchListener) {
        this.mOnSearchItemTouchListener = onSearchItemTouchListener;
    }

    public void addQueries(List<SearchQuery> queries) {
        mQueries.addAll(queries);
    }

    public SearchQuery getQuery(int position) {
        return mQueries.get(position);
    }

    public void setQueries(List<SearchQuery> queries) {
        mQueries = new ArrayList<>(queries);
    }

}
