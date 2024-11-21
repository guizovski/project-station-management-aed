package dataStructures;

public class EntryClass<K, V> implements Entry<K, V>{

    static final long serialVersionUID = 0L;

    protected K key;
    protected V value;

    public EntryClass(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }
}
