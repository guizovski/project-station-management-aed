package System;

import java.io.Serializable;

/**
 * Safe interface for Line that exposes only necessary methods to Main
 */
public interface SafeLine extends Comparable<Line>, Serializable {

    /**
     * Gets the name of the line
     * @return name of the line
     */
    String getName();

}
