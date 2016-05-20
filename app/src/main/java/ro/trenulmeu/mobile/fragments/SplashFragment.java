package ro.trenulmeu.mobile.fragments;

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

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.helpers.DatabaseHelpers;
import ro.trenulmeu.mobile.helpers.NetworkHelpers;
import ro.trenulmeu.mobile.models.DataBaseStatus;

/**
 * Splash Fragment.
 * Used to download / update the Database and initialize tha Application.
 */
public class SplashFragment extends Fragment {

    // Required empty constructor.
    public SplashFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ActionBar actionBar = AppContext.activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
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

        } else {
            openDb();
        }
    }

    private void openDb() {
        try {
            AppContext.db = DatabaseHelpers.open();
            checkValidity();
        } catch (Exception e) {
            AppContext.db = null;
            DatabaseHelpers.delete();
            displayMsg(R.string.error_database_open, true);
        }
    }

    private void checkValidity() {
        DataBaseStatus status = AppContext.db.getDataBaseStatusDao().queryBuilder().list().get(0);
        if (status.getValidTo().before(new Date())) {
            displayMsg(R.string.error_database_expired, false);
        }
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        ActionBar actionBar = AppContext.activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
        super.onDetach();
    }

}
