package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A collection of fixed maximum size to which elements may always be added.
 * If the maximum size is reached, adding an element overwrites the oldest in the collection.
 */
public class CircularBuffer<E> implements Collection<E> {
    private final int maxSize;
    private final ArrayList<E> buffer;
    private int idx;

    public CircularBuffer(int maxSize) {
        this.maxSize = maxSize;
        buffer = new ArrayList<>(maxSize);
        idx = 0;
    }

    @Override
    public boolean add(E e) {
        if (buffer.size() < maxSize) {
            buffer.add(e);
        } else {
            buffer.set(idx, e);
            idx = (idx + 1) % maxSize;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public void clear() {
        buffer.clear();
        idx = 0;
    }

    @Override
    public boolean contains(Object o) {
        return buffer.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return buffer.containsAll(c);
    }

    @Override
    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        // Mask Iterator.remove
        Iterator<E> iter = buffer.iterator();
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public E next() {
                return iter.next();
            }
        };
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return buffer.size();
    }

    @Override
    public Object[] toArray() {
        return buffer.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return buffer.toArray(a);
    }
}
