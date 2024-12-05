package System;

import java.io.Serializable;

/**
 * Interface representing a time of day.
 * Handles time operations and comparisons.
 */
public interface Time extends Serializable, Comparable<Time> {
    /**
     * Gets hour component
     * @return hour as string
     */
    String hour();

    /**
     * Gets minute component
     * @return minute as string
     */
    String minute();

    /**
     * Checks if this time is before another time
     * @param other time to compare with
     * @return true if this time is before other
     */
    boolean hasTravelTime(Time other);
    
}
