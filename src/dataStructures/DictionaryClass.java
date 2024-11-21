package dataStructures;

public class DictionaryClass<K, V> implements Dictionary<K, V>{

    static final long serialVersionUID = 0L;

    protected DoubleList<Entry<K, V>> data;
    protected int size;

    public DictionaryClass() {
        this.data = new DoubleList<Entry<K, V>>();
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return data.iterator();
    }

    @Override
    public V find(K key) {
        int pos = data.find(new EntryClass<K, V>(key, null));
        return data.get(pos).getValue();
    }

    @Override
    public V insert(K key, V value) {
        EntryClass<K,V> tmp = new EntryClass<K, V>(key, value);
        data.addLast(tmp);
        return tmp.getValue();
    }

    @Override
    public V remove(K key) {
        V tmp = find(key);
        data.remove(new EntryClass<K, V>(key, null));
        return tmp;
    }
    
}
