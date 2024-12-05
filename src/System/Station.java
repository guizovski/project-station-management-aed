package System;

import dataStructures.*;

import java.io.Serializable;

/**
 * Interface representing a railway station.
 * Manages lines and trains passing through the station.
 */
public interface Station extends Comparable<Station>, Serializable {
    /**
     * Gets station name
     * @return station name
     */
    String getName();

    /**
     * Adds a line passing through this station
     * @param line line to add
     */
    void addLine(Line line);

    /**
     * Removes a line from this station
     * @param line line to remove
     */
    void removeLine(Line line);

    /**
     * Adds a train passing through this station
     * @param train train identifier
     * @param time time of passage
     * @param line line of the train
     */
    void addTrain(String train, Time time, Line line);

    /**
     * Removes a train from this station
     * @param train train identifier to remove
     */
    void removeTrain(int train);

    /**
     * Gets iterator over trains passing through station
     * @return iterator of train entries
     */
    Iterator<Entry<TimeTrainPairClass, Line>> consultTrains();

    /**
     * Gets iterator over lines passing through station
     * @return iterator of line entries
     */
    Iterator<Entry<String, Line>> consultLines();

    /**
     * Checks if station has no lines
     * @return true if station has no lines
     */
    boolean isAbandoned();
}
