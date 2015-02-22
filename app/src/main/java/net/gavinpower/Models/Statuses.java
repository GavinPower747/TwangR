package net.gavinpower.Models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Statuses implements Collection<Status> {
    public List<Status> _Statuses;

    public Statuses()
    {
        _Statuses = new ArrayList<Status>();
    }

    public Status get(int position)
    {
        return _Statuses.get(position);
    }

    @Override
    public boolean add(Status object) {
        return _Statuses.add(object);
    }

    @Override
    public boolean addAll(Collection<? extends Status> collection) {
        return _Statuses.addAll(collection);
    }

    @Override
    public void clear() {
        _Statuses.clear();
    }

    @Override
    public boolean contains(Object object) {
        return _Statuses.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return _Statuses.containsAll(collection);
    }

    @Override
    public boolean equals(Object object) {
        return _Statuses.equals(object);
    }

    @Override
    public int hashCode() {
        return _Statuses.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return _Statuses.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<Status> iterator() {
        return _Statuses.iterator();
    }

    @Override
    public boolean remove(Object object) {
        return _Statuses.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return _Statuses.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return _Statuses.retainAll(collection);
    }

    @Override
    public int size() {
        return _Statuses.size();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return _Statuses.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] array) {
        return _Statuses.toArray(array);
    }
}
