package System;

import java.io.Serializable;
import dataStructures.*;

public interface Station extends Comparable<Station>, Serializable {

    String getName();

    void addLine(Line line);

    void removeLine(Line line);

    void addTrain(String train, Time time);

    void removeTrain(int train);

    Iterator<Entry<TimeTrainPair, Time>> consultTrains();

    Iterator<Entry<String, Line>> consultLines();

    boolean isAbandoned();

}
