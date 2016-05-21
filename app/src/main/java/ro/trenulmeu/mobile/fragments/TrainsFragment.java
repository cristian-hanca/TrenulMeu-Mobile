package ro.trenulmeu.mobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.Constants;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.adapters.TrainsAdapter;
import ro.trenulmeu.mobile.dialogs.TrainsFilterDialog;
import ro.trenulmeu.mobile.filters.MultiSelectDialog;
import ro.trenulmeu.mobile.filters.TrainsFilters;
import ro.trenulmeu.mobile.filters.models.CheckItem;
import ro.trenulmeu.mobile.helpers.FragmentHelpers;
import ro.trenulmeu.mobile.managedrecyclerview.ManagedRecyclerView;
import ro.trenulmeu.mobile.managedrecyclerview.adapter.RecyclerViewClickListener;
import ro.trenulmeu.mobile.models.Train;
import ro.trenulmeu.mobile.models.TrainOperator;

public class TrainsFragment extends Fragment {

    private String searchString;

    private ManagedRecyclerView list;
    private TrainsAdapter adapter;
    private SearchView search;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        AppContext.activity.setTitle(R.string.title_trains);
        if (AppContext.trainFilter == null) {
            AppContext.trainFilter = new TrainsFilters();
        }
        final TrainsFilterDialog tfd = FragmentHelpers.findByTag(Constants.dialogTrainFilter,
                TrainsFilterDialog.class, new TrainsFilterDialog());
        tfd.setCallbacks(new TrainsFilterDialog.Callbacks() {
            @Override
            public void onDone() {
                UpdateUI();
            }
        });

        final View view = inflater.inflate(R.layout.fragment_trains, container, false);
        list = (ManagedRecyclerView) view.findViewById(R.id.list);
        search = (SearchView) view.findViewById(R.id.search);

        adapter = new TrainsAdapter(AppContext.trainFilter.getFiltered());
        adapter.setItemClickListener(new RecyclerViewClickListener() {
            @Override
            public void itemClicked(View v, int position) {
                AppContext.selectedTrain = adapter.getItem(position);
                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentHelpers.goToSingleton(new DetailsFragment(), Constants.gotoDetails);
                    }
                });
            }
        });
        list.setAdapter(adapter);

        view.findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tfd.show(AppContext.activity.getSupportFragmentManager(), Constants.dialogTrainFilter);
            }
        });

        searchString = AppContext.trainFilter.getSearch();
        search.setQuery(searchString, false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                searchString = newText;
                AppContext.trainFilter.setSearch(searchString);
                UpdateUI();
                return true;
            }
        });

        return view;
    }

    private void UpdateUI() {
        final List<Train> filteredModelList = AppContext.trainFilter.getFiltered();
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
