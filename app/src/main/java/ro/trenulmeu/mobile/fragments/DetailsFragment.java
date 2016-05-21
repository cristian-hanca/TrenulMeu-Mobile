package ro.trenulmeu.mobile.fragments;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.fragments.details.TrainAvailabilityFragment;
import ro.trenulmeu.mobile.fragments.details.TrainGlanceFragment;
import ro.trenulmeu.mobile.models.Train;

public class DetailsFragment extends Fragment {

    private static final String glanceTag = "glanceTag";
    private static final String serviceTag = "serviceTag";
    private static final String pathTag = "pathTag";
    private static final String availabilityTag = "availabilityTag";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        Train train = AppContext.selectedTrain;
        AppContext.activity.setTitle(R.string.title_details);

        final View view = inflater.inflate(R.layout.fragment_details, container, false);

        ((ImageView) view.findViewById(R.id.train_type_img)).setImageResource(
                AppContext.activity.getResources().getIdentifier(
                "type" + train.getTypeId(), "drawable", AppContext.activity.getPackageName()));
        ((TextView) view.findViewById(R.id.train_type_name)).setText(train.getTrainType().getLongName());
        ((TextView) view.findViewById(R.id.train_name)).setText(train.getName());

        if (savedInstanceState == null) {
            TrainGlanceFragment glance = TrainGlanceFragment.newInstance(true);
            addFragment(R.id.details_glance, glanceTag, glance);

            TrainAvailabilityFragment availability = TrainAvailabilityFragment.newInstance(true);
            addFragment(R.id.details_availability, availabilityTag, availability);
        }

        return view;
    }

    private void addFragment(@IdRes int destination, String tag, Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.add(destination, fragment, tag);
        ft.commit();
    }

}
