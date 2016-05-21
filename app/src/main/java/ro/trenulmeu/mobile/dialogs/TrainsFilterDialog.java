package ro.trenulmeu.mobile.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.Constants;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.filters.MultiSelectDialog;
import ro.trenulmeu.mobile.filters.models.CheckItem;
import ro.trenulmeu.mobile.models.TrainOperator;
import ro.trenulmeu.mobile.models.TrainType;

public class TrainsFilterDialog extends DialogFragment {

    private TextView operationsStatus;
    private TextView typesStatus;

    private Callbacks callbacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_train_filter, container, false);

        operationsStatus = (TextView) view.findViewById(R.id.filter_train_operators_count);
        typesStatus = (TextView) view.findViewById(R.id.filter_train_types_count);

        view.findViewById(ro.trenulmeu.mobile.filters.R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                callbacks.onDone();
            }
        });

        view.findViewById(R.id.filter_train_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppContext.trainFilter.setOnlyRunning(!AppContext.trainFilter.getOnlyRunning());
            }
        });

        view.findViewById(R.id.filter_train_operators).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiSelectDialog<TrainOperator> msd = new MultiSelectDialog<>();
                msd.setInput(new MultiSelectDialog.Input<TrainOperator>() {
                    @Override
                    public int getIcon() {
                        return R.drawable.filter;
                    }

                    @Override
                    public int getTitle() {
                        return R.string.operators;
                    }

                    @Override
                    public List<CheckItem<TrainOperator>> inputItems() {
                        return AppContext.trainFilter.getOperators();
                    }
                });
                msd.setCallbacks(new MultiSelectDialog.Callbacks<TrainOperator>() {
                    @Override
                    public void onDone(List<CheckItem<TrainOperator>> checkItems) {
                        AppContext.trainFilter.setOperators(checkItems);
                        updateUI();
                    }
                });
                msd.show(AppContext.activity.getSupportFragmentManager(),
                        Constants.dialogTrainFilterOperators);
            }
        });

        view.findViewById(R.id.filter_train_types).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiSelectDialog<TrainType> msd = new MultiSelectDialog<>();
                msd.setInput(new MultiSelectDialog.Input<TrainType>() {
                    @Override
                    public int getIcon() {
                        return R.drawable.filter;
                    }

                    @Override
                    public int getTitle() {
                        return R.string.types;
                    }

                    @Override
                    public List<CheckItem<TrainType>> inputItems() {
                        return AppContext.trainFilter.getTypes();
                    }
                });
                msd.setCallbacks(new MultiSelectDialog.Callbacks<TrainType>() {
                    @Override
                    public void onDone(List<CheckItem<TrainType>> checkItems) {
                        AppContext.trainFilter.setTypes(checkItems);
                        updateUI();
                    }
                });
                msd.show(AppContext.activity.getSupportFragmentManager(),
                        Constants.dialogTrainFilterTypes);
            }
        });

        updateUI();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void updateUI() {
        long opCnt = Stream.of(AppContext.trainFilter.getOperators())
                .filter(new Predicate<CheckItem<TrainOperator>>() {
                    @Override
                    public boolean test(CheckItem<TrainOperator> value) {
                        return value.isCheck();
                    }
                })
                .count();
        operationsStatus.setText(opCnt + " / " + AppContext.trainFilter.getOperators().size());
        operationsStatus.setTextColor(ContextCompat.getColor(AppContext.activity, opCnt == 0
                ? R.color.red : R.color.text));

        long tyCnt = Stream.of(AppContext.trainFilter.getTypes())
                .filter(new Predicate<CheckItem<TrainType>>() {
                    @Override
                    public boolean test(CheckItem<TrainType> value) {
                        return value.isCheck();
                    }
                })
                .count();
        typesStatus.setText(tyCnt + " / " + AppContext.trainFilter.getTypes().size());
        typesStatus.setTextColor(ContextCompat.getColor(AppContext.activity, tyCnt == 0
                ? R.color.red : R.color.text));
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
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

    public interface Callbacks {
        void onDone();
    }

}
