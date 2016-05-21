package ro.trenulmeu.mobile.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.concurrent.Callable;

import retrofit2.Call;
import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.Constants;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.api.MobileAPI;
import ro.trenulmeu.mobile.api.fetcher.DataBaseFetch;
import ro.trenulmeu.mobile.api.fetcher.FetchUnit;
import ro.trenulmeu.mobile.helpers.CleanupDatabaseFetchCallbacks;
import ro.trenulmeu.mobile.helpers.CleanupFetchCallbacks;
import ro.trenulmeu.mobile.helpers.DatabaseHelpers;
import ro.trenulmeu.mobile.helpers.NetworkHelpers;
import ro.trenulmeu.mobile.models.DataBaseStatus;

/**
 * Splash Fragment.
 * Used to download / update the Database and initialize tha Application.
 */
public class SplashFragment extends Fragment {

    private static final String serverStatusFetch_key = "serverStatusFetch_key";
    private static final String dataBaseFetch_key = "dataBaseFetch_key";

    private DataBaseStatus serverStatus;
    private FetchUnit<DataBaseStatus> serverStatusFetch;
    private FetchUnit.FetchCallbacks<DataBaseStatus> serverStatusCallbacks;

    private DataBaseFetch dataBaseFetch;
    private DataBaseFetch.Callbacks dataBaseFetchCallback;

    private ProgressDialog progressDialog;

    // Required empty constructor.
    public SplashFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        AppContext.activity.lockMode(true);
        AppContext.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(AppContext.activity);
                progressDialog.setIcon(R.drawable.download);
                progressDialog.setMessage(AppContext.activity.getString(R.string.downloading_database));
                progressDialog.setIndeterminate(true);
                progressDialog.setMax(100);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            }
        });
        // Leave 100ms for Activity to create the Dialog.
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (AppContext.db != null) {
            return view;
        }

        // If it is the first time, we do checks if we have any reason to be in this Fragment.
        // If the DB is OK and we cannot check for Updates, we must go back to Main.
        // But we cannot do that instantly, we must wait a bit, so we add a Sleep.
        // This "hack" also allows the use of a Splash Screen.
        if (savedInstanceState == null) {
            AsyncTask<Void, Void, Void> hackyTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    init();
                    return null;
                }
            }.execute();
        } else {
            init();
        }

        return view;
    }

    private void init() {
        if (NetworkHelpers.isNetworkAvailable()) {
            if (serverStatusFetch != null) {
                checkServerStatus();
            } else if (dataBaseFetch != null) {
                downloadDb();
            } else if (DatabaseHelpers.fileExists()) {
                checkServerStatus();
            } else {
                downloadDb();
            }
        } else if (DatabaseHelpers.fileExists()) {
            openDb(true);
        } else {
            displayMsg(R.string.error_database_no_internet, true);
        }
    }

    private void checkServerStatus() {
        if (serverStatus == null) {
            serverStatusCallbacks = new CleanupFetchCallbacks<DataBaseStatus>(serverStatusFetch_key) {
                @Override
                public void onStart() { }

                @Override
                public void onFail(Exception e) {
                    openDb(true);
                }

                @Override
                public void onSuccess(DataBaseStatus result) {
                    serverStatus = result;
                    AppContext.cache.set(Constants.serverStatus_key, serverStatus);
                    checkIfNewer();
                }
            };

            if (serverStatusFetch == null) {
                serverStatusFetch = new FetchUnit<>(serverStatusCallbacks);
                serverStatusFetch.fetch(new Callable<Call<DataBaseStatus>>() {
                    @Override
                    public Call<DataBaseStatus> call() throws Exception {
                        return MobileAPI.getStatus();
                    }
                });
                AppContext.cache.set(serverStatusFetch_key, serverStatusFetch);
            } else {
                serverStatusFetch.setCallbacks(serverStatusCallbacks);
            }
        } else {
            checkIfNewer();
        }
    }

    private void downloadDb() {
        dataBaseFetchCallback = new CleanupDatabaseFetchCallbacks(dataBaseFetch_key) {
            @Override
            public void onStart() {
                if (!progressDialog.isShowing()) {
                    AppContext.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.show();
                        }
                    });
                }
            }

            @Override
            public void onFail(Exception e) {
                DatabaseHelpers.delete();
                displayMsg(R.string.error_database_download, true);
            }

            @Override
            public void onSuccess(Void result) {
                if (progressDialog.isShowing()) {
                    AppContext.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                }
                openDb(true);
            }

            @Override
            public void onProgress(final int progress) {
                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!progressDialog.isShowing()) {
                            progressDialog.show();
                        }
                        progressDialog.setIndeterminate(false);
                        progressDialog.setProgress(progress);
                    }
                });
            }
        };

        if (dataBaseFetch == null) {
            dataBaseFetch = new DataBaseFetch(MobileAPI.baseURL
                    + Constants.databaseDownloadPath, DatabaseHelpers.getFile());
            dataBaseFetch.setCallbacks(dataBaseFetchCallback);
            dataBaseFetch.fetch();

            AppContext.cache.set(dataBaseFetch_key, dataBaseFetch);
        } else {
            if (!progressDialog.isShowing()) {
                AppContext.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.show();
                    }
                });
            }
            dataBaseFetch.setCallbacks(dataBaseFetchCallback);
        }
    }

    private void checkIfNewer() {
        if (!openDb(false)) {
            return;
        }

        DataBaseStatus status = AppContext.db.getDataBaseStatusDao().queryBuilder().list().get(0);
        if (status.getDate().before(serverStatus.getDate())) {
            AppContext.db = null;
            DatabaseHelpers.delete();
            downloadDb();
        } else {
            checkValidity();
            returnToMain();
        }
    }

    private void checkValidity() {
        DataBaseStatus status = AppContext.db.getDataBaseStatusDao().queryBuilder().list().get(0);
        if (status.getValidTo().before(new Date())) {
            displayMsg(R.string.error_database_expired, false);
        }
    }

    private boolean openDb(boolean checkValidity) {
        try {
            AppContext.db = DatabaseHelpers.open();

            // Check if it works...
            AppContext.db.getDataBaseStatusDao().queryBuilder().list();

            if (checkValidity) {
                checkValidity();
                returnToMain();
            }

            return true;
        } catch (Exception e) {
            AppContext.db = null;
            DatabaseHelpers.delete();
            if (NetworkHelpers.isNetworkAvailable()) {
                downloadDb();
            } else {
                displayMsg(R.string.error_database_open, true);
            }
        }

        return false;
    }

    private void displayMsg(@StringRes final int msg, final boolean fatal) {
        AppContext.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(AppContext.activity);
                alert.setIcon(fatal ? R.drawable.error : R.drawable.info);
                alert.setTitle(fatal ? R.string.error : R.string.info);
                alert.setMessage(msg);
                alert.setPositiveButton(fatal ? R.string.exit : R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        returnToMain();
                    }
                });
                alert.show();
            }
        });
    }

    private void returnToMain() {
        AppContext.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AppContext.activity.returnToMain();
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        serverStatus = AppContext.cache.get(Constants.serverStatus_key, DataBaseStatus.class);
        serverStatusFetch = AppContext.cache.get(serverStatusFetch_key, FetchUnit.class);
        dataBaseFetch = AppContext.cache.get(dataBaseFetch_key, DataBaseFetch.class);
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        AppContext.activity.lockMode(false);
        if (serverStatusFetch != null) {
            serverStatusFetch.setCallbacks(null);
        }
        if (dataBaseFetch != null) {
            dataBaseFetch.setCallbacks(null);
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDetach();
    }

}
