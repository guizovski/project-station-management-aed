package System;

import dataStructures.*;

import java.io.Serial;

public class StationClass implements Station {

    @Serial
    private static final long serialVersionUID = 0L;

    protected OrderedDoubleList<String, Line> lines;
    protected OrderedDoubleList<TimeTrainPair, Time> trains;
    protected String name;

    public StationClass(String name) {
        this.lines = new OrderedDoubleList<String, Line>();
        this.trains = new OrderedDoubleList<TimeTrainPair, Time>();
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addLine(Line line) {
        lines.insert(line.getName(), line);
    }

    @Override
    public void removeLine(Line line) {
        lines.remove(line.getName());
    }

    @Override
    public void addTrain(String train, Time time) {
        TimeTrainPair key = new TimeTrainPair(time, train);
        trains.insert(key, time);
    }

    @Override
    public void removeTrain(int train) {
        Iterator<Entry<TimeTrainPair, Time>> it = trains.iterator();
        while(it.hasNext()) {
            Entry<TimeTrainPair, Time> entry = it.next();
            if(entry.getKey().getTrain() == train) {
                trains.remove(entry.getKey());
                break;
            }
        }
    }

    @Override
    public Iterator<Entry<String, Line>> consultLines() {
        return lines.iterator();
    }

    @Override
    public Iterator<Entry<TimeTrainPair, Time>> consultTrains() {
        return trains.iterator();
    }

    @Override
    public boolean isAbandoned() {
        return lines.isEmpty();
    }

    @Override
    public int compareTo(Station o) {
        return this.name.toUpperCase().compareTo(o.getName().toUpperCase());
    }

}
