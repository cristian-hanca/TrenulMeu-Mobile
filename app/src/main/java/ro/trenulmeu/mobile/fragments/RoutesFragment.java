package ro.trenulmeu.mobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckedTextView;
import android.widget.Switch;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.google.common.collect.Lists;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.dao.query.QueryBuilder;
import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.Constants;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.adapters.PlatformAdapter;
import ro.trenulmeu.mobile.dialogs.DateTimeDialog;
import ro.trenulmeu.mobile.helpers.FragmentHelpers;
import ro.trenulmeu.mobile.helpers.StringHelpers;
import ro.trenulmeu.mobile.managedrecyclerview.ManagedRecyclerView;
import ro.trenulmeu.mobile.models.Station;
import ro.trenulmeu.mobile.models.TrainPath;
import ro.trenulmeu.mobile.models.TrainPathDao;

public class RoutesFragment extends Fragment {


    private static final String routesFromKey = "routesFromKey";
    private static final String routesToKey = "routesToKey";
    private static final String routesDateTime = "routesDateTime";

    private ManagedRecyclerView list;
    private PlatformAdapter adapter;

    private DateTime dateTime;
    private static Map<String, Station> stationMap;

    private AutoCompleteTextView autoFrom;
    private AutoCompleteTextView autoTo;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        AppContext.activity.setTitle(R.string.title_routes);
        if (dateTime == null) {
            dateTime = DateTime.now();
        }

        final View view = inflater.inflate(R.layout.fragment_routes, container, false);
        list = (ManagedRecyclerView) view.findViewById(R.id.list);
        autoFrom = (AutoCompleteTextView) view.findViewById(R.id.route_from);
        autoTo = (AutoCompleteTextView) view.findViewById(R.id.route_to);

        adapter = new PlatformAdapter(new ArrayList<TrainPath>());
        list.setAdapter(adapter);

        stationMap = new LinkedHashMap<>();
        for (Station s : AppContext.db.getStationDao().loadAll()) {
            stationMap.put(StringHelpers.normalize(s.getName()), s);
        }

        ArrayAdapter<String> a1 = new ArrayAdapter<>(AppContext.activity,
                android.R.layout.simple_spinner_dropdown_item,
                Lists.newArrayList(stationMap.keySet()));
        autoFrom.setAdapter(a1);
        autoFrom.setText("");

        autoTo.setText("");

        //UpdateUI();
        //UpdateList();
        return view;
    }

    private void UpdateUI() {

    }

    private void UpdateList() {

    }

    private void setupAutocomplete() {

    }

    @Override
    public void onAttach(Context context) {
        //isArrive = AppContext.cache.get(platformIsArrive_key, Boolean.class, true);
        //onlyStop = AppContext.cache.get(platformOnlyStop_key, Boolean.class, true);
        dateTime = AppContext.cache.get(routesDateTime, DateTime.class, DateTime.now());

        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        //AppContext.cache.set(platformIsArrive_key, isArrive);
        //AppContext.cache.set(platformOnlyStop_key, onlyStop);
        AppContext.cache.set(routesDateTime, dateTime);

        super.onDetach();
    }

}
