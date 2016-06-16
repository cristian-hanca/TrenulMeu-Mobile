package ro.trenulmeu.mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.helpers.TimeHelpers;
import ro.trenulmeu.mobile.managedrecyclerview.adapter.FilterableRecyclerViewAdapter;
import ro.trenulmeu.mobile.models.Train;
import ro.trenulmeu.mobile.timespan.TimeSpan;

public class TrainsAdapter extends FilterableRecyclerViewAdapter<Train, TrainsAdapter.ViewHolder> {

    public TrainsAdapter(List<Train> dataSet) {
        super(dataSet);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_train, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Train item = dataSet.get(position);

        holder.typeImage.setImageResource(AppContext.activity.getResources().getIdentifier(
                "type" + item.getTypeId(), "drawable", AppContext.activity.getPackageName()));
        holder.nameText.setText(item.getName());
        holder.fromText.setText(item.getFromName());
        holder.toText.setText(item.getToName());
        holder.fromTimeText.setText(TimeHelpers.getCorrectFrom(item).toString());
        holder.toTimeText.setText(TimeHelpers.getCorrectTo(item).subtract(TimeSpan.TimeUnits.DAY).toString());
        holder.dividerImage.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
    }

    public class ViewHolder extends FilterableRecyclerViewAdapter.ClickableViewHolder {

        ImageView typeImage;
        TextView nameText;
        TextView fromText;
        TextView toText;
        TextView fromTimeText;
        TextView toTimeText;
        ImageView dividerImage;

        public ViewHolder(View itemView) {
            super(itemView);

            typeImage = (ImageView) itemView.findViewById(R.id.item_train_type);
            nameText = (TextView) itemView.findViewById(R.id.item_train_name);
            fromText = (TextView) itemView.findViewById(R.id.item_train_from);
            toText = (TextView) itemView.findViewById(R.id.item_train_to);
            fromTimeText = (TextView) itemView.findViewById(R.id.item_train_from_time);
            toTimeText = (TextView) itemView.findViewById(R.id.item_train_to_time);
            dividerImage = (ImageView) itemView.findViewById(R.id.item_train_divider);

            itemView.setTag(this);
        }

    }

}
