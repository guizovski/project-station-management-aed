package System;

import java.io.Serial;

public record TimeClass(String hour, String minute) implements Time {

    @Serial
    private static final long serialVersionUID = 0L;

    @Override
    public String getTime() {
        return hour + ":" + minute;
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
