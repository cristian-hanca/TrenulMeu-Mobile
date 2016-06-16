package ro.trenulmeu.mobile.fragments.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Locale;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.models.Train;

/**
 * Mini-Fragment displaying basic Train Data.
 */
public class TrainServiceFragment extends Fragment {

    public static final String visibleArgKey = "visibleArgKey";

    private View content;

    public static TrainServiceFragment newInstance(boolean visible) {
        TrainServiceFragment fragment = new TrainServiceFragment();
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

        final View view = inflater.inflate(R.layout.fragment_train_service, container, false);

        String service = train.getTrainService().getName().toLowerCase(Locale.ENGLISH);
        view.findViewById(R.id.service_c1).setVisibility(service.contains("c1") ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.service_c2).setVisibility(service.contains("c2") ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.service_d1).setVisibility(service.contains("d1") ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.service_d4).setVisibility(service.contains("d4") ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.service_d6).setVisibility(service.contains("d6") ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.service_b).setVisibility(service.contains("b") ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.service_r).setVisibility(service.contains("r") ? View.VISIBLE : View.GONE);

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
