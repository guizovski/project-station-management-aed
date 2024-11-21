package System;

import java.io.Serializable;

public interface Time extends Serializable, Comparable<Time> {

    String hour();

    String minute();

    boolean hasTravelTime(Time other);

    String getTime();
    
}
