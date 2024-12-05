package System;

import dataStructures.Entry;
import dataStructures.EntryClass;
import dataStructures.Iterator;
import dataStructures.NoSuchElementException;

public class SafeLineIterator<K> implements Iterator<Entry<K, SafeLine>> {

    /** Original iterator being wrapped */
    private final Iterator<Entry<K, Line>> originalIterator;

    public SafeLineIterator(Iterator<Entry<K, Line>> originalIterator) {
        this.originalIterator = originalIterator;
    }

    @Override
    public boolean hasNext() {
        return originalIterator.hasNext();
    }

    @Override
    public Entry<K, SafeLine> next() throws NoSuchElementException {
        Entry<K, Line> original = (Entry<K, Line>) originalIterator.next();
        return new EntryClass<>(original.getKey(), (SafeLine) original.getValue());
    }

    @Override
    public void rewind() {
        originalIterator.rewind();
    }
}
