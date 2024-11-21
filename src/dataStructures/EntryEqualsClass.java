package dataStructures;

public class EntryEqualsClass<K, V> extends EntryClass<K, V> implements EntryEquals<K, V> {

    public EntryEqualsClass(K key, V value) {
        super(key, value);
    }

    // Completar!!!!
    @Override
    public boolean equals(Entry<K, V> e) {
        return this.key.equals(e.getKey());
    }
    
}
