package ro.trenulmeu.mobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.controls.ExpandFragment;
import ro.trenulmeu.mobile.models.Train;

public class DetailsFragment extends Fragment {

    private Train train;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        train = AppContext.selectedTrain;
        AppContext.activity.setTitle(R.string.title_details);

        final View view = inflater.inflate(R.layout.fragment_details, container, false);

        ((ImageView) view.findViewById(R.id.train_type_img)).setImageResource(
                AppContext.activity.getResources().getIdentifier(
                "type" + train.getTypeId(), "drawable", AppContext.activity.getPackageName()));
        ((TextView) view.findViewById(R.id.train_type_name)).setText(train.getTrainType().getLongName());
        ((TextView) view.findViewById(R.id.train_name)).setText(train.getName());

        ExpandFragment ef = (ExpandFragment) view.findViewById(R.id.ef);
        ef.setTitle(R.string.placeholder_station_long);
        ef.setFragment(new SplashFragment(), "");

        return view;
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
