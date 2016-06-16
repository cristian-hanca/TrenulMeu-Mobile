package ro.trenulmeu.mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.managedrecyclerview.adapter.FilterableRecyclerViewAdapter;
import ro.trenulmeu.mobile.models.Station;

public class StationsAdapter extends FilterableRecyclerViewAdapter<Station, StationsAdapter.ViewHolder> {

    public StationsAdapter(List<Station> dataSet) {
        super(dataSet);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_station, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Station item = dataSet.get(position);

        holder.nameText.setText(item.getName());
        holder.dividerImage.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
    }

    public class ViewHolder extends FilterableRecyclerViewAdapter.ClickableViewHolder {

        TextView nameText;
        ImageView dividerImage;

        public ViewHolder(View itemView) {
            super(itemView);

            nameText = (TextView) itemView.findViewById(R.id.item_station_name);
            dividerImage = (ImageView) itemView.findViewById(R.id.item_station_divider);

            itemView.setTag(this);
        }

    }

}
