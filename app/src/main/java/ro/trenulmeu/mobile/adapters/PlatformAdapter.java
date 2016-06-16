package ro.trenulmeu.mobile.adapters;

import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.managedrecyclerview.adapter.FilterableRecyclerViewAdapter;
import ro.trenulmeu.mobile.models.Train;
import ro.trenulmeu.mobile.models.TrainPath;

public class PlatformAdapter extends FilterableRecyclerViewAdapter<TrainPath, PlatformAdapter.ViewHolder> {

    private boolean showArrival;

    public PlatformAdapter(List<TrainPath> dataSet) {
        super(dataSet);
    }

    public void setShowArrival(boolean showArrival) {
        this.showArrival = showArrival;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_platform, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TrainPath item = dataSet.get(position);
        boolean isStop = item.getIsStop();

        Train train = item.getTrain();
        if (isStop) {
            holder.typeImage.setImageResource(AppContext.activity.getResources().getIdentifier(
                    "type" + train.getTypeId(), "drawable", AppContext.activity.getPackageName()));
        } else {
            holder.typeImage.setImageResource(AppContext.activity.getResources().getIdentifier(
                    "type" + train.getTypeId() + "bw", "drawable", AppContext.activity.getPackageName()));
        }
        holder.nameText.setText(train.getName());
        holder.stationText.setText(showArrival ? train.getFromName() : train.getToName());

        if (position == 0) {
            holder.headerRow.setVisibility(View.VISIBLE);
            holder.headerLabel.setText(showArrival ? R.string.from : R.string.to);
        } else {
            holder.headerRow.setVisibility(View.GONE);
        }

        if (item.getKm() == 0) {
            holder.inText.setText(R.string.empty_time);
            holder.outText.setText(item.getDisplayDepart().toString());
        } else if (item.getIsFinalStop()) {
            holder.inText.setText(item.getDisplayArrive().toString());
            holder.outText.setText(R.string.empty_time);
        } else {
            holder.inText.setText(item.getDisplayArrive().toString());
            holder.outText.setText(item.getDisplayDepart().toString());
        }

        holder.dividerImage.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
        setColor(holder, isStop ? R.color.text : R.color.text_muted);
    }

    private void setColor(ViewHolder holder, @ColorRes int id) {
        holder.nameText.setTextColor(ContextCompat.getColor(AppContext.activity, id));
        holder.stationText.setTextColor(ContextCompat.getColor(AppContext.activity, id));
        holder.inText.setTextColor(ContextCompat.getColor(AppContext.activity, id));
        holder.outText.setTextColor(ContextCompat.getColor(AppContext.activity, id));
    }

    public class ViewHolder extends FilterableRecyclerViewAdapter.ClickableViewHolder {

        TableRow headerRow;
        ImageView typeImage;
        TextView nameText;
        TextView stationText;
        TextView inText;
        TextView outText;
        ImageView dividerImage;
        TextView headerLabel;

        public ViewHolder(View itemView) {
            super(itemView);

            headerRow = (TableRow) itemView.findViewById(R.id.item_platform_header);
            typeImage = (ImageView) itemView.findViewById(R.id.item_platform_type);
            nameText = (TextView) itemView.findViewById(R.id.item_platform_name);
            stationText = (TextView) itemView.findViewById(R.id.item_platform_station);
            inText = (TextView) itemView.findViewById(R.id.item_platform_in);
            outText = (TextView) itemView.findViewById(R.id.item_platform_out);
            dividerImage = (ImageView) itemView.findViewById(R.id.item_platform_divider);
            headerLabel = (TextView) itemView.findViewById(R.id.item_platform_header_txt);

            itemView.setTag(this);
        }

    }

}
