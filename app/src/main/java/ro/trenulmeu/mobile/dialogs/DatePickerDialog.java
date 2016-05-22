package ro.trenulmeu.mobile.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerDialog extends DialogFragment
        implements android.app.DatePickerDialog.OnDateSetListener {

    private DateTime dateTime = null;
    private Callbacks callbacks;

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (dateTime == null) {
            dateTime = DateTime.now();
        }

        final Calendar c = dateTime.toCalendar(Locale.ENGLISH);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new android.app.DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (callbacks != null) {
            callbacks.onDone(new DateTime(year, month + 1, day,
                    dateTime.getHourOfDay(), dateTime.getMinuteOfHour()));
        }
    }

    public interface Callbacks {
        void onDone(DateTime dateTime);
    }
}
