package ro.trenulmeu.mobile.fragments.details;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;
import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.adapters.PathAdapter;
import ro.trenulmeu.mobile.api.MobileAPI;
import ro.trenulmeu.mobile.api.fetcher.FetchUnit;
import ro.trenulmeu.mobile.api.models.TrainDelay;
import ro.trenulmeu.mobile.helpers.CleanupFetchCallbacks;
import ro.trenulmeu.mobile.managedrecyclerview.ManagedRecyclerView;
import ro.trenulmeu.mobile.models.Train;
import ro.trenulmeu.mobile.models.TrainPath;
import ro.trenulmeu.mobile.timespan.TimeSpan;

/**
 * Mini-Fragment displaying basic Train Data.
 */
public class TrainPathFragment extends Fragment {

    public static final String pathAdapter_key = "pathAdapter_key";
    public static final String delayFetch_key = "delayFetch_key";

    private static final String visibleArgKey = "visibleArgKey";
    private static final String modeArgKey = "modeArgKey";
    private static final String delayArgKey = "delayArgKey";

    private View content;
    private boolean visible;
    private boolean loading;
    private boolean onlyStops;
    private int delay;

    private ImageView toggle;
    private TextView delayButton;
    private ManagedRecyclerView list;
    private PathAdapter adapter;
    private Train train;

    private FetchUnit<TrainDelay> delayFetch;
    private FetchUnit.FetchCallbacks<TrainDelay> delayCallbacks;

    public static TrainPathFragment newInstance(boolean visible, boolean onlyStops) {
        TrainPathFragment fragment = new TrainPathFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(visibleArgKey, visible);
        bundle.putBoolean(modeArgKey, onlyStops);
        bundle.putInt(delayArgKey, 0);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        train = AppContext.selectedTrain;

        delay = savedInstanceState == null
                ? 0 : savedInstanceState.getInt(delayArgKey, 0);
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

        delayButton = (TextView) view.findViewById(R.id.path_delay);
        delayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDelay();
            }
        });

        delayCallbacks = new CleanupFetchCallbacks<TrainDelay>(delayFetch_key) {
            @Override
            public void onStart() {
                loading = true;
                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUi();
                    }
                });
            }

            @Override
            public void onFail(Exception e) {
                loading = false;
                AppContext.activity.showToast(R.string.error_delay);
                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUi();
                    }
                });
            }

            @Override
            public void onSuccess(TrainDelay result) {
                loading = false;
                if (result.isError()) {
                    AppContext.activity.showToast(R.string.error_delay_available);
                } else {
                    delay = result.getDelay();
                    while (adapter == null) {
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (delay  == 0) {
                        AppContext.activity.showToast(R.string.no_delay);
                    } else {
                        AppContext.activity.showToast(delay + " "
                                + AppContext.activity.getString(R.string.minutes_delay));
                    }
                    AppContext.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateList();
                        }
                    });
                }
                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUi();
                    }
                });
            }
        };

        if (delayFetch != null) {
            delayFetch.setCallbacks(delayCallbacks);
            loading = true;
        }
        loading = false;

        /**
         * Look at this hacky thing!
         * It does the expensive task of loading the Paths in a different thread, after 0.5s.
         * The aim is to wait for the Transition to end before launching this hard task.
         * We do this complicated thing so that the GPU does not run out of memory when the User
         * decides to swipe down immediately after starting the Fragment.
         */
        final AsyncTask<Void, Void, Void> loadTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        content.setVisibility(View.GONE);
                    }
                });

                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initList();
                        updateUi();
                    }
                });

                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        content.setVisibility(View.VISIBLE);
                    }
                });
                return null;
            }
        }.execute();

        updateUi();
        return view;
    }

    private void requestDelay() {
        delayFetch = new FetchUnit<>(delayCallbacks);
        delayFetch.fetch(new Callable<Call<TrainDelay>>() {
            @Override
            public Call<TrainDelay> call() throws Exception {
                return MobileAPI.getTrainDelay(train);
            }
        });
        AppContext.cache.set(delayFetch_key, delayFetch);
    }

    private void updateUi() {
        content.setVisibility(visible ? View.VISIBLE : View.GONE);
        toggle.setImageResource(visible ? R.drawable.fold : R.drawable.expand);

        if (loading) {
            delayButton.setEnabled(false);
            delayButton.setText(R.string.loading_delays);
            delayButton.setTextColor(ContextCompat.getColor(AppContext.activity, R.color.text_muted));
        } else {
            delayButton.setEnabled(true);
            delayButton.setText(R.string.load_delays);
            delayButton.setTextColor(ContextCompat.getColor(AppContext.activity, R.color.colorPrimaryDark));
        }
    }

    private void updateList() {
        final List<TrainPath> filteredModelList = onlyStops
                ? AppContext.selectedTrain.getStops()
                : AppContext.selectedTrain.getPath();
        adapter.animateTo(filteredModelList);
        adapter.setProgress(train, TimeSpan.now(), delay);
        list.getListView().scrollToPosition(0);
    }

    private void initList() {
        if (adapter == null) {
            adapter = new PathAdapter(onlyStops
                    ? train.getStops()
                    : train.getPath());
        }
        adapter.setProgress(train, TimeSpan.now(), delay);

        list.getListView().setNestedScrollingEnabled(false);
        list.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(visibleArgKey, visible);
        outState.putBoolean(modeArgKey, onlyStops);
        outState.putInt(delayArgKey, delay);
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        adapter = AppContext.cache.get(pathAdapter_key, PathAdapter.class);
        delayFetch = AppContext.cache.get(delayFetch_key, FetchUnit.class);
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        AppContext.cache.set(pathAdapter_key, adapter);
        if (delayFetch != null) {
            delayFetch.setCallbacks(null);
        }
        System.gc();
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!AppContext.activity.isChangingConfigurations()) {
            AppContext.cache.delete(pathAdapter_key);
            AppContext.cache.delete(delayFetch_key);
        }
    }

}
