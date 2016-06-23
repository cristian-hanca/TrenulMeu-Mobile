package ro.trenulmeu.mobile.fragments.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.adapters.AvailabilityAdapter;
import ro.trenulmeu.mobile.managedrecyclerview.ManagedRecyclerView;
import ro.trenulmeu.mobile.models.Train;

/**
 * Mini-Fragment displaying basic Train Data.
 */
public class TrainAvailabilityFragment extends Fragment {

    public static final String visibleArgKey = "visibleArgKey";

    private View content;

    public static TrainAvailabilityFragment newInstance(boolean visible) {
        TrainAvailabilityFragment fragment = new TrainAvailabilityFragment();
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
        Train train = AppContext.selectedTrain;

        final View view = inflater.inflate(R.layout.fragment_train_availability, container, false);

        ManagedRecyclerView list = (ManagedRecyclerView) view.findViewById(R.id.content);
        AvailabilityAdapter aa = new AvailabilityAdapter(train.getAvailability());
        list.setAdapter(aa);
        list.getListView().setNestedScrollingEnabled(false);

        final ImageView toggle = (ImageView) view.findViewById(R.id.toggle);
        content = list;
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
