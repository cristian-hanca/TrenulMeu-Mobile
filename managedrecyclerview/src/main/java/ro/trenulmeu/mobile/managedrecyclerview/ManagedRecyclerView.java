package ro.trenulmeu.mobile.managedrecyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ManagedRecyclerView extends LinearLayout {

    private TextView emptyText;
    private RecyclerView listView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.AdapterDataObserver observer;

    public ManagedRecyclerView(Context context) {
        super(context);
        init();
    }

    public ManagedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ManagedRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.control_recyclerview, this);

        emptyText = (TextView) v.findViewById(R.id.crv_text);
        listView = (RecyclerView) v.findViewById(R.id.crv_list);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateUI();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (this.adapter != null) {
            this.adapter.unregisterAdapterDataObserver(this.observer);
        }

        this.adapter = adapter;

        if (this.adapter != null) {
            this.observer = new UpdateAdapterDataObserver();
            this.adapter.registerAdapterDataObserver(this.observer);
        }

        listView.setAdapter(this.adapter);
        updateUI();
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        this.manager = manager;
        listView.setLayoutManager(manager);
        updateUI();
    }

    public void updateUI() {
        if (adapter == null || adapter.getItemCount() == 0) {
            emptyText.setVisibility(VISIBLE);
            listView.setVisibility(GONE);
        } else {
            emptyText.setVisibility(GONE);
            listView.setVisibility(VISIBLE);
        }
    }

    public TextView getEmptyView() {
        return emptyText;
    }

    public RecyclerView getListView() {
        return listView;
    }

    /**
     * A custom RecyclerView.AdapterDataObserver that just calls UpdateUI on any Data Change.
     */
    private class UpdateAdapterDataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            updateUI();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            updateUI();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            updateUI();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            updateUI();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            updateUI();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            updateUI();
        }
    }

}
