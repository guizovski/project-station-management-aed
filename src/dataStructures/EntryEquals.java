package dataStructures;

public interface EntryEquals<K, V> extends Entry<K, V>{
    
    boolean equals(Entry<K, V> e);
    
}
