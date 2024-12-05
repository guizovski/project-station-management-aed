package System;

import dataStructures.*;
import System.exceptions.ImpossibleRouteException;
import System.exceptions.InvalidScheduleException;
import System.exceptions.NonexistentScheduleException;
import System.exceptions.NonexistentStationException;

import java.io.Serializable;

/**
 * Interface representing a railway line with stations and schedules.
 * Manages line operations including schedule management and station information.
 */
public interface Line extends Comparable<Line>, Serializable {

    /**
     * Gets the name of the line
     * @return line name
     */
    String getName();

    /**
     * Gets list of all stations in the line
     * @return List of stations
     */
    List<Station> getStations();

    /**
     * Gets iterator over all stations in the line
     * @return Iterator of stations
     */
    Iterator<Station> getStationsIt();

    /**
     * Gets iterator over all schedules in the line
     * @return Iterator of schedule entries
     */
    Iterator<Entry<String, Schedule>> getScheduleIt();

    /**
     * Checks if a station is a terminal station (first or last)
     * @param station station to check
     * @return true if station is terminal, false otherwise
     */
    boolean isNotDepartingStation(Station station);

    /**
     * Inserts a new schedule into the line
     * @param train train identifier
     * @param scheduleStations list of stations in schedule
     * @param scheduleTimes list of times for each station
     * @throws InvalidScheduleException if schedule is invalid
     */
    void insertSchedule(String train, DoubleList<Station> scheduleStations, DoubleList<Time> scheduleTimes)
            throws InvalidScheduleException;

    /**
     * Removes a schedule from the line
     * @param station departure station
     * @param time departure time
     * @throws NonexistentScheduleException if schedule doesn't exist
     */
    void removeSchedule(Station station, Time time) throws NonexistentScheduleException;

    /**
     * Gets all schedules departing from a station
     * @param station departure station
     * @return Iterator of schedule entries
     * @throws NonexistentStationException if station doesn't exist
     */
    Iterator<Entry<Time, Schedule>> consultSchedules(Station station) throws NonexistentStationException;

    /**
     * Finds best schedule between two stations before given time
     * @param departure departure station
     * @param destination destination station
     * @param time arrival time limit
     * @return best matching schedule
     * @throws NonexistentStationException if station doesn't exist
     * @throws ImpossibleRouteException if no valid route exists
     */
    Schedule getBestSchedule(Station departure, Station destination, Time time)
            throws NonexistentStationException, ImpossibleRouteException;

    /**
     * Checks if a station exists in this line
     * @param station station to check
     * @return true if station exists in line, false otherwise
     */
    boolean isNonexistentStation(Station station);
}
