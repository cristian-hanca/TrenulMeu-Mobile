package ro.trenulmeu.mobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import lombok.Data;
import retrofit2.Call;
import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.Constants;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.adapters.RoutesAdapter;
import ro.trenulmeu.mobile.adapters.StationsAutoCompleteAdapter;
import ro.trenulmeu.mobile.api.MobileAPI;
import ro.trenulmeu.mobile.api.fetcher.FetchUnit;
import ro.trenulmeu.mobile.api.models.PathOption;
import ro.trenulmeu.mobile.api.models.PathResult;
import ro.trenulmeu.mobile.dialogs.DateTimeDialog;
import ro.trenulmeu.mobile.helpers.CleanupFetchCallbacks;
import ro.trenulmeu.mobile.helpers.FragmentHelpers;
import ro.trenulmeu.mobile.managedrecyclerview.ManagedRecyclerView;
import ro.trenulmeu.mobile.models.Station;

public class RoutesFragment extends Fragment {

    private static final String routesDataKey = "routesDataKey";
    private static final String optionsFetch_key = "optionsFetch_key";

    private Content content;

    private ManagedRecyclerView list;
    private RoutesAdapter adapter;

    private AutoCompleteTextView autoFrom;
    private AutoCompleteTextView autoTo;
    private TextView datetimeText;

    private PathResult options;
    private FetchUnit<PathResult> optionsFetch;
    private FetchUnit.FetchCallbacks<PathResult> optionsCallbacks;

    private View howto;
    private View loading;
    private View noitems;

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

        howto = view.findViewById(R.id.how_to);
        loading = view.findViewById(R.id.loading);
        noitems = view.findViewById(R.id.no_items);

        adapter = new RoutesAdapter(new ArrayList<PathOption>());
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
                request();
            }
        });

        optionsCallbacks = new CleanupFetchCallbacks<PathResult>(optionsFetch_key) {
            @Override
            public void onStart() {
                options = null;
                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UpdateList();
                    }
                });
            }

            @Override
            public void onFail(final Exception e) {
                options = null;
                optionsFetch = null;
                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar s = Snackbar.make(view,
                                AppContext.activity.getString(R.string.error_routes_download) +
                                e.getMessage(), Snackbar.LENGTH_LONG);
                        ((TextView) s.getView().findViewById(android.support.design.R.id.snackbar_text))
                                .setTextColor(ContextCompat.getColor(AppContext.activity, R.color.white));
                        s.show();
                        UpdateList();
                    }
                });
            }

            @Override
            public void onSuccess(PathResult result) {
                options = result;
                optionsFetch = null;
                AppContext.cache.set(Constants.routeOptions_key, options);
                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UpdateList();
                    }
                });
            }
        };
        if (optionsFetch != null) {
            optionsFetch.setCallbacks(optionsCallbacks);
        }

        UpdateUI();
        UpdateList();
        return view;
    }

    private void request() {
        final Station from = content.stationMap.get(content.fromId);
        final Station to = content.stationMap.get(content.toId);

        optionsFetch = new FetchUnit<>(optionsCallbacks);
        optionsFetch.fetch(new Callable<Call<PathResult>>() {
            @Override
            public Call<PathResult> call() throws Exception {
                return MobileAPI.getRoutes(from, to, content.dateTime);
            }
        });
        AppContext.cache.set(optionsFetch_key, optionsFetch);
    }

    private void UpdateUI() {
        autoFrom.setText(content.fromId == -1 ? "" : content.stationMap.get(content.fromId).getName());
        autoTo.setText(content.toId == -1 ? "" : content.stationMap.get(content.toId).getName());
        datetimeText.setText(content.dateTime.toString("dd/MM/yy HH:mm"));
    }

    private void UpdateList() {
        if (options == null) {
            if (optionsFetch == null) {
                howto.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                noitems.setVisibility(View.GONE);
                list.setVisibility(View.GONE);
            } else {
                howto.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                noitems.setVisibility(View.GONE);
                list.setVisibility(View.GONE);
            }
        } else if (options.getOptions().size() == 0) {
            howto.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            noitems.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            howto.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            noitems.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);

            adapter.animateTo(options.getOptions());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        content = AppContext.cache.get(routesDataKey, Content.class);
        options = AppContext.cache.get(Constants.routeOptions_key, PathResult.class);
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
