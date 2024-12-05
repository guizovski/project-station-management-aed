package System;

import dataStructures.*;
import System.exceptions.*;

import java.io.Serial;

public class RailwaySystemClass implements RailwaySystem {

    @Serial
    static final long serialVersionUID = 0L;

    /** Collection of all lines in the system, indexed by name */
    protected Dictionary<String, Line> lines;
    /** Collection of all stations in the system, indexed by name */
    protected Dictionary<String, Station> stations;

    /**
     * Creates new empty railway system
     */
    public RailwaySystemClass() {
        lines = new SepChainHashTable<String, Line>();
        stations = new SepChainHashTable<String, Station>();
    }

    @Override
    public void insertLine(String name, DoubleList<String> stationNames)
            throws ExistentLineException {
        if(existsLine(name))
            throw new ExistentLineException();

        DoubleList<Station> stationsList = arrangeStations(stationNames);

        Line line = new LineClass(name, stationsList);
        lines.insert(name.toUpperCase(), line);
        Iterator<Station> it = stationsList.iterator();

        while(it.hasNext())
            it.next().addLine(line);
    }

    @Override
    public void removeLine(String name) throws NonexistentLineException {

        if(!existsLine(name))
            throw new NonexistentLineException();

        Line line = lines.remove(name.toUpperCase());
        List<Station> lineStations = line.getStations();
        Iterator<Station> it = lineStations.iterator();

        while(it.hasNext()) {
            Station station = it.next();
            station.removeLine(line);

            if(station.isAbandoned())
                stations.remove(station.getName().toUpperCase());
        }
    }

    @Override
    public SafeStationIterator consultLine(String name) throws NonexistentLineException {
        if(!existsLine(name))
            throw new NonexistentLineException();
        return new SafeStationIterator(lines.find(name.toUpperCase()).getStationsIt());
    }

    @Override
    public SafeLineIterator<String> consultStation(String name) throws NonexistentStationException {
        if(!existsStation(name))
            throw new NonexistentStationException();

        Station station = stations.find(name.toUpperCase());
        Iterator<Entry<String, Line>> originalIterator = station.consultLines();

        return new SafeLineIterator<String>(originalIterator);
    }

    @Override
    public void insertSchedule(String lineName, String train, DoubleList<String> stations, DoubleList<String[]> times)
            throws NonexistentLineException, InvalidScheduleException {

        if(!existsLine(lineName))
            throw new NonexistentLineException();

        Line line = lines.find(lineName.toUpperCase());

        DoubleList<Station> scheduleStations = extractStations(stations);
        DoubleList<Time> scheduleTimes = extractTimes(times);
        line.insertSchedule(train, scheduleStations, scheduleTimes);

    }

    @Override
    public void removeSchedule(String lineName, String stationName, String[] departure)
            throws NonexistentLineException, NonexistentScheduleException {
        if(!existsLine(lineName))
            throw new NonexistentLineException();

        Line line = lines.find(lineName.toUpperCase());

        if(!existsStation(stationName))
            throw new NonexistentScheduleException();

        Station station = stations.find(stationName.toUpperCase());
        Time time = new TimeClass(departure[0], departure[1]);

        line.removeSchedule(station, time);
    }

    @Override
    public SafeScheduleIterator<Time> consultSchedules(String lineName, String stationName)
            throws NonexistentLineException, NonexistentStationException {

        if(!existsLine(lineName))
            throw new NonexistentLineException();
        if(!existsStation(stationName))
            throw new NonexistentStationException();

        Line line = lines.find(lineName.toUpperCase());
        Station station = stations.find(stationName.toUpperCase());

        return new SafeScheduleIterator<Time>(line.consultSchedules(station));
    }

    @Override
    public SafeLineIterator<TimeTrainPairClass> stationTrains(String name) throws NonexistentStationException {
        if(!existsStation(name))
            throw new NonexistentStationException();
        Station station = stations.find(name.toUpperCase());
        Iterator<Entry<TimeTrainPairClass, Line>> originalIterator = station.consultTrains();

        return new SafeLineIterator<TimeTrainPairClass>(originalIterator);
    }

    @Override
    public SafeSchedule bestSchedule(String lineName, String departure, String destination, String[] timeOfArrival)
            throws NonexistentLineException, NonexistentStationException, ImpossibleRouteException {

        if(!existsLine(lineName))
            throw new NonexistentLineException();

        Line line = lines.find(lineName.toUpperCase());
        Station depart = stations.find(departure.toUpperCase());
        Station destin = stations.find(destination.toUpperCase());
        Time time = new TimeClass(timeOfArrival[0], timeOfArrival[1]);

        return (SafeSchedule) line.getBestSchedule(depart, destin, time);
    }

    /**
     * Extracts station objects from array of strings containing station information
     * @param stationNames DoubleList of string arrays containing station names
     * @return DoubleList of Station objects
     * @throws InvalidScheduleException if a station doesn't exist
     */
    private DoubleList<Station> extractStations(DoubleList<String> stationNames)
            throws InvalidScheduleException {

        DoubleList<Station> stationsList = new DoubleList<>();
        Iterator<String> it = stationNames.iterator();

        while(it.hasNext()) {
            String name = it.next();
            if(!existsStation(name))
                throw new InvalidScheduleException();
            Station station = this.stations.find(name.toUpperCase());
            stationsList.addLast(station);
        }
        return stationsList;
    }

    /**
     * Extracts time objects from array of strings containing schedule information
     * @param times DoubleList of string arrays containing schedule times
     * @return DoubleList of Time objects
     */
    private DoubleList<Time> extractTimes(DoubleList<String[]> times) {

        DoubleList<Time> timesList = new DoubleList<>();
        Iterator<String[]> it = times.iterator();

        while(it.hasNext()) {
            String[] tmp = it.next();
            timesList.addLast(new TimeClass(tmp[0], tmp[1]));
        }
        return timesList;
    }

    /**
     * Checks if a line with the given name exists
     * @param lineName name of the line to check
     * @return true if line exists, false otherwise
     */
    private boolean existsLine(String lineName) {
        return lines.find(lineName.toUpperCase()) != null;
    }

    /**
     * Checks if a station with the given name exists
     * @param stationName name of the station to check
     * @return true if station exists, false otherwise
     */
    private boolean existsStation(String stationName) {
        return this.stations.find(stationName.toUpperCase()) != null;
    }

    /**
     * Creates or retrieves station objects for a list of station names
     * @param stationsNames list of station names to process
     * @return DoubleList of Station objects corresponding to the names
     */
    private DoubleList<Station> arrangeStations(DoubleList<String> stationsNames) {
        DoubleList<Station> stationsList = new DoubleList<>();
        Iterator<String> it = stationsNames.iterator();

        while(it.hasNext()) {
            String sName = it.next();
            Station station = this.stations.find(sName.toUpperCase());

            if(station == null) {
                station = new StationClass(sName);
                this.stations.insert(sName.toUpperCase(), station);
            }
            stationsList.addLast(station);
        }
        return stationsList;
    }
}
