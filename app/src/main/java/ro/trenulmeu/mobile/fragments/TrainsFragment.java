package ro.trenulmeu.mobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.adapters.TrainsAdapter;
import ro.trenulmeu.mobile.managedrecyclerview.ManagedRecyclerView;
import ro.trenulmeu.mobile.models.Train;

public class TrainsFragment extends Fragment {

    private static final String trainsFilter_key = "trainsFilter_key";

    private String searchString;

    private ManagedRecyclerView list;
    private TrainsAdapter adapter;
    private SearchView search;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        AppContext.activity.setTitle(R.string.title_trains);

        final View view = inflater.inflate(R.layout.fragment_trains, container, false);
        list = (ManagedRecyclerView) view.findViewById(R.id.list);
        search = (SearchView) view.findViewById(R.id.search);

        adapter = new TrainsAdapter(AppContext.db.getTrainDao().queryBuilder().list());
        list.setAdapter(adapter);

        search.setIconified(false);
        search.setIconifiedByDefault(false);
        search.setQuery(searchString, false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                searchString = newText;

                final List<Train> filteredModelList = AppContext.db.getTrainDao().queryBuilder().list();
                adapter.animateTo(filteredModelList);
                list.getListView().scrollToPosition(0);
                return true;
            }
        });

        return view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        searchString = AppContext.cache.get(trainsFilter_key, String.class, "");

        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        AppContext.cache.set(trainsFilter_key, searchString);

        super.onDetach();
    }

}
