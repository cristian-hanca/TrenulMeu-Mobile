package ro.trenulmeu.mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.Lists;

import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.Constants;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.api.models.PathStep;
import ro.trenulmeu.mobile.fragments.DetailsFragment;
import ro.trenulmeu.mobile.fragments.PlatformFragment;
import ro.trenulmeu.mobile.fragments.details.TrainPathFragment;
import ro.trenulmeu.mobile.helpers.FragmentHelpers;
import ro.trenulmeu.mobile.managedrecyclerview.adapter.FilterableRecyclerViewAdapter;

public class StepsAdapter extends
        FilterableRecyclerViewAdapter<PathStep, StepsAdapter.ViewHolder> {

    public StepsAdapter(List<PathStep> dataSet) {
        super(Lists.newArrayList(dataSet));
        this.dataSet.add(dataSet.get(dataSet.size() - 1));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PathStep item = dataSet.get(position);
        boolean first = position == 0;
        boolean last = position == getItemCount() - 1;

        holder.stationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppContext.selectedStation = AppContext.db.getStationDao().load((long) v.getTag());
                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentHelpers.goToSingleton(new PlatformFragment(), Constants.gotoPlatform);
                    }
                });
            }
        });
        holder.trainName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppContext.selectedTrain = AppContext.db.getTrainDao().loadDeep((long) v.getTag());
                AppContext.cache.delete(TrainPathFragment.pathAdapter_key);
                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentHelpers.goToSingleton(new DetailsFragment(), Constants.gotoDetails);
                    }
                });
            }
        });
        holder.trainName.setTag((long) item.getTrain().getTrainId());

        if (last) {
            holder.stationName.setTag((long) item.getEnd().getId());
            holder.stationName.setText(item.getEnd().getName());
            holder.trainLayout.setVisibility(View.GONE);
            holder.stationImg.setImageResource(R.drawable.path_end_used);

            holder.inText.setVisibility(View.VISIBLE);
            holder.outText.setVisibility(View.GONE);

            holder.inText.setText(item.getEndTime().toTimeString());
        } else {
            holder.stationName.setTag((long) item.getStart().getId());
            holder.stationName.setText(item.getStart().getName());
            holder.trainLayout.setVisibility(View.VISIBLE);
            holder.trainType.setImageResource(AppContext.activity.getResources().getIdentifier("type" +
                    item.getTrain().getTypeId(), "drawable", AppContext.activity.getPackageName()));
            holder.trainName.setText(item.getTrain().getName());

            if (first) {
                holder.stationImg.setImageResource(R.drawable.path_start_used);

                holder.inText.setVisibility(View.GONE);
                holder.outText.setVisibility(View.VISIBLE);

                holder.outText.setText(item.getStartTime().toString());
            } else {
                holder.stationImg.setImageResource(R.drawable.path_int_used);
                holder.inText.setText(dataSet.get(position - 1).getEndTime().toTimeString());

                holder.inText.setVisibility(View.VISIBLE);
                holder.outText.setVisibility(View.VISIBLE);

                holder.inText.setText(dataSet.get(position - 1).getEndTime().toTimeString());
                holder.outText.setText(item.getStartTime().toTimeString());
            }
        }
    }

    public class ViewHolder extends FilterableRecyclerViewAdapter.ClickableViewHolder {

        LinearLayout stationLayout;
        ImageView stationImg;
        TextView inText;
        TextView outText;
        TextView stationName;

        LinearLayout trainLayout;
        ImageView trainImg;
        ImageView trainType;
        TextView trainName;

        public ViewHolder(View itemView) {
            super(itemView);

            stationLayout = (LinearLayout) itemView.findViewById(R.id.item_step_stop);
            stationImg = (ImageView) itemView.findViewById(R.id.item_step_stop_img);
            inText = (TextView) itemView.findViewById(R.id.item_step_stop_in);
            outText = (TextView) itemView.findViewById(R.id.item_step_stop_out);
            stationName = (TextView) itemView.findViewById(R.id.item_step_stop_station);

            trainLayout = (LinearLayout) itemView.findViewById(R.id.item_step_train);
            trainImg = (ImageView) itemView.findViewById(R.id.item_step_train_img);
            trainType = (ImageView) itemView.findViewById(R.id.item_step_train_type);
            trainName = (TextView) itemView.findViewById(R.id.item_step_train_name);
        }

    }
}
