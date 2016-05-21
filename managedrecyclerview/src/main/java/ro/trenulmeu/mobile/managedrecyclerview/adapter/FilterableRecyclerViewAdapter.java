package ro.trenulmeu.mobile.managedrecyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class FilterableRecyclerViewAdapter<T, VH
        extends FilterableRecyclerViewAdapter.ClickableViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> dataSet;
    private RecyclerViewClickListener itemListener = null;

    public FilterableRecyclerViewAdapter(List<T> dataSet) {
        this.dataSet = dataSet;
    }

    public void setItemClickListener(RecyclerViewClickListener itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(VH holder, int position);

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public T getItem(int position) {
        return dataSet.get(position);
    }

    public T removeItem(int position) {
        final T model = dataSet.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, T model) {
        dataSet.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final T model = dataSet.remove(fromPosition);
        dataSet.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<T> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<T> newModels) {
        for (int i = dataSet.size() - 1; i >= 0; i--) {
            final T model = dataSet.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<T> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final T model = newModels.get(i);
            if (!dataSet.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<T> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final T model = newModels.get(toPosition);
            final int fromPosition = dataSet.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }


    public class ClickableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ClickableViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemListener != null) {
                itemListener.itemClicked(v, this.getAdapterPosition());
            }
        }

    }

}
