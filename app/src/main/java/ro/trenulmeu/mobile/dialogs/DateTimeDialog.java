package ro.trenulmeu.mobile.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.helpers.FragmentHelpers;

public class DateTimeDialog extends DialogFragment {

    private static final String datePickerTag = "datePickerTag";
    private static final String timePickerTag = "timePickerTag";

    private TextView dateText;
    private TextView timeText;

    private DateTime dateTime = null;
    private Callbacks callbacks;

    private DatePickerDialog dDiag;
    private TimePickerDialog tDiag;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (dateTime == null) {
            dateTime = DateTime.now();
        }

        View view = inflater.inflate(R.layout.dialog_datetimer, container, false);
        dateText = (TextView) view.findViewById(R.id.datetime_date_text);
        timeText = (TextView) view.findViewById(R.id.datetime_time_text);

        view.findViewById(ro.trenulmeu.mobile.filters.R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                callbacks.onDone(dateTime);
            }
        });

        view.findViewById(R.id.datetime_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTime = DateTime.now();
                updateUI();
            }
        });
        view.findViewById(R.id.datetime_p15m).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTime = dateTime.plusMinutes(15);
                updateUI();
            }
        });
        view.findViewById(R.id.datetime_p1h).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTime = dateTime.plusHours(1);
                updateUI();
            }
        });
        view.findViewById(R.id.datetime_p1d).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTime = dateTime.plusDays(1);
                updateUI();
            }
        });
        view.findViewById(R.id.datetime_m15m).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTime = dateTime.minusMinutes(15);
                updateUI();
            }
        });
        view.findViewById(R.id.datetime_m1h).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTime = dateTime.minusHours(1);
                updateUI();
            }
        });
        view.findViewById(R.id.datetime_m1d).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTime = dateTime.minusDays(1);
                updateUI();
            }
        });

        dDiag = FragmentHelpers.findByTag(datePickerTag, DatePickerDialog.class, new DatePickerDialog());
        dDiag.setCallbacks(new DatePickerDialog.Callbacks() {
            @Override
            public void onDone(DateTime result) {
                dateTime = result;
                updateUI();
            }
        });
        view.findViewById(R.id.datetime_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDiag.setDateTime(dateTime);
                dDiag.show(AppContext.activity.getSupportFragmentManager(), datePickerTag);
            }
        });

        tDiag = FragmentHelpers.findByTag(timePickerTag, TimePickerDialog.class, new TimePickerDialog());
        tDiag.setCallbacks(new TimePickerDialog.Callbacks() {
            @Override
            public void onDone(DateTime result) {
                dateTime = result;
                updateUI();
            }
        });
        view.findViewById(R.id.datetime_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tDiag.setDateTime(dateTime);
                tDiag.show(AppContext.activity.getSupportFragmentManager(), timePickerTag);
            }
        });

        updateUI();
        return view;
    }

    private void updateUI() {
        dateText.setText(dateTime.toString("dd/MM/yy"));
        timeText.setText(dateTime.toString("HH:mm"));
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
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
        void onDone(DateTime dateTime);
    }

}
