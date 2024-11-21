package dataStructures;

import java.io.Serializable;

public interface Dictionary<K,V> extends Serializable {
    // Returns true iff the dictionary contains no entries.
    boolean isEmpty( );
    // Returns the number of entries in the dictionary.
    int size( );
// Returns an iterator of the entries in the dictionary.
    Iterator<Entry<K,V>> iterator( );

    // If there is an entry in the dictionary whose key is the specified key,
// returns its value; otherwise, returns null.
    V find( K key );
    // If there is an entry in the dictionary whose key is the specified key, value;
    // replaces its value by the specified value and returns the old
    // otherwise, inserts the entry (key, value) and returns null.
    V insert( K key, V value );
    // If there is an entry in the dictionary whose key is the specified key,
    // removes it from the dictionary and returns its value;
    // otherwise, returns null.
    V remove( K key );
}// End of Dictionary
