package ro.trenulmeu.mobile.adapters;

import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.common.collect.Lists;

import java.text.DecimalFormat;
import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.managedrecyclerview.adapter.FilterableRecyclerViewAdapter;
import ro.trenulmeu.mobile.models.TrainPath;

public class PathAdapter extends
        FilterableRecyclerViewAdapter<TrainPath, PathAdapter.ViewHolder> {

    private DecimalFormat df = new DecimalFormat("#.0");

    public PathAdapter(List<TrainPath> dataSet) {
        super(Lists.newArrayList(dataSet));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_path, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TrainPath item = dataSet.get(position);
        boolean first = position == 0;
        boolean last = position == getItemCount() - 1;

        holder.headerRow.setVisibility(first ? View.VISIBLE : View.GONE);
        holder.kmText.setText(df.format(item.getKm()));
        holder.stationText.setText(item.getStation().getName());

        if (first) {
            holder.inText.setVisibility(View.INVISIBLE);
            holder.outText.setVisibility(View.VISIBLE);

            holder.outText.setText(item.getDepart().toString());
            holder.posImage.setImageResource(R.drawable.path_start);
        } else if (last) {
            holder.inText.setVisibility(View.VISIBLE);
            holder.outText.setVisibility(View.INVISIBLE);

            holder.inText.setText(item.getArrive().toString());
            holder.posImage.setImageResource(R.drawable.path_end);
        } else {
            holder.inText.setVisibility(View.VISIBLE);
            holder.outText.setVisibility(View.VISIBLE);

            holder.inText.setText(item.getArrive().toString());
            holder.outText.setText(item.getDepart().toString());

            if (item.getIsStop()) {
                holder.posImage.setImageResource(R.drawable.path_int);
                setColor(holder, R.color.text);
            } else {
                holder.posImage.setImageResource(R.drawable.path_no);
                setColor(holder, R.color.text_muted);
            }
        }
    }

    private void setColor(ViewHolder holder, @ColorRes int color) {
        holder.kmText.setTextColor(ContextCompat.getColor(AppContext.activity, color));
        holder.inText.setTextColor(ContextCompat.getColor(AppContext.activity, color));
        holder.stationText.setTextColor(ContextCompat.getColor(AppContext.activity, color));
        holder.outText.setTextColor(ContextCompat.getColor(AppContext.activity, color));
    }

    public class ViewHolder extends FilterableRecyclerViewAdapter.ClickableViewHolder {

        TableRow headerRow;
        TextView kmText;
        ImageView posImage;
        TextView inText;
        TextView stationText;
        TextView outText;

        public ViewHolder(View itemView) {
            super(itemView);

            headerRow = (TableRow) itemView.findViewById(R.id.path_header);
            kmText = (TextView) itemView.findViewById(R.id.path_km);
            posImage = (ImageView) itemView.findViewById(R.id.path_pos);
            inText = (TextView) itemView.findViewById(R.id.path_in);
            stationText = (TextView) itemView.findViewById(R.id.path_station);
            outText = (TextView) itemView.findViewById(R.id.path_out);
        }

    }
}
