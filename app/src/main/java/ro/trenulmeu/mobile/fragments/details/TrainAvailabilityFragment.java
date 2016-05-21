package ro.trenulmeu.mobile.fragments.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.adapters.AvailabilityAdapter;
import ro.trenulmeu.mobile.managedrecyclerview.ManagedRecyclerView;
import ro.trenulmeu.mobile.models.Train;

/**
 * Mini-Fragment displaying basic Train Data.
 */
public class TrainAvailabilityFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        Train train = AppContext.selectedTrain;

        final View view = inflater.inflate(R.layout.fragment_train_availability, container, false);

        ManagedRecyclerView list = (ManagedRecyclerView) view.findViewById(R.id.list);
        AvailabilityAdapter aa = new AvailabilityAdapter(train.getAvailability());
        list.setAdapter(aa);

        return view;
    }

}
