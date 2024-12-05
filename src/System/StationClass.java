package System;

import dataStructures.*;
import java.io.Serial;

public class StationClass implements Station, SafeStation {

    @Serial
    static final long serialVersionUID = 0L;

    /** Collection of lines passing through this station, indexed by line name */
    protected OrderedDictionary<String, Line> lines;
    /** Collection of trains passing through this station, ordered by time */
    protected OrderedDictionary<TimeTrainPairClass, Line> trains;
    /** The station's unique name */
    protected String name;

    /**
     * Creates a new station with given name
     * @param name unique name for the station
     */
    public StationClass(String name) {
        this.lines = new AVLTree<String, Line>();
        this.trains = new BinarySearchTree<TimeTrainPairClass, Line>();
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
        Iterator<Entry<TimeTrainPairClass, Line>> it = trains.iterator();
        while(it.hasNext()) {
            Entry<TimeTrainPairClass, Line> entry = it.next();
            if(entry.getValue().equals(line)) trains.remove(entry.getKey());
        }
    }

    @Override
    public void addTrain(String train, Time time, Line line) {
        TimeTrainPairClass key = new TimeTrainPairClass(time, train);
        trains.insert(key, line);
    }

    @Override
    public void removeTrain(int train) {
        Iterator<Entry<TimeTrainPairClass, Line>> it = trains.iterator();
        while(it.hasNext()) {
            Entry<TimeTrainPairClass, Line> entry = it.next();
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
    public Iterator<Entry<TimeTrainPairClass, Line>> consultTrains() {
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
