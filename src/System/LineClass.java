package System;

import dataStructures.*;
import exceptions.ImpossibleRouteException;
import exceptions.InvalidScheduleException;
import exceptions.NonexistentScheduleException;
import exceptions.NonexistentStationException;

import java.io.Serial;

public class LineClass implements Line {

    @Serial
    private static final long serialVersionUID = 0L;

    protected String name;
    protected OrderedDoubleList<String, Schedule> schedules;
    protected DoubleList<Station> stations;

    public LineClass(String name, DoubleList<Station> stations) {
        this.name = name;
        this.stations = stations;
        this.schedules = new OrderedDoubleList<String, Schedule>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void insertSchedule(String train, DoubleList<Station> scheduleStations, DoubleList<Time> scheduleTimes)
            throws InvalidScheduleException {

        if(!isDepartingStation(scheduleStations.getFirst()))
            throw new InvalidScheduleException();

        int stationsValidated = validateSchedule(scheduleStations, scheduleTimes);

        Schedule schedule = new ScheduleClass();
        schedule.addTrain(train);
        int counter = 0;

        while(counter < stationsValidated) {
            schedule.addSchedule(scheduleStations.get(counter), scheduleTimes.get(counter));
            scheduleStations.get(counter).addTrain(train, scheduleTimes.get(counter) );
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
        if(!isDepartingStation(station))
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

        if(!existsStation(departure))
            throw new NonexistentStationException();

        if(!existsStation(destination))
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
    public DoubleList<Station> getStations() {
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
    public boolean isDepartingStation(Station station) {
        return this.stations.getFirst().equals(station) || this.stations.getLast().equals(station);
    }

    @Override
    public boolean existsStation(Station station) {
        return stations.find(station) != -1;
    }

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

    private boolean hasOvertake(Station station, Time time, Station departureStation,Time departureTime) {
        Iterator<Entry<String, Schedule>> it = schedules.iterator();
        int stationPos = stations.find(station);
        boolean isForward = stations.getFirst().equals(departureStation); // Check direction

        while(it.hasNext()) {
            Schedule existingSchedule = it.next().getValue();

            // Only compare schedules going in the same direction
            if(existingSchedule.getDepartureStation().equals(stations.getFirst()) == isForward) {
                if(existingSchedule.existsStation(station)) {
                    // Compare times only if trains pass through the same station
                    if(existingSchedule.getDepartureTime().compareTo(departureTime) <= 0) {
                        // Existing train departs earlier
                        if(existingSchedule.getStationTime(station).compareTo(time) >= 0) {
                            return true; // Overtake detected
                        }
                    }
                    else {
                        // Existing train departs later
                        if(existingSchedule.getStationTime(station).compareTo(time) < 0) {
                            return true; // Overtake detected
                        }
                    }
                }
                else {
                    // Start checking from the station right before our target
                    for(int pos = stationPos - 1; pos >= 0; pos--) {
                        Station previousStation = stations.get(pos);
                        if(existingSchedule.existsStation(previousStation)) {
                            // Found the most recent previous station in the schedule
                            if(existingSchedule.getDepartureTime().compareTo(departureTime) < 0) {
                                // Existing train passes through previous station earlier
                                if(existingSchedule.getStationTime(previousStation).compareTo(time) >= 0) {
                                    return true; // Overtake detected
                                }
                            } else {
                                // Existing train passes through previous station later
                                if(existingSchedule.getStationTime(previousStation).compareTo(time) <= 0) {
                                    return true; // Overtake detected
                                }
                            }
                            break; // Only need to check the most recent previous station
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
