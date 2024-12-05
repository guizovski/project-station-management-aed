package System;

import java.io.Serializable;

/**
 * Safe interface for Station that exposes only necessary methods to Main
 */
public interface SafeStation extends Comparable<Station>, Serializable {

    /**
     * Gets the name of the station
     * @return name of the station
     */
    String getName();

}
