package ro.trenulmeu.mobile.helpers;

import ro.trenulmeu.mobile.models.Train;
import ro.trenulmeu.mobile.timespan.TimeSpan;

public class TimeHelpers {

    public static TimeSpan getCorrectFrom(Train train) {
        return getCorrectTime(train.getFromTime(), train.getFromTimeOffset());
    }

    public static TimeSpan getCorrectTo(Train train) {
        return getCorrectTime(train.getToTime(), train.getToTimeOffset());
    }

    public static TimeSpan getCorrectTime(TimeSpan time, byte offset) {
        return new TimeSpan(time).add(offset, TimeSpan.TimeUnits.HOUR);
    }

    /**
     * Static Class.
     */
    private TimeHelpers() {}

}
