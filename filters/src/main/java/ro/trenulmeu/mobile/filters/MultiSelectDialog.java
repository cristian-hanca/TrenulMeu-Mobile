package ro.trenulmeu.mobile.filters;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ro.trenulmeu.mobile.filters.adapters.MultiSelectAdapter;
import ro.trenulmeu.mobile.filters.models.CheckItem;
import ro.trenulmeu.mobile.managedrecyclerview.ManagedRecyclerView;

public class MultiSelectDialog<T> extends DialogFragment {

    private Input<T> input = null;
    private Callbacks<T> callbacks = null;

    private ManagedRecyclerView list;
    private MultiSelectAdapter<T> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select, container, false);
        list = (ManagedRecyclerView) view.findViewById(R.id.list);

        ((ImageView) view.findViewById(R.id.icon)).setImageResource(input.getIcon());
        ((TextView) view.findViewById(R.id.title)).setText(input.getTitle());

        view.findViewById(R.id.all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setAll(true);
            }
        });
        view.findViewById(R.id.none).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setAll(false);
            }
        });
        view.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                callbacks.onDone(adapter.getData());
            }
        });

        updateUI();
        return view;
    }

    public void setInput(Input<T> input) {
        if (input != null) {
            this.input = input;
        }
    }

    public void setCallbacks(Callbacks<T> callbacks) {
        if (callbacks != null) {
            this.callbacks = callbacks;
        }
    }

    private void updateUI() {
        adapter = new MultiSelectAdapter<>(input.inputItems());
        list.setAdapter(adapter);
    }

    @Override
    public void onDestroyView()
    {
        Dialog dialog = getDialog();

        // Work around bug: http://code.google.com/p/android/issues/detail?id=17423
        if ((dialog != null) && getRetainInstance())
            dialog.setDismissMessage(null);

        super.onDestroyView();
    }

    public interface Input<T> {
        @DrawableRes int getIcon();
        @StringRes int getTitle();
        List<CheckItem<T>> inputItems();
    }

    public interface Callbacks<T> {
        void onDone(List<CheckItem<T>> items);
    }

}
