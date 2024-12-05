package System;

import java.io.Serializable;

/**
 * Interface representing a train schedule.
 * Contains timing information for a train's journey through stations.
 */
public interface Schedule extends Comparable<Schedule>, Serializable {
    /**
     * Gets arrival/departure time for a station
     * @param station station to get time for
     * @return time at the station
     */
    Time getStationTime(Station station);

    /**
     * Adds a station and its time to the schedule
     * @param station station to add
     * @param time time at the station
     */
    void addSchedule(Station station, Time time);

    /**
     * Sets the train identifier for this schedule
     * @param train train identifier
     */
    void addTrain(String train);

    /**
     * Gets the first station in the schedule
     * @return departure station
     */
    Station getDepartureStation();

    /**
     * Gets departure time from first station
     * @return departure time
     */
    Time getDepartureTime();

    /**
     * Gets train identifier
     * @return train identifier
     */
    int getTrain();

    /**
     * Gets iterator over stations in schedule
     * @return iterator of stations
     */
    SafeStationIterator getStationIt();

    /**
     * Checks if station exists in schedule
     * @param station station to check
     * @return true if station exists in schedule
     */
    boolean existsStation(Station station);

    /**
     * Removes this schedule from all its stations
     */
    void deleteSchedule();

    /**
     * Checks if stations are in correct order in schedule
     * @param departure first station
     * @param arrival second station
     * @return true if stations are in correct order
     */
    boolean isRightOrder(Station departure, Station arrival);
}
