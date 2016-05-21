package ro.trenulmeu.mobile.fragments.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.models.Train;

/**
 * Mini-Fragment displaying basic Train Data.
 */
public class TrainGlanceFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        Train train = AppContext.selectedTrain;

        final View view = inflater.inflate(R.layout.fragment_train_glance, container, false);

        ((TextView) view.findViewById(R.id.glance_from)).setText(train.getFromName());
        ((TextView) view.findViewById(R.id.glance_to)).setText(train.getToName());
        ((TextView) view.findViewById(R.id.glance_time)).setText(train.getTotalTime().toString());

        return view;
    }

}
