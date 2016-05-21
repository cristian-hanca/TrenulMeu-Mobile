package ro.trenulmeu.mobile.filters.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;

import ro.trenulmeu.mobile.filters.R;
import ro.trenulmeu.mobile.filters.models.CheckItem;

/**
 * Adapter for MultiSelect Items.
 */
public class MultiSelectAdapter<T> extends RecyclerView.Adapter<MultiSelectAdapter<T>.ViewHolder> {

    private final List<CheckItem<T>> dataSet;

    public MultiSelectAdapter(List<CheckItem<T>> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_multiselect, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CheckItem<T> item = dataSet.get(position);

        holder.checkBox.setText(item.getName());
        holder.checkBox.setChecked(item.isCheck());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            checkBox = (CheckBox) itemView.findViewById(R.id.check);
            checkBox.setOnClickListener(this);
            itemView.setTag(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            boolean checked = checkBox.isChecked();
            dataSet.get(this.getAdapterPosition()).setCheck(checked);
            //checkBox.setChecked(!checked);
        }

    }

}
