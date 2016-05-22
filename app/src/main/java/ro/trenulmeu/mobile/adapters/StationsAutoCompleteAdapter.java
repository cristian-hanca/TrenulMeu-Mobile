package ro.trenulmeu.mobile.adapters;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.helpers.StringHelpers;
import ro.trenulmeu.mobile.models.Station;

public class StationsAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private @LayoutRes int layoutId;
    private List<Station> dataSet = new ArrayList<>();
    private List<Station> filterSet = new ArrayList<>();

    private StationFilter filter;

    public StationsAutoCompleteAdapter(List<Station> dataSet, @LayoutRes int layoutId) {
        this.dataSet = dataSet;
        this.filterSet = dataSet;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Station getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataSet.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = AppContext.activity.getLayoutInflater();
            convertView = inflater.inflate(layoutId, parent, false);

            holder = new ViewHolder();
            holder.view = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.view.setText(dataSet.get(position).getName());

        return convertView;
    }

    public class ViewHolder {
        TextView view;
    }


    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new StationFilter();
        }
        return filter;
    }

    private class StationFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0){
                ArrayList<Station> filterList = new ArrayList<>();
                for (int i=0; i < filterSet.size();i++){
                    if (StringHelpers.normalize(filterSet.get(i).getName())
                            .contains(StringHelpers.normalize(constraint.toString()))) {
                        Station contacts = new Station();
                        contacts.setName(filterSet.get(i).getName());
                        contacts.setId(filterSet.get(i).getId());
                        filterList.add(contacts);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = filterSet.size();
                results.values = filterSet;
            }
            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      Filter.FilterResults results) {
            dataSet = (ArrayList<Station>) results.values;
            notifyDataSetChanged();
        }
    }
}
