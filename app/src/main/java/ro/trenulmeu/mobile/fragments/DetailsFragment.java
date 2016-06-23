package ro.trenulmeu.mobile.fragments;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.fragments.details.TrainAlarmFragment;
import ro.trenulmeu.mobile.fragments.details.TrainAvailabilityFragment;
import ro.trenulmeu.mobile.fragments.details.TrainGlanceFragment;
import ro.trenulmeu.mobile.fragments.details.TrainMapFragment;
import ro.trenulmeu.mobile.fragments.details.TrainPathFragment;
import ro.trenulmeu.mobile.fragments.details.TrainServiceFragment;
import ro.trenulmeu.mobile.models.Train;

public class DetailsFragment extends Fragment {

    private static final String glanceTag = "glanceTag";
    private static final String serviceTag = "serviceTag";
    private static final String pathTag = "pathTag";
    private static final String availabilityTag = "availabilityTag";
    private static final String mapTag = "mapTag";
    private static final String alarmTag = "alarmTag";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        Train train = AppContext.selectedTrain;
        AppContext.activity.setTitle(train.getTrainType().getName() + " " + train.getName());

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        TrainGlanceFragment glance = findFragment(glanceTag, TrainGlanceFragment.class);
        if (glance == null) {
            glance = TrainGlanceFragment.newInstance(true);
            addFragment(R.id.details_glance, glanceTag, glance);
        }

        TrainServiceFragment service = findFragment(serviceTag, TrainServiceFragment.class);
        if (service == null) {
            service = TrainServiceFragment.newInstance(true);
            addFragment(R.id.details_service, serviceTag, service);
        }

        TrainPathFragment path = findFragment(pathTag, TrainPathFragment.class);
        if (path == null) {
            path = TrainPathFragment.newInstance(true, true);
            addFragment(R.id.details_path, pathTag, path);
        }

        TrainMapFragment map = findFragment(mapTag, TrainMapFragment.class);
        if (map == null) {
            map = TrainMapFragment.newInstance(true);
            addFragment(R.id.details_map, mapTag, map);
        }
        map.setScrollParent((NestedScrollView) view.findViewById(R.id.scroll));

        TrainAvailabilityFragment availability = findFragment(availabilityTag, TrainAvailabilityFragment.class);
        if (availability == null) {
            availability = TrainAvailabilityFragment.newInstance(true);
            addFragment(R.id.details_availability, availabilityTag, availability);
        }

        TrainAlarmFragment alarm = findFragment(alarmTag, TrainAlarmFragment.class);
        if (alarm == null) {
            alarm = TrainAlarmFragment.newInstance(true);
            addFragment(R.id.details_alarm, alarmTag, alarm);
        }

        return view;
    }

    private void addFragment(@IdRes int destination, String tag, Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.add(destination, fragment, tag);
        ft.commit();
    }

    private <T extends Fragment> T findFragment(String tag, Class<T> clazz) {
        try {
            return clazz.cast(getChildFragmentManager().findFragmentByTag(tag));
        } catch (Exception e) {
            return null;
        }
    }

}
