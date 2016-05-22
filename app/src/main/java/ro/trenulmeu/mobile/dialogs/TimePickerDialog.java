package ro.trenulmeu.mobile.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Locale;

public class TimePickerDialog extends DialogFragment
        implements android.app.TimePickerDialog.OnTimeSetListener {

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
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new android.app.TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (callbacks != null) {
            callbacks.onDone(new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
                    dateTime.getDayOfMonth(), hourOfDay, minute));
        }
    }

    public interface Callbacks {
        void onDone(DateTime dateTime);
    }
}
