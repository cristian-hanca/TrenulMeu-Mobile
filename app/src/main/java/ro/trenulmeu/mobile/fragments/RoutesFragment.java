package ro.trenulmeu.mobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import lombok.Data;
import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.Constants;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.adapters.PlatformAdapter;
import ro.trenulmeu.mobile.adapters.StationsAutoCompleteAdapter;
import ro.trenulmeu.mobile.api.fetcher.FetchUnit;
import ro.trenulmeu.mobile.api.models.PathOption;
import ro.trenulmeu.mobile.dialogs.DateTimeDialog;
import ro.trenulmeu.mobile.helpers.FragmentHelpers;
import ro.trenulmeu.mobile.helpers.StringHelpers;
import ro.trenulmeu.mobile.managedrecyclerview.ManagedRecyclerView;
import ro.trenulmeu.mobile.models.DataBaseStatus;
import ro.trenulmeu.mobile.models.Station;
import ro.trenulmeu.mobile.models.TrainPath;
import ro.trenulmeu.mobile.models.TrainPathDao;
import ro.trenulmeu.mobile.timespan.TimeSpan;

public class RoutesFragment extends Fragment {

    private static final String routesFetch_key = "routesFetch_key";
    private static final String routesDataKey = "routesDataKey";

    private Content content;

    private ManagedRecyclerView list;
    private PlatformAdapter adapter;

    private AutoCompleteTextView autoFrom;
    private AutoCompleteTextView autoTo;
    private TextView datetimeText;

    private List<PathOption> options;
    private FetchUnit<List<PathOption>> optionsFetch;
    private FetchUnit.FetchCallbacks<List<PathOption>> optionsCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        AppContext.activity.setTitle(R.string.title_routes);
        if (content == null) {
            content = new Content();
            content.dateTime = new DateTime();
            content.stationMap = new LinkedHashMap<>();
            for (Station s : AppContext.db.getStationDao().loadAll()) {
                content.stationMap.put(s.getId(), s);
            }
            content.fromId = -1;
            content.toId = -1;
        }

        final View view = inflater.inflate(R.layout.fragment_routes, container, false);
        list = (ManagedRecyclerView) view.findViewById(R.id.list);
        autoFrom = (AutoCompleteTextView) view.findViewById(R.id.route_from);
        autoTo = (AutoCompleteTextView) view.findViewById(R.id.route_to);
        datetimeText = (TextView) view.findViewById(R.id.route_date);

        adapter = new PlatformAdapter(new ArrayList<TrainPath>());
        list.setAdapter(adapter);

        StationsAutoCompleteAdapter a1 = new StationsAutoCompleteAdapter(AppContext.db.getStationDao().loadAll(), android.R.layout.simple_dropdown_item_1line);
        autoFrom.setAdapter(a1);
        autoFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                content.fromId = id;
                UpdateUI();
            }
        });

        StationsAutoCompleteAdapter a2 = new StationsAutoCompleteAdapter(AppContext.db.getStationDao().loadAll(), android.R.layout.simple_dropdown_item_1line);
        autoTo.setAdapter(a2);
        autoTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                content.toId = id;
                UpdateUI();
            }
        });

        final DateTimeDialog dtd = FragmentHelpers.findByTag(Constants.dialogStationsDateTime,
                DateTimeDialog.class, new DateTimeDialog());
        dtd.setDateTime(content.dateTime);
        dtd.setCallbacks(new DateTimeDialog.Callbacks() {
            @Override
            public void onDone(DateTime result) {
                content.dateTime = result;
                UpdateUI();
            }
        });
        view.findViewById(R.id.route_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dtd.show(AppContext.activity.getSupportFragmentManager(), Constants.dialogTrainFilter);
            }
        });

        view.findViewById(R.id.route_flip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long tmp = content.fromId;
                content.fromId = content.toId;
                content.toId = tmp;
                UpdateUI();
            }
        });

        view.findViewById(R.id.route_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        UpdateUI();
        //UpdateList();
        return view;
    }

    private void UpdateUI() {
        autoFrom.setText(content.fromId == -1 ? "" : content.stationMap.get(content.fromId).getName());
        autoTo.setText(content.toId == -1 ? "" : content.stationMap.get(content.toId).getName());
        datetimeText.setText(content.dateTime.toString("dd/MM/yy HH:mm"));
    }

    private void UpdateList() {

    }

    @Override
    public void onAttach(Context context) {
        content = AppContext.cache.get(routesDataKey, Content.class);
        //options = AppContext.cache.get(Constants.routeOptions_key, List.class);
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        AppContext.cache.set(routesDataKey, content);
        super.onDetach();
    }

    @Data
    public class Content {
        private DateTime dateTime;
        private Map<Long, Station> stationMap;
        private long fromId;
        private long toId;
    }

}
