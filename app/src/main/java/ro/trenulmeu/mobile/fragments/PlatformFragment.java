package ro.trenulmeu.mobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.Constants;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.adapters.PlatformAdapter;
import ro.trenulmeu.mobile.dialogs.DateTimeDialog;
import ro.trenulmeu.mobile.fragments.details.TrainPathFragment;
import ro.trenulmeu.mobile.helpers.FragmentHelpers;
import ro.trenulmeu.mobile.helpers.TimeHelpers;
import ro.trenulmeu.mobile.managedrecyclerview.ManagedRecyclerView;
import ro.trenulmeu.mobile.managedrecyclerview.adapter.RecyclerViewClickListener;
import ro.trenulmeu.mobile.models.Station;
import ro.trenulmeu.mobile.models.TrainPath;
import ro.trenulmeu.mobile.models.TrainPathDao;
import ro.trenulmeu.mobile.timespan.TimeSpan;

public class PlatformFragment extends Fragment {

    private static final String platformIsArrive_key = "platformIsArrive_key";
    private static final String platformOnlyStop_key = "platformOnlyStop_key";
    private static final String platformDateTime_key = "platformDateTime_key";

    private ManagedRecyclerView list;
    private PlatformAdapter adapter;

    private Station station;
    private DateTime dateTime;
    private boolean isArrive;
    private boolean onlyStop;

    private TextView arriveText;
    private TextView departText;
    private Switch stopsSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        station = AppContext.selectedStation;
        AppContext.activity.setTitle(station.getName());
        if (dateTime == null) {
            dateTime = DateTime.now();
        }

        final View view = inflater.inflate(R.layout.fragment_platform, container, false);
        list = (ManagedRecyclerView) view.findViewById(R.id.list);

        stopsSwitch = (Switch) view.findViewById(R.id.item_platform_sw);
        stopsSwitch.setChecked(onlyStop);
        stopsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlyStop = !onlyStop;
                UpdateList();
            }
        });

        final DateTimeDialog dtd = FragmentHelpers.findByTag(Constants.dialogStationsDateTime,
                DateTimeDialog.class, new DateTimeDialog());
        dtd.setDateTime(dateTime);
        dtd.setCallbacks(new DateTimeDialog.Callbacks() {
            @Override
            public void onDone(DateTime result) {
                dateTime = result;
                UpdateList();
            }
        });
        view.findViewById(R.id.platform_dt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dtd.show(AppContext.activity.getSupportFragmentManager(), Constants.dialogTrainFilter);
            }
        });

        arriveText = (TextView) view.findViewById(R.id.platform_arr);
        arriveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isArrive) {
                    isArrive = true;
                    UpdateUI();
                    UpdateList();
                }
            }
        });

        departText = (TextView) view.findViewById(R.id.platform_dep);
        departText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isArrive) {
                    isArrive = false;
                    UpdateUI();
                    UpdateList();
                }
            }
        });

        adapter = new PlatformAdapter(new ArrayList<TrainPath>());
        adapter.setItemClickListener(new RecyclerViewClickListener() {
            @Override
            public void itemClicked(View v, int position) {
                AppContext.selectedTrain = adapter.getItem(position).getTrain();
                AppContext.cache.delete(TrainPathFragment.pathAdapter_key);
                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentHelpers.goToSingleton(new DetailsFragment(), Constants.gotoDetails);
                    }
                });
            }
        });
        list.setAdapter(adapter);

        UpdateUI();
        UpdateList();
        return view;
    }

    private void UpdateUI() {
        if (isArrive) {
            arriveText.setTextColor(ContextCompat.getColor(AppContext.activity, R.color.colorPrimaryDark));
            departText.setTextColor(ContextCompat.getColor(AppContext.activity, R.color.text));
        } else {
            arriveText.setTextColor(ContextCompat.getColor(AppContext.activity, R.color.text));
            departText.setTextColor(ContextCompat.getColor(AppContext.activity, R.color.colorPrimaryDark));
        }
    }

    private void UpdateList() {
        adapter.setShowArrival(isArrive);
        int point = TimeHelpers.getCorrectTime(
                new TimeSpan(0, dateTime.getHourOfDay(), dateTime.getMinuteOfHour()),
                station.getTimeOffset()).getTicks();

        QueryBuilder<TrainPath> query = AppContext.db.getTrainPathDao().queryBuilder()
                    .where(TrainPathDao.Properties.StationId.eq(station.getId()));
        if (isArrive) {
            query = query.where(TrainPathDao.Properties.DisplayArrive.ge(point))
                    .where(TrainPathDao.Properties.Km.gt(0))
                    .orderAsc(TrainPathDao.Properties.Arrive);
        } else {
            query = query.where(TrainPathDao.Properties.DisplayDepart.ge(point))
                    .where(TrainPathDao.Properties.IsFinalStop.eq(false))
                    .orderAsc(TrainPathDao.Properties.Depart);
        }
        if (onlyStop) {
            query = query.where(TrainPathDao.Properties.IsStop.eq(true));
        }

        final Date date = dateTime.toDate();
        List<TrainPath> ls = Stream.of(query.list()).filter(new Predicate<TrainPath>() {
            @Override
            public boolean test(TrainPath value) {
                return value.getTrain().runsOnDate(date);
            }
        }).collect(Collectors.<TrainPath>toList());

        adapter.replaceDataSet(ls);
    }

    @Override
    public void onAttach(Context context) {
        isArrive = AppContext.cache.get(platformIsArrive_key, Boolean.class, true);
        onlyStop = AppContext.cache.get(platformOnlyStop_key, Boolean.class, true);
        dateTime = AppContext.cache.get(platformDateTime_key, DateTime.class, DateTime.now());

        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        AppContext.cache.set(platformIsArrive_key, isArrive);
        AppContext.cache.set(platformOnlyStop_key, onlyStop);
        AppContext.cache.set(platformDateTime_key, dateTime);

        super.onDetach();
    }

}
