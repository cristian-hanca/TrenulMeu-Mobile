package ro.trenulmeu.mobile.filters;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ro.trenulmeu.mobile.filters.adapters.MultiSelectAdapter;
import ro.trenulmeu.mobile.filters.models.CheckItem;
import ro.trenulmeu.mobile.managedrecyclerview.ManagedRecyclerView;

public class SelectDialog<T> extends DialogFragment {

    private Interaction<T> interaction = null;
    private List<CheckItem<T>> items = new ArrayList<>();

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
        updateUI();
        return view;
    }

    public void setInteraction(Interaction<T> interaction) {
        if (interaction != null) {
            this.interaction = interaction;
            //updateUI();
        }
    }

    private void updateUI() {
        adapter = new MultiSelectAdapter<>(interaction.inputItems());
        list.setAdapter(adapter);
    }

   public interface Interaction<T> {
       @DrawableRes int getIcon();
       @StringRes int getTitle();
       List<CheckItem<T>> inputItems();
       void onDone();
   }

}
