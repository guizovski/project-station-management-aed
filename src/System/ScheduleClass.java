package System;

import dataStructures.*;

import java.io.Serial;

public class ScheduleClass implements Schedule {

    @Serial
    private static final long serialVersionUID = 0L;

    protected OrderedDoubleList<Station, Time> schedule;
    protected DoubleList<Station> orderedStations;
    protected int train;

    public ScheduleClass() {
        this.schedule = new OrderedDoubleList<Station, Time>();
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
    public int getTrain() {
        return train;
    }

    @Override
    public Iterator<Station> getStationIt() {
        return orderedStations.iterator();
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
