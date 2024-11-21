package System;

import java.io.Serializable;

import dataStructures.*;
import exceptions.*;

public interface RailwaySystem extends Serializable {

    void insertLine(String name, DoubleList<String> stationNames) throws ExistentLineException;

    void removeLine(String name) throws NonexistentLineException;

    Iterator<Station> consultLine(String name) throws NonexistentLineException;

    Iterator<Entry<String, Line>> consultStation(String name) throws NonexistentStationException;

    void insertSchedule(String lineName, String train, DoubleList<String[]> stationTime)
    throws NonexistentLineException, InvalidScheduleException;

    void removeSchedule(String lineName, String[] stationAndTime)
    throws NonexistentLineException, NonexistentScheduleException;

    Iterator<Entry<Time, Schedule>> consultSchedules(String lineName, String stationName)
    throws NonexistentLineException, NonexistentStationException;

    Iterator<Entry<String, Time>> stationTrains(String name) throws NonexistentStationException;

    Schedule bestSchedule(String lineName, String departure, String destination, String timeOfArrival)
    throws NonexistentLineException, NonexistentStationException, ImpossibleRouteException;
}
