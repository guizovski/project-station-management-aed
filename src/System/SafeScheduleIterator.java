package System;

import dataStructures.Entry;
import dataStructures.EntryClass;
import dataStructures.Iterator;
import dataStructures.NoSuchElementException;

public class SafeScheduleIterator<K> implements Iterator<Entry<K, SafeSchedule>> {

    /** Original iterator being wrapped */
    private final Iterator<Entry<K, Schedule>> originalIterator;

    public SafeScheduleIterator(Iterator<Entry<K, Schedule>> originalIterator) {
        this.originalIterator = originalIterator;
    }

    @Override
    public boolean hasNext() {
        return originalIterator.hasNext();
    }

    @Override
    public Entry<K, SafeSchedule> next() throws NoSuchElementException {
        Entry<K, Schedule> original = originalIterator.next();
        return new EntryClass<>(original.getKey(), (SafeSchedule) original.getValue());
    }

    @Override
    public void rewind() {
        originalIterator.rewind();
    }
}
