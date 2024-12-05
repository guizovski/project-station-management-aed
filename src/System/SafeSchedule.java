package System;

import java.io.Serializable;

/**
 * Safe interface for Schedule that exposes only necessary methods to Main
 */
public interface SafeSchedule extends Comparable<Schedule>, Serializable {

    /**
     * Gets the time for a specific station in this schedule
     * @param station station to get time for
     * @return time at the station
     */
    Time getStationTime(SafeStation station);

    /**
     * Gets train identifier
     * @return train identifier
     */
    int getTrain();

    /**
     * Gets iterator over stations in schedule
     * @return stations iterator
     */
    SafeStationIterator getStationIt();

}
