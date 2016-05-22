package ro.trenulmeu.mobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.Constants;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.adapters.StationsAdapter;
import ro.trenulmeu.mobile.filters.StationsFilters;
import ro.trenulmeu.mobile.helpers.FragmentHelpers;
import ro.trenulmeu.mobile.managedrecyclerview.ManagedRecyclerView;
import ro.trenulmeu.mobile.managedrecyclerview.adapter.RecyclerViewClickListener;
import ro.trenulmeu.mobile.models.Station;

public class StationsFragment extends Fragment {

    private String searchString;

    private ManagedRecyclerView list;
    private StationsAdapter adapter;
    private SearchView search;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        AppContext.activity.setTitle(R.string.title_station);
        if (AppContext.stationFilter == null) {
            AppContext.stationFilter = new StationsFilters();
        }

        final View view = inflater.inflate(R.layout.fragment_stations, container, false);
        list = (ManagedRecyclerView) view.findViewById(R.id.list);
        search = (SearchView) view.findViewById(R.id.search);

        adapter = new StationsAdapter(AppContext.stationFilter.getFiltered());
        adapter.setItemClickListener(new RecyclerViewClickListener() {
            @Override
            public void itemClicked(View v, int position) {
                AppContext.selectedStation = adapter.getItem(position);
                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentHelpers.goToSingleton(new PlatformFragment(), Constants.gotoPlatform);
                    }
                });
            }
        });
        list.setAdapter(adapter);

        searchString = AppContext.stationFilter.getSearch();
        search.setQuery(searchString, false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                searchString = newText;
                AppContext.stationFilter.setSearch(searchString);
                UpdateUI();
                return true;
            }
        });

        return view;
    }

    private void UpdateUI() {
        final List<Station> filteredModelList = AppContext.stationFilter.getFiltered();
        adapter.animateTo(filteredModelList);
        list.getListView().scrollToPosition(0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
