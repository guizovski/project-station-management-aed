package System;

import java.io.Serializable;

import dataStructures.*;
import System.exceptions.*;

/**
 * Interface representing the complete railway system.
 * Manages lines, stations, and schedules across the entire network.
 */
public interface RailwaySystem extends Serializable {
    /**
     * Creates a new line with given stations
     * @param name line name
     * @param stationNames list of station names
     * @throws ExistentLineException if line already exists
     */
    void insertLine(String name, DoubleList<String> stationNames) throws ExistentLineException;

    /**
     * Removes a line from the system
     * @param name line to remove
     * @throws NonexistentLineException if line doesn't exist
     */
    void removeLine(String name) throws NonexistentLineException;

    /**
     * Gets iterator over stations in a line
     * @param name line name
     * @return iterator of stations
     * @throws NonexistentLineException if line doesn't exist
     */
    SafeStationIterator consultLine(String name) throws NonexistentLineException;

    /**
     * Gets iterator over lines passing through a station
     * @param name station name
     * @return iterator of lines
     * @throws NonexistentStationException if station doesn't exist
     */
    SafeLineIterator<String> consultStation(String name) throws NonexistentStationException;

    /**
     * Creates a new schedule in a line
     * @param lineName line name
     * @param train train identifier
     * @param stations station names
     * @param times schedule times
     * @throws NonexistentLineException if line doesn't exist
     * @throws InvalidScheduleException if schedule is invalid
     */
    void insertSchedule(String lineName, String train, DoubleList<String> stations, DoubleList<String[]> times)
            throws NonexistentLineException, InvalidScheduleException;

    /**
     * Removes a schedule from a line
     * @param lineName line name
     * @param stationName station name
     * @param departure schedule time
     * @throws NonexistentLineException if line doesn't exist
     * @throws NonexistentScheduleException if schedule doesn't exist
     */
    void removeSchedule(String lineName, String stationName, String[] departure)
            throws NonexistentLineException, NonexistentScheduleException;

    /**
     * Gets schedules departing from a station in a line
     * @param lineName line name
     * @param stationName station name
     * @return iterator of schedules
     * @throws NonexistentLineException if line doesn't exist
     * @throws NonexistentStationException if station doesn't exist
     */
    SafeScheduleIterator<Time> consultSchedules(String lineName, String stationName)
            throws NonexistentLineException, NonexistentStationException;

    /**
     * Gets all trains passing through a station
     * @param name station name
     * @return iterator of train information
     * @throws NonexistentStationException if station doesn't exist
     */
    SafeLineIterator<TimeTrainPairClass> stationTrains(String name) throws NonexistentStationException;

    /**
     * Finds best schedule between stations before given time
     * @param lineName line name
     * @param departure departure station name
     * @param destination destination station name
     * @param timeOfArrival arrival time limit
     * @return best matching schedule
     * @throws NonexistentLineException if line doesn't exist
     * @throws NonexistentStationException if station doesn't exist
     * @throws ImpossibleRouteException if no valid route exists
     */
    SafeSchedule bestSchedule(String lineName, String departure, String destination, String[] timeOfArrival)
            throws NonexistentLineException, NonexistentStationException, ImpossibleRouteException;
}
