package System;

import dataStructures.*;
import System.exceptions.ImpossibleRouteException;
import System.exceptions.InvalidScheduleException;
import System.exceptions.NonexistentScheduleException;
import System.exceptions.NonexistentStationException;

import java.io.Serial;

public class LineClass implements Line, SafeLine {

    @Serial
    static final long serialVersionUID = 0L;

    /** The line's unique name */
    protected String name;
    /** Collection of schedules for this line, indexed by train ID */
    protected OrderedDictionary<String, Schedule> schedules;
    /** Ordered list of stations along this line */
    protected List<Station> stations;

    /**
     * Creates new line with given name and stations
     * @param name unique name for the line
     * @param stations ordered list of stations on this line
     */
    public LineClass(String name, DoubleList<Station> stations) {
        this.name = name;
        this.stations = stations;
        this.schedules = new AVLTree<String, Schedule>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void insertSchedule(String train, DoubleList<Station> scheduleStations, DoubleList<Time> scheduleTimes)
            throws InvalidScheduleException {

        if(isNotDepartingStation(scheduleStations.getFirst()))
            throw new InvalidScheduleException();

        int stationsValidated = validateSchedule(scheduleStations, scheduleTimes);

        Schedule schedule = new ScheduleClass();
        schedule.addTrain(train);
        int counter = 0;

        while(counter < stationsValidated) {
            schedule.addSchedule(scheduleStations.get(counter), scheduleTimes.get(counter));
            scheduleStations.get(counter).addTrain(train, scheduleTimes.get(counter), this);
            counter++;
        }
        schedules.insert(train, schedule);
    }

    @Override
    public void removeSchedule(Station station, Time time) throws NonexistentScheduleException {
        Iterator<Entry<String, Schedule>> schedules = getScheduleIt();
        boolean found = false;

        while (schedules.hasNext() && !found) {
            Entry<String, Schedule> entry = schedules.next();
            Schedule schedule = entry.getValue();

            if(schedule.getDepartureStation().equals(station))
                if(schedule.getDepartureTime().compareTo(time) == 0) {
                    Schedule tmp = this.schedules.remove(entry.getKey());
                    tmp.deleteSchedule();
                    found = true;
                }
        }
        if (!found)
            throw new NonexistentScheduleException();
    }

    @Override
    public Iterator<Entry<Time, Schedule>> consultSchedules(Station station)
            throws NonexistentStationException {
        if(isNotDepartingStation(station))
            throw new NonexistentStationException();

        Iterator<Entry<String, Schedule>> it = getScheduleIt();
        OrderedDoubleList<Time, Schedule> schedules = new OrderedDoubleList<Time, Schedule>();

        while(it.hasNext()) {
            Entry<String, Schedule> entry = it.next();
            if(entry.getValue().getDepartureStation().equals(station)) {
                schedules.insert(entry.getValue().getDepartureTime(), entry.getValue());
            }
        }

        return schedules.iterator();
    }

    @Override
    public Schedule getBestSchedule(Station departure, Station destination, Time time)
            throws NonexistentStationException, ImpossibleRouteException {
        Iterator<Entry<String, Schedule>> it = schedules.iterator();
        Schedule bestSchedule = null;

        if(isNonexistentStation(departure))
            throw new NonexistentStationException();

        if(isNonexistentStation(destination))
            throw new ImpossibleRouteException();

        while(it.hasNext()) {
            Entry<String, Schedule> entry = it.next();
            Schedule tmp = entry.getValue();
            if(tmp.existsStation(departure)
                    && tmp.existsStation(destination)
                    && tmp.isRightOrder(departure, destination)
                    && tmp.getStationTime(destination).compareTo(time) <= 0) {
                if(bestSchedule == null)
                    bestSchedule = tmp;
                else if(tmp.getStationTime(destination).compareTo(bestSchedule.getStationTime(destination)) > 0)
                    bestSchedule = tmp;
            }
        }
        if(bestSchedule == null)
            throw new ImpossibleRouteException();

        return bestSchedule;
    }

    @Override
    public List<Station> getStations() {
        return this.stations;
    }

    @Override
    public Iterator<Station> getStationsIt() {
        return stations.iterator();
    }

    @Override
    public Iterator<Entry<String, Schedule>> getScheduleIt() {
        return schedules.iterator();
    }

    @Override
    public boolean isNotDepartingStation(Station station) {
        return !this.stations.getFirst().equals(station) && !this.stations.getLast().equals(station);
    }

    @Override
    public boolean isNonexistentStation(Station station) {
        return stations.find(station) == -1;
    }

    /**
     * Validates a schedule by checking station order and timing
     * @param scheduleStations list of stations in the schedule
     * @param scheduleTimes list of times corresponding to each station
     * @return number of validated stations
     * @throws InvalidScheduleException if schedule is invalid
     */
    private int validateSchedule(DoubleList<Station> scheduleStations, DoubleList<Time> scheduleTimes)
            throws InvalidScheduleException {

        int currentPos = (scheduleStations.getFirst().equals(this.stations.getFirst())) ? 0 : this.stations.size() - 1;
        int stationsValidated = 0;

        Iterator<Station> stationsIt = scheduleStations.iterator();
        Iterator<Time> timesIt = scheduleTimes.iterator();

        while(stationsIt.hasNext()) {
            Station station = stationsIt.next();
            Time time = timesIt.next();

            if(scheduleStations.getFirst().equals(this.stations.getFirst())) {
                while(currentPos < this.stations.size() && !station.equals(this.stations.get(currentPos))) currentPos++;
                if(currentPos == this.stations.size()) throw new InvalidScheduleException();
            }
            else {
                while(currentPos >= 0 && !station.equals(this.stations.get(currentPos))) currentPos--;
                if(currentPos == -1) throw new InvalidScheduleException();
            }

            if(stationsValidated > 0 && !scheduleTimes.get(stationsValidated - 1).hasTravelTime(time))
                throw new InvalidScheduleException();

            if(stationsValidated > 0 && hasOvertake(station, time, scheduleStations.getFirst() ,scheduleTimes.getFirst()))
                throw new InvalidScheduleException();

            stationsValidated++;
        }
        return stationsValidated;
    }

    /**
     * Checks if there's a train overtake at a given station
     * @param station station to check for overtake
     * @param time time at the station
     * @param departureStation initial station of the schedule
     * @param departureTime departure time from initial station
     * @return true if there's an overtake, false otherwise
     */
    private boolean hasOvertake(Station station, Time time, Station departureStation,Time departureTime) {
        Iterator<Entry<String, Schedule>> it = schedules.iterator();
        boolean isForward = stations.getFirst().equals(departureStation);

        while(it.hasNext()) {
            Schedule existingSchedule = it.next().getValue();

            if(existingSchedule.getDepartureStation().equals(stations.getFirst()) == isForward) {
                if(existingSchedule.existsStation(station)) {
                    if(existingSchedule.getDepartureTime().compareTo(departureTime) <= 0) {
                        if(existingSchedule.getStationTime(station).compareTo(time) >= 0) {
                            return true;
                        }
                    }
                    else {
                        if(existingSchedule.getStationTime(station).compareTo(time) <= 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int compareTo(Line o) {
        return this.name.toUpperCase().compareTo(o.getName().toUpperCase());
    }

}
