package System;

import java.io.Serializable;

import dataStructures.*;
import exceptions.ImpossibleRouteException;
import exceptions.InvalidScheduleException;
import exceptions.NonexistentScheduleException;
import exceptions.NonexistentStationException;

public interface Line extends Comparable<Line>, Serializable {

    String getName();

    DoubleList<Station> getStations();

    Iterator<Station> getStationsIt();

    Iterator<Entry<String, Schedule>> getScheduleIt();

    boolean isDepartingStation(Station station);

    void insertSchedule(String train, DoubleList<Station> scheduleStations, DoubleList<Time> scheduleTimes) throws InvalidScheduleException;

    void removeSchedule(Station station, Time time) throws NonexistentScheduleException;

    Iterator<Entry<Time, Schedule>> consultSchedules(Station station) throws NonexistentStationException;

    Schedule getBestSchedule(Station departure, Station destination, Time time) throws NonexistentStationException, ImpossibleRouteException;

    boolean existsStation(Station station);

}
