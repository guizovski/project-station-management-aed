package dataStructures;

public class OpenHashTableIterator<K extends Comparable<K>, V>
    implements Iterator<Entry<K, V>> {

    protected Dictionary<K, V>[] table;
    protected int current;
    protected Iterator<Entry<K, V>> listIterator;

    public OpenHashTableIterator(Dictionary<K, V>[] table) {
        this.table = table;
        this.current = 0;
        this.listIterator = getNextIterator();
    }

    @Override
    public boolean hasNext() {
        if (listIterator == null)
            return false;
        if (listIterator.hasNext())
            return true;
        listIterator = getNextIterator();
        return listIterator != null;
    }

    @Override
    public Entry<K, V> next() throws NoSuchElementException {
        if (!hasNext())
            throw new NoSuchElementException();
        return listIterator.next();
    }

    @Override
    public void rewind() {
        current = 0;
        listIterator = getNextIterator();
    }

    private Iterator<Entry<K, V>> getNextIterator() {
        while (current < table.length) {
            if (!table[current].isEmpty())
                return table[current++].iterator();
            current++;
        }
        return null;
    }
}
