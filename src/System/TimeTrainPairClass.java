package System;

import java.io.Serial;

/**
 * Class representing a combination of time and train identifier.
 * Used to track train passages through stations.
 */
public class TimeTrainPairClass implements TimeTrainPair {

    @Serial
    static final long serialVersionUID = 0L;

    /** Time of train passage */
    private final Time time;
    /** Unique train identifier */
    private final int train;

    /**
     * Creates new time-train pair
     * @param time time of passage
     * @param train train identifier
     */
    public TimeTrainPairClass(Time time, String train) {
        this.time = time;
        this.train = Integer.parseInt(train);
    }

    @Override
    public Time getTime() { return time; }

    @Override
    public int getTrain() { return train; }

    @Override
    public int compareTo(TimeTrainPairClass other) {
        int timeCompare = this.time.compareTo(other.getTime());
        if(timeCompare != 0)
            return timeCompare;
        return this.train - other.getTrain();
    }
}
