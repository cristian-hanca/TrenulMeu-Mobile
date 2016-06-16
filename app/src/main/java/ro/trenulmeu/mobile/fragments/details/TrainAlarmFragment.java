package ro.trenulmeu.mobile.fragments.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import com.google.common.base.Strings;

import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.adapters.StationsAutoCompleteAdapter;
import ro.trenulmeu.mobile.models.Station;
import ro.trenulmeu.mobile.models.Train;
import ro.trenulmeu.mobile.models.TrainPath;
import ro.trenulmeu.mobile.timespan.TimeSpan;

/**
 * Mini-Fragment displaying basic Train Data.
 */
public class TrainAlarmFragment extends Fragment {

    public static final String visibleArgKey = "visibleArgKey";

    private View content;

    private int selectedId = 0;

    public static TrainAlarmFragment newInstance(boolean visible) {
        TrainAlarmFragment fragment = new TrainAlarmFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(visibleArgKey, visible);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        boolean visible = savedInstanceState == null
                || savedInstanceState.getBoolean(visibleArgKey, true);
        final Train train = AppContext.selectedTrain;

        final View view = inflater.inflate(R.layout.fragment_train_alarm, container, false);
        final EditText minutes = (EditText) view.findViewById(R.id.alarm_mins);

        List<Station> options = Stream.of(train.getStops())
                .map(new Function<TrainPath, Station>() {
                    @Override
                    public Station apply(TrainPath value) {
                        return value.getStation();
                    }
                })
                .skip(1)
                .collect(Collectors.<Station>toList());
        StationsAutoCompleteAdapter adapter = new StationsAutoCompleteAdapter(options,
                android.R.layout.simple_spinner_item);

        final Spinner selector = (Spinner) view.findViewById(R.id.alarm_stations);
        selector.setAdapter(adapter);

        view.findViewById(R.id.alarm_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Station selected = (Station) selector.getSelectedItem();
                if (selected != null) {
                    TrainPath path = Stream.of(train.getStops()).filter(new Predicate<TrainPath>() {
                        @Override
                        public boolean test(TrainPath value) {
                            return value.getStation().getId().equals(selected.getId());
                        }
                    }).findFirst().get();

                    String minStr = minutes.getText().toString();
                    int minutes = Strings.isNullOrEmpty(minStr) ? 5 : Integer.valueOf(minStr);

                    AppContext.activity.setAlarm(train.getTrainType().getName() + " "
                            + train.getName(), selected.getName(), minutes,
                            path.getDisplayArrive().subtract(minutes, TimeSpan.TimeUnits.MINUTE));
                }
            }
        });

        final ImageView toggle = (ImageView) view.findViewById(R.id.toggle);
        content = view.findViewById(R.id.content);
        view.findViewById(R.id.header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean visible = content.getVisibility() == View.VISIBLE;

                content.setVisibility(visible ? View.GONE : View.VISIBLE);
                toggle.setImageResource(visible ? R.drawable.expand : R.drawable.fold);
            }
        });

        content.setVisibility(visible ? View.VISIBLE : View.GONE);
        toggle.setImageResource(visible ? R.drawable.fold : R.drawable.expand);

        return view;
    }

    public boolean isContentVisible() {
        return content.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(visibleArgKey, isContentVisible());
        super.onSaveInstanceState(outState);
    }

}
