package ro.trenulmeu.mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.api.models.PathOption;
import ro.trenulmeu.mobile.api.models.PathStep;
import ro.trenulmeu.mobile.managedrecyclerview.ManagedRecyclerView;
import ro.trenulmeu.mobile.managedrecyclerview.adapter.FilterableRecyclerViewAdapter;
import ro.trenulmeu.mobile.timespan.TimeSpan;

public class RoutesAdapter extends FilterableRecyclerViewAdapter<PathOption, RoutesAdapter.ViewHolder> {

    public RoutesAdapter(List<PathOption> dataSet) {
        super(dataSet);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PathOption item = getItem(position);

        holder.fromTime.setText(item.getStartTime().toString());
        holder.toTime.setText(new TimeSpan(item.getEndTime()).subtract(TimeSpan.TimeUnits.DAY).toString());
        holder.totalTime.setText(item.getTotalTime().toString());
        holder.changesText.setText(String.valueOf(item.getSteps().size() - 1));

        holder.typesLayout.removeAllViews();
        for (PathStep ps : item.getSteps()) {
            ImageView iv = new ImageView(AppContext.activity);
            iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            iv.setImageResource(AppContext.activity.getResources().getIdentifier("type"
                    + ps.getTrain().getTypeId() + "c", "drawable", AppContext.activity.getPackageName()));
            holder.typesLayout.addView(iv);
        }

        holder.contentView.setAdapter(new StepsAdapter(item.getSteps()));

        holder.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.visible = !holder.visible;
                holder.contentLayout.setVisibility(holder.visible ? View.VISIBLE : View.GONE);
                holder.toggle.setImageResource(holder.visible ? R.drawable.fold : R.drawable.expand);
            }
        });
        holder.contentLayout.setVisibility(holder.visible ? View.VISIBLE : View.GONE);
        holder.toggle.setImageResource(holder.visible ? R.drawable.fold : R.drawable.expand);
    }

    public class ViewHolder extends FilterableRecyclerViewAdapter.ClickableViewHolder {

        boolean visible;
        TextView fromTime;
        TextView toTime;
        TextView totalTime;
        TextView changesText;
        ImageView toggle;
        LinearLayout typesLayout;
        LinearLayout contentLayout;
        ManagedRecyclerView contentView;


        public ViewHolder(View itemView) {
            super(itemView);

            visible = false;
            fromTime = (TextView) itemView.findViewById(R.id.item_route_from_time);
            toTime = (TextView) itemView.findViewById(R.id.item_route_to_time);
            totalTime = (TextView) itemView.findViewById(R.id.item_route_total_time);
            changesText = (TextView) itemView.findViewById(R.id.item_route_changes);
            toggle = (ImageView) itemView.findViewById(R.id.item_route_toggle);
            typesLayout = (LinearLayout) itemView.findViewById(R.id.item_route_types);
            contentLayout = (LinearLayout) itemView.findViewById(R.id.item_route_content);
            contentView = (ManagedRecyclerView) itemView.findViewById(R.id.item_route_content_list);

            itemView.setTag(this);
        }

    }

}
