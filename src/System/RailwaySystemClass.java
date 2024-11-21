package System;

import dataStructures.*;
import exceptions.*;

import java.io.Serial;

public class RailwaySystemClass implements RailwaySystem {

    @Serial
    private static final long serialVersionUID = 0L;

    protected SepChainHashTable<String, Line> lines;
    protected SepChainHashTable<String, Station> stations;

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
        DoubleList<Station> lineStations = line.getStations();
        Iterator<Station> it = lineStations.iterator();

        while(it.hasNext()) {
            Station station = it.next();
            station.removeLine(line);

            if(station.isAbandoned())
                stations.remove(station.getName().toUpperCase());
        }
    }

    @Override
    public Iterator<Station> consultLine(String name) throws NonexistentLineException {
        if(!existsLine(name))
            throw new NonexistentLineException();
        return lines.find(name.toUpperCase()).getStationsIt();
    }

    @Override
    public Iterator<Entry<String, Line>> consultStation(String name) throws NonexistentStationException {
        if(!existsStation(name))
            throw new NonexistentStationException();
        return stations.find(name.toUpperCase()).consultLines();
    }

    @Override
    public void insertSchedule(String lineName, String train, DoubleList<String[]> stationTime)
            throws NonexistentLineException, InvalidScheduleException {

        if(!existsLine(lineName))
            throw new NonexistentLineException();

        Line line = lines.find(lineName.toUpperCase());

        DoubleList<Station> scheduleStations;
        try {
            scheduleStations = extractStations(stationTime);
        } catch (NonexistentStationException e) {
            throw new InvalidScheduleException();
        }
        DoubleList<Time> scheduleTimes = extractTimes(stationTime);
        line.insertSchedule(train, scheduleStations, scheduleTimes);
    }

    @Override
    public void removeSchedule(String lineName, String[] stationAndTime)
            throws NonexistentLineException, NonexistentScheduleException {
        if(!existsLine(lineName))
            throw new NonexistentLineException();

        Line line = lines.find(lineName.toUpperCase());

        String stationName = arrangeStationName(stationAndTime);

        if(!existsStation(stationName))
            throw new NonexistentScheduleException();

        Station station = stations.find(stationName.toUpperCase());
        Time time = parseTime(stationAndTime[stationAndTime.length - 1]);

        line.removeSchedule(station, time);
    }

    @Override
    public Iterator<Entry<Time, Schedule>> consultSchedules(String lineName, String stationName)
            throws NonexistentLineException, NonexistentStationException {

        if(!existsLine(lineName))
            throw new NonexistentLineException();
        if(!existsStation(stationName))
            throw new NonexistentStationException();

        Line line = lines.find(lineName.toUpperCase());
        Station station = stations.find(stationName.toUpperCase());

        return line.consultSchedules(station);
    }

    @Override
    public Iterator<Entry<String, Time>> stationTrains(String name) throws NonexistentStationException {
        if(!existsStation(name))
            throw new NonexistentStationException();

        return this.stations.find(name.toUpperCase()).consultTrains();
    }

    @Override
    public Schedule bestSchedule(String lineName, String departure, String destination, String timeOfArrival)
            throws NonexistentLineException, NonexistentStationException, ImpossibleRouteException {

        if(!existsLine(lineName))
            throw new NonexistentLineException();

        Line line = lines.find(lineName.toUpperCase());
        Station depart = stations.find(departure.toUpperCase());
        Station destin = stations.find(destination.toUpperCase());
        Time time = parseTime(timeOfArrival);

        return line.getBestSchedule(depart, destin, time);
    }

    private DoubleList<Station> extractStations(DoubleList<String[]> stations)
            throws NonexistentStationException {

        DoubleList<Station> stationList = new DoubleList<>();

        for(int i = 0; i < stations.size(); i++) {
            String name = arrangeStationName(stations.get(i));
            if(!existsStation(name))
                throw new NonexistentStationException();
            stationList.addLast(this.stations.find(name.toUpperCase()));
        }
        return stationList;
    }

    private DoubleList<Time> extractTimes(DoubleList<String[]> stations) {

        DoubleList<Time> timesList = new DoubleList<>();

        for(int i = 0; i < stations.size(); i++) {
            String[] tmp = stations.get(i);
            timesList.addLast(parseTime(tmp[tmp.length - 1]));
        }
        return timesList;
    }

    private String arrangeStationName(String[] station) {
        StringBuilder stationName = new StringBuilder(station[0]);
        for(int i = 1; i < station.length - 1; i++)
                stationName.append(" ").append(station[i]);
        return stationName.toString();
    }

    private Time parseTime(String timeLine) {
        String[] tmp = timeLine.split(":");
        return new TimeClass(tmp[0], tmp[1]);
    }

    private boolean existsLine(String lineName) {
        return lines.find(lineName.toUpperCase()) != null;
    }

    private boolean existsStation(String stationName) {
        return this.stations.find(stationName.toUpperCase()) != null;
    }

    private DoubleList<Station> arrangeStations(DoubleList<String> stationsNames) {
        DoubleList<Station> stationsList = new DoubleList<>();
        Iterator<String> it = stationsNames.iterator();

        while(it.hasNext()) {
            String sName = it.next();
            Station station = this.stations.find(sName.toUpperCase());

            // IF station is null, therefore doesn't exist
            if(station == null) {
                station = new StationClass(sName);
                this.stations.insert(sName.toUpperCase(), station);
            }
            stationsList.addLast(station);
        }
        return stationsList;
    }
}
