package System;

import dataStructures.*;

import java.io.Serial;

public class ScheduleClass implements Schedule, SafeSchedule {

    @Serial
    static final long serialVersionUID = 0L;

    /** Maps stations to their times in this schedule */
    protected OrderedDictionary<Station, Time> schedule;
    /** Maintains the order of stations in this schedule */
    protected List<Station> orderedStations;
    /** Unique identifier for the train running this schedule */
    protected int train;

    /**
     * Creates a new empty schedule
     */
    public ScheduleClass() {
        this.schedule = new AVLTree<Station, Time>();
        this.orderedStations = new DoubleList<Station>();
    }

    @Override
    public void addTrain(String train) {
        this.train = Integer.parseInt(train);
    }

    @Override
    public void addSchedule(Station station, Time time) {
        orderedStations.addLast(station);
        schedule.insert(orderedStations.getLast(), time);
    }

    @Override
    public Station getDepartureStation() {
        return orderedStations.getFirst();
    }

    @Override
    public Time getDepartureTime() {
        return schedule.find(getDepartureStation());
    }

    @Override
    public Time getStationTime(SafeStation station) {
        return schedule.find((Station) station);
    }

    @Override
    public int getTrain() {
        return train;
    }

    @Override
    public SafeStationIterator getStationIt() {
        return new SafeStationIterator(orderedStations.iterator());
    }

    @Override
    public Time getStationTime(Station station) {
        return schedule.find(station);
    }

    @Override
    public boolean existsStation(Station station) {
        return orderedStations.find(station) != -1;
    }

    @Override
    public void deleteSchedule() {
        Iterator<Station> it = orderedStations.iterator();
        while(it.hasNext()) it.next().removeTrain(train);
    }

    @Override
    public boolean isRightOrder(Station departure, Station arrival) {
        return orderedStations.find(departure) < orderedStations.find(arrival);
    }
    
    @Override
    public int compareTo(Schedule o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }
    
}
