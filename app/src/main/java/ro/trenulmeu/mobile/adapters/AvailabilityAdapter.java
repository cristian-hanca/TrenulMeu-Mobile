package ro.trenulmeu.mobile.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.managedrecyclerview.adapter.FilterableRecyclerViewAdapter;
import ro.trenulmeu.mobile.models.TrainAvailability;

public class AvailabilityAdapter extends
        FilterableRecyclerViewAdapter<TrainAvailability, AvailabilityAdapter.ViewHolder> {

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");

    public AvailabilityAdapter(List<TrainAvailability> dataSet) {
        super(dataSet);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_availability, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TrainAvailability item = dataSet.get(position);

        holder.headerRow.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        holder.divider.setVisibility(position != getItemCount() - 1 ? View.VISIBLE : View.GONE);

        holder.fromText.setText(sdf.format(item.getFrom()));
        holder.toText.setText(sdf.format(item.getTo()));

        char[] days = item.getDays().toCharArray();
        for (int i = 0; i < 7; i++) {
            holder.days[i].setImageResource(days[i] == '1'
                    ? R.drawable.yes : R.drawable.no);
        }
    }

    public class ViewHolder extends FilterableRecyclerViewAdapter.ClickableViewHolder {

        TableRow headerRow;
        TextView fromText;
        TextView toText;
        ImageView[] days;
        View divider;

        public ViewHolder(View itemView) {
            super(itemView);

            headerRow = (TableRow) itemView.findViewById(R.id.av_header);
            fromText = (TextView) itemView.findViewById(R.id.av_from);
            toText = (TextView) itemView.findViewById(R.id.av_to);
            divider = itemView.findViewById(R.id.av_divider);

            days = new ImageView[7];
            days[0] = (ImageView) itemView.findViewById(R.id.av_1);
            days[1] = (ImageView) itemView.findViewById(R.id.av_2);
            days[2] = (ImageView) itemView.findViewById(R.id.av_3);
            days[3] = (ImageView) itemView.findViewById(R.id.av_4);
            days[4] = (ImageView) itemView.findViewById(R.id.av_5);
            days[5] = (ImageView) itemView.findViewById(R.id.av_6);
            days[6] = (ImageView) itemView.findViewById(R.id.av_7);
        }

    }
}
