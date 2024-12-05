package dataStructures;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serial;

public class OrderedDoubleList<K extends Comparable<K>, V> implements Dictionary<K, V> {

    static final long serialVersionUID = 0L;

    protected DoubleListNode<Entry<K, V>> head;
    protected DoubleListNode<Entry<K, V>> tail;
    protected int currentSize;

    public OrderedDoubleList() {
        head = null;
        tail = null;
        currentSize = 0;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new DoubleListIterator<>(head, tail);
    }

    @Override
    public V find(K key) {
        if (key == null) return null;

        DoubleListNode<Entry<K, V>> node = findNode(key);
        if (node != null && node.getElement().getKey().compareTo(key) == 0) {
            return node.getElement().getValue();
        }
        return null;
    }

    @Override
    public V insert(K key, V value) {
        if (key == null) return null;

        Entry<K, V> newEntry = new EntryClass<>(key, value);

        if (isEmpty()) {
            head = tail = new DoubleListNode<>(newEntry);
            currentSize++;
            return null;
        }

        DoubleListNode<Entry<K, V>> node = findNode(key);

        if (node == null) {
            DoubleListNode<Entry<K, V>> newNode = new DoubleListNode<>(newEntry);
            tail.setNext(newNode);
            newNode.setPrevious(tail);
            tail = newNode;
            currentSize++;
            return null;
        }

        if (node.getElement().getKey().compareTo(key) == 0) {
            V oldValue = node.getElement().getValue();
            node.setElement(newEntry);
            return oldValue;
        }

        DoubleListNode<Entry<K, V>> newNode = new DoubleListNode<>(newEntry);
        if (node == head) {
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
        } else {
            newNode.setPrevious(node.getPrevious());
            newNode.setNext(node);
            node.getPrevious().setNext(newNode);
            node.setPrevious(newNode);
        }
        currentSize++;
        return null;
    }

    @Override
    public V remove(K key) {
        if (key == null || isEmpty()) return null;

        DoubleListNode<Entry<K, V>> node = findNode(key);

        if (node == null || node.getElement().getKey().compareTo(key) != 0) return null;

        V oldValue = node.getElement().getValue();

        if (node == head && node == tail) {
            head = tail = null;
        }
        else if (node == head) {
            head = head.getNext();
            head.setPrevious(null);
        }
        else if (node == tail) {
            tail = tail.getPrevious();
            tail.setNext(null);
        }
        else {
            node.getPrevious().setNext(node.getNext());
            node.getNext().setPrevious(node.getPrevious());
        }

        currentSize--;
        return oldValue;
    }

    private DoubleListNode<Entry<K, V>> findNode(K key) {
        DoubleListNode<Entry<K, V>> node = head;
        while (node != null && node.getElement().getKey().compareTo(key) < 0) {
            node = node.getNext();
        }
        return node;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {

        out.defaultWriteObject();

        DoubleListNode<Entry<K, V>> current = head;
        while (current != null) {
            out.writeObject(current.getElement());
            current = current.getNext();
        }

        out.writeObject(null);
    }
}
