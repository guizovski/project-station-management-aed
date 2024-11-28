package System;

import java.io.Serial;
import java.io.Serializable;

public class TimeTrainPair implements Comparable<TimeTrainPair>, Serializable {

    @Serial
    private static final long serialVersionUID = 0L;

    private final Time time;
    private final int train;

    public TimeTrainPair(Time time, String train) {
        this.time = time;
        this.train = Integer.parseInt(train);
    }

    public Time getTime() { return time; }
    public int getTrain() { return train; }

    @Override
    public int compareTo(TimeTrainPair other) {
        int timeCompare = this.time.compareTo(other.time);
        if(timeCompare != 0)
            return timeCompare;
        return this.train - other.getTrain();
    }
}
