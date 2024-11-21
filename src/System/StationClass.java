package System;

import dataStructures.*;

import java.io.Serial;

public class StationClass implements Station {

    @Serial
    private static final long serialVersionUID = 0L;

    protected BinarySearchTree<String, Line> lines;
    protected BinarySearchTree<String, Time> trains;
    protected String name;

    public StationClass(String name) {
        this.lines = new BinarySearchTree<String, Line>();
        this.trains = new BinarySearchTree<String, Time>();
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
        this.trains.insert(train, time);
    }

    @Override
    public void removeTrain(String train) {
        this.trains.remove(train);
    }

    @Override
    public Iterator<Entry<String, Line>> consultLines() {
        return lines.iterator();
    }

    // Não está certo, tem que ser por ordem de hora de partida!
    @Override
    public Iterator<Entry<String, Time>> consultTrains() {
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
