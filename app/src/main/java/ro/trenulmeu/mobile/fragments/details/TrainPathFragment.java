package ro.trenulmeu.mobile.fragments.details;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.common.collect.Lists;

import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.adapters.AvailabilityAdapter;
import ro.trenulmeu.mobile.adapters.PathAdapter;
import ro.trenulmeu.mobile.managedrecyclerview.ManagedRecyclerView;
import ro.trenulmeu.mobile.models.Train;
import ro.trenulmeu.mobile.models.TrainPath;

/**
 * Mini-Fragment displaying basic Train Data.
 */
public class TrainPathFragment extends Fragment {

    public static final String pathAdapter_key = "pathAdapter_key";

    public static final String visibleArgKey = "visibleArgKey";
    public static final String modeArgKey = "modeArgKey";

    private View content;
    private boolean visible;
    private boolean onlyStops;

    private ImageView toggle;
    private ManagedRecyclerView list;
    private PathAdapter adapter;
    private Train train;

    public static TrainPathFragment newInstance(boolean visible, boolean onlyStops) {
        TrainPathFragment fragment = new TrainPathFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(visibleArgKey, visible);
        bundle.putBoolean(modeArgKey, onlyStops);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        train = AppContext.selectedTrain;

        visible = savedInstanceState == null
                || savedInstanceState.getBoolean(visibleArgKey, true);
        onlyStops = savedInstanceState == null
                || savedInstanceState.getBoolean(modeArgKey, true);

        final View view = inflater.inflate(R.layout.fragment_train_path, container, false);

        list = (ManagedRecyclerView) view.findViewById(R.id.list);
        toggle = (ImageView) view.findViewById(R.id.toggle);
        content = view.findViewById(R.id.content);
        view.findViewById(R.id.path_mode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlyStops = !onlyStops;
                updateList();
            }
        });
        view.findViewById(R.id.header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible = !visible;
                updateUi();
            }
        });

        /**
         * Look at this hacky thing!
         * It does the expensive task of loading the Paths in a different thread, after 0.5s.
         * The aim is to wait for the Transition to end before launching this hard task.
         */
        final AsyncTask<Void, Void, Void> loadTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                initList();
                return null;
            }
        }.execute();

        updateUi();
        return view;
    }

    private void updateUi() {
        content.setVisibility(visible ? View.VISIBLE : View.GONE);
        toggle.setImageResource(visible ? R.drawable.fold : R.drawable.expand);
    }

    private void updateList() {
        final List<TrainPath> filteredModelList = onlyStops
                ? AppContext.selectedTrain.getSortedStops()
                : AppContext.selectedTrain.getSortedPath();
        adapter.animateTo(filteredModelList);
        list.getListView().scrollToPosition(0);
    }

    private void initList() {
        if (adapter == null) {
            adapter = new PathAdapter(onlyStops
                    ? train.getSortedStops()
                    : train.getSortedPath());
        }

        AppContext.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.getListView().setNestedScrollingEnabled(false);
                list.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(visibleArgKey, visible);
        outState.putBoolean(modeArgKey, onlyStops);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        adapter = AppContext.cache.get(pathAdapter_key, PathAdapter.class);
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        AppContext.cache.set(pathAdapter_key, adapter);
        System.gc();
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!AppContext.activity.isChangingConfigurations()) {
            AppContext.cache.delete(pathAdapter_key);
        }
    }

}
