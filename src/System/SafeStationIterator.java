package System;

import dataStructures.Iterator;
import dataStructures.NoSuchElementException;

public class SafeStationIterator implements Iterator<SafeStation> {

    /** Original iterator being wrapped */
    private final Iterator<Station> originalIterator;

    public SafeStationIterator(Iterator<Station> originalIterator) {
        this.originalIterator = originalIterator;
    }

    @Override
    public boolean hasNext() {
        return originalIterator.hasNext();
    }

    @Override
    public SafeStation next() throws NoSuchElementException {
        return (SafeStation) originalIterator.next();
    }

    @Override
    public void rewind() {
        originalIterator.rewind();
    }
}
