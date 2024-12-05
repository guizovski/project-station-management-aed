package System;

import java.io.Serializable;

public interface TimeTrainPair extends Comparable<TimeTrainPairClass>, Serializable {

    /**
     * Gets time component
     * @return time of passage
     */
    Time getTime();

    /**
     * Gets train identifier
     * @return train identifier
     */
    int getTrain();
}
