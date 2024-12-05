package System;

import java.io.Serial;

public class TimeClass implements Time {

    @Serial
    static final long serialVersionUID = 0L;

    /** Hour component of the time (00-23) */
    private final String hour;
    /** Minute component of the time (00-59) */
    private final String minute;

    public TimeClass(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public String hour() {
        return hour;
    }

    @Override
    public String minute() {
        return minute;
    }

    @Override
    public boolean hasTravelTime(Time other) {
        return this.compareTo(other) < 0;
    }

    @Override
    public int compareTo(Time o) {
        int intHour = Integer.parseInt(hour);
        int intMin = Integer.parseInt(minute);

        if (intHour - Integer.parseInt(o.hour()) == 0)
            return intMin - Integer.parseInt(o.minute());
        return intHour - Integer.parseInt(o.hour());
    }

}
