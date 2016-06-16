package ro.trenulmeu.mobile.adapters;

import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.common.collect.Lists;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.managedrecyclerview.adapter.FilterableRecyclerViewAdapter;
import ro.trenulmeu.mobile.models.Train;
import ro.trenulmeu.mobile.models.TrainPath;
import ro.trenulmeu.mobile.timespan.TimeSpan;

public class PathAdapter extends
        FilterableRecyclerViewAdapter<TrainPath, PathAdapter.ViewHolder> {

    private int delay = 0;
    private int runPos = -1;
    private int nextStopPos = -1;
    private boolean isStop = false;
    private TimeSpan localTime = null;

    private DecimalFormat df = new DecimalFormat("0.0");

    public PathAdapter(List<TrainPath> dataSet) {
        super(Lists.newArrayList(dataSet));
    }

    public void setProgress(Train train, TimeSpan time, int delay) {
        this.runPos = -1;
        this.isStop = false;
        this.delay = delay;

        if (train.runsOnDateTime(DateTime.now()
                .withHourOfDay(time.getCount(TimeSpan.TimeUnits.HOUR))
                .withMinuteOfHour(time.getCount(TimeSpan.TimeUnits.MINUTE))
                .toDate())) {
            this.localTime = new TimeSpan(time);
            localTime = localTime.subtract(delay, TimeSpan.TimeUnits.MINUTE);
            Pair<Integer, Boolean> pos = getTrainPosition(localTime, train.getToTime());

            this.runPos = pos.first;
            this.isStop = pos.second;

            if (delay < 0) {
                Pair<Integer, Boolean> tmpPos = getTrainPosition(time, train.getToTime());
                for (int i = tmpPos.first + 1; i < dataSet.size(); i++) {
                    if (dataSet.get(i).getIsStop()) {
                        this.nextStopPos = i;
                        break;
                    }
                }

                this.runPos = Math.min(this.nextStopPos, this.runPos);
                this.isStop = this.runPos == this.nextStopPos || this.isStop;
            } else {
                this.nextStopPos = -1;
            }
        }

        notifyDataSetChanged();
    }

    private Pair<Integer, Boolean> getTrainPosition(TimeSpan time, TimeSpan toTime) {
        int pos = -1;
        boolean stp = false;

        TimeSpan localTime = new TimeSpan(time);
        while (localTime.compareTo(toTime) < 0) {
            for (int i = 0; i < dataSet.size(); i++) {
                TrainPath path = dataSet.get(i);
                int compare = path.getArrive().compareTo(localTime);
                if (compare <= 0) {
                    pos = i;
                    stp = localTime.compareTo(path.getDepart()) < 0;
                } else {
                    break;
                }
            }
            if (pos != -1) {
                break;
            }
            localTime = localTime.add(1, TimeSpan.TimeUnits.DAY);
        }

        return new Pair<>(pos, stp);
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

        if (delay < 0 && position == nextStopPos) {
            holder.inDelayText.setVisibility(View.VISIBLE);
            holder.outDelayText.setVisibility(View.GONE);

            holder.inDelayText.setText(String.valueOf(delay));
        } else if (delay == 0 || position <= runPos
                || (delay < 0 && position > nextStopPos)) {
            holder.inDelayText.setVisibility(View.GONE);
            holder.outDelayText.setVisibility(View.GONE);
        } else {
            holder.inDelayText.setVisibility(View.VISIBLE);
            holder.outDelayText.setVisibility(View.VISIBLE);

            String delayText = (delay > 0 ? "+" : "") + delay;
            holder.inDelayText.setText(delayText);
            holder.outDelayText.setText(delayText);
        }

        if (first) {
            holder.inLayout.setVisibility(View.INVISIBLE);
            holder.outLayout.setVisibility(View.VISIBLE);

            holder.outText.setText(item.getDisplayDepart().toString());
            holder.posImage.setImageResource(runPos < 0
                    ? R.drawable.path_start
                    : R.drawable.path_start_used);
        } else if (last) {
            holder.inLayout.setVisibility(View.VISIBLE);
            holder.outLayout.setVisibility(View.INVISIBLE);

            holder.inText.setText(item.getDisplayArrive().toString());
            holder.posImage.setImageResource(runPos < position
                    ? R.drawable.path_end
                    : R.drawable.path_end_used);
        } else {
            holder.inLayout.setVisibility(View.VISIBLE);
            holder.outLayout.setVisibility(View.VISIBLE);

            holder.inText.setText(item.getDisplayArrive().toString());
            holder.outText.setText(item.getDisplayDepart().toString());

            if (item.getIsStop()) {
                holder.posImage.setImageResource(runPos < position
                        ? R.drawable.path_int : runPos > position
                        ? R.drawable.path_int_used : isStop
                        ? R.drawable.path_int_stop
                        : R.drawable.path_int_used);
                setColor(holder, R.color.text);
                setDelayColor(holder, R.color.delay_neg, R.color.delay_pos);
            } else {
                holder.posImage.setImageResource(runPos < position
                        ? R.drawable.path_no : runPos > position
                        ? R.drawable.path_no_used : localTime.compareTimeTo(item.getArrive()) == 0
                        ? R.drawable.path_no_stop
                        : R.drawable.path_no_used);
                setColor(holder, R.color.text_muted);
                setDelayColor(holder, R.color.delay_neg_muted, R.color.delay_pos_muted);
            }
        }
    }

    private void setColor(ViewHolder holder, @ColorRes int color) {
        holder.kmText.setTextColor(ContextCompat.getColor(AppContext.activity, color));
        holder.inText.setTextColor(ContextCompat.getColor(AppContext.activity, color));
        holder.stationText.setTextColor(ContextCompat.getColor(AppContext.activity, color));
        holder.outText.setTextColor(ContextCompat.getColor(AppContext.activity, color));
    }

    private void setDelayColor(ViewHolder holder, @ColorRes int neg, @ColorRes int pos) {
        int color = delay > 0 ? neg : pos;

        holder.inDelayText.setTextColor(ContextCompat.getColor(AppContext.activity, color));
        holder.outDelayText.setTextColor(ContextCompat.getColor(AppContext.activity, color));
    }

    public class ViewHolder extends FilterableRecyclerViewAdapter.ClickableViewHolder {

        TableRow headerRow;
        TextView kmText;
        ImageView posImage;
        LinearLayout inLayout;
        TextView inText;
        TextView inDelayText;
        TextView stationText;
        LinearLayout outLayout;
        TextView outText;
        TextView outDelayText;

        public ViewHolder(View itemView) {
            super(itemView);

            headerRow = (TableRow) itemView.findViewById(R.id.path_header);
            kmText = (TextView) itemView.findViewById(R.id.path_km);
            posImage = (ImageView) itemView.findViewById(R.id.path_pos);
            inLayout = (LinearLayout) itemView.findViewById(R.id.path_in_layout);
            inText = (TextView) itemView.findViewById(R.id.path_in);
            inDelayText = (TextView) itemView.findViewById(R.id.path_in_delay);
            stationText = (TextView) itemView.findViewById(R.id.path_station);
            outLayout = (LinearLayout) itemView.findViewById(R.id.path_out_layout);
            outText = (TextView) itemView.findViewById(R.id.path_out);
            outDelayText = (TextView) itemView.findViewById(R.id.path_out_delay);
        }

    }
}
