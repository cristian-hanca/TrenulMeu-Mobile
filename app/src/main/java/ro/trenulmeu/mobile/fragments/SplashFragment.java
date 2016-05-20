package ro.trenulmeu.mobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;

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

        return view;
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
