package ro.trenulmeu.mobile.timespan;

import java.util.Calendar;

/**
 * Custom Implementation of a TimeSpan.
 * Internally stored in a Short representing number of minutes after 00:00, this Class is a very
 * efficient implementation to store Times in our application.
 */
public class TimeSpan {
    private static final short sH = 60;
    private static final short sD = 60 * 24;

    private short ticks;

    public static TimeSpan now() {
        Calendar now = Calendar.getInstance();
        return new TimeSpan(0, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
    }

    public TimeSpan() {
        ticks = 0;
    }

    public TimeSpan(TimeSpan other) {
        ticks = other.ticks;
    }

    public TimeSpan(short ticks) {
        this.ticks = ticks;
    }

    public TimeSpan(String string) {
        ticks = 0;
        boolean negate;
        if (string.charAt(0) == '-') {
            negate = true;
            string = string.substring(1);
        } else {
            negate = false;
        }

        String[] split = string.split("\\.");
        if (split.length == 2) {
            ticks += Integer.valueOf(split[0]) * sD;
            string = split[1];
        } else {
            string = split[0];
        }

        split = string.split(":");
        ticks += Integer.valueOf(split[0]) * sH + Integer.valueOf(split[1]);

        if (negate) {
            negate();
        }
    }

    public TimeSpan(int day, int hour, int minute) {
        this.ticks = (short) (day * sD + hour * sH + minute);
    }

    public TimeSpan add(TimeSpan other) {
        ticks += other.ticks;
        return this;
    }

    public TimeSpan add(int count, TimeUnits unit) {
        short multiplier =
                unit == TimeUnits.DAY ? sD :
                        unit == TimeUnits.HOUR ? sH : 1;

        ticks += count * multiplier;
        return this;
    }

    public TimeSpan subtract(TimeSpan other) {
        ticks -= other.ticks;
        return this;
    }

    public TimeSpan subtract(TimeUnits unit) {
        int ticksC = Math.abs(ticks);
        if (unit == TimeUnits.DAY) {
            return subtract(ticksC / sD, unit);
        }

        ticksC = ticksC % sD;
        if (unit == TimeUnits.HOUR) {
            return subtract(ticksC / sH, unit);
        }

        return subtract(ticksC % sH, unit);
    }

    public TimeSpan subtract(int count, TimeUnits unit) {
        short multiplier =
                unit == TimeUnits.DAY ? sD :
                        unit == TimeUnits.HOUR ? sH : 1;

        ticks -= count * multiplier;
        return this;
    }

    public TimeSpan negate() {
        ticks = (short) (ticks * -1);
        return this;
    }

    public TimeSpan absolute() {
        return ticks < 0 ? negate() : this;
    }

    public TimeSpan absoluteDay() {
        while (ticks < 0) {
            ticks += sD;
        }
        return this;
    }

    public int getCount(TimeUnits unit) {
        int ticksC = Math.abs(ticks);
        if (unit == TimeUnits.DAY) {
            return ticksC / sD;
        }

        ticksC = ticksC % sD;
        if (unit == TimeUnits.HOUR) {
            return ticksC / sH;
        }

        return ticksC % sH;
    }

    public short getTicks() {
        return ticks;
    }

    public int compareTo(TimeSpan o) {
        return ticks == o.ticks ? 0 : ticks > o.ticks ? 1 : -1;
    }

    public int compareTimeTo(TimeSpan o) {
        return new TimeSpan(this).subtract(TimeUnits.DAY).compareTo(new TimeSpan(o).subtract(TimeUnits.DAY));
    }

    public enum TimeUnits {
        DAY,
        HOUR,
        MINUTE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSpan)) return false;

        TimeSpan timeSpan = (TimeSpan) o;

        return ticks == timeSpan.ticks;

    }

    @Override
    public int hashCode() {
        return (int) ticks;
    }

    @Override
    public String toString() {
        short ticksC = (short) Math.abs(ticks);

        short days = (short) (ticksC / sD);
        ticksC = (short) (ticksC % sD);

        short hours = (short) (ticksC / sH);
        ticksC = (short) (ticksC % sH);

        short minutes = ticksC;

        StringBuilder sb = new StringBuilder();
        if (ticks < 0) sb.append('-');
        if (days != 0) {
            sb.append(days); sb.append('.');
        }
        if (hours < 10) sb.append('0'); sb.append(hours); sb.append(':');
        if (minutes < 10) sb.append('0'); sb.append(minutes);

        return sb.toString();
    }


    public String toTimeString() {
        short ticksC = (short) (Math.abs(ticks) % sD);

        short hours = (short) (ticksC / sH);
        ticksC = (short) (ticksC % sH);

        short minutes = ticksC;

        StringBuilder sb = new StringBuilder();
        if (ticks < 0) sb.append('-');
        if (hours < 10) sb.append('0'); sb.append(hours); sb.append(':');
        if (minutes < 10) sb.append('0'); sb.append(minutes);

        return sb.toString();
    }

}
