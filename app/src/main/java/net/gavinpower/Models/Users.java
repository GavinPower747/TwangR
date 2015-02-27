package net.gavinpower.Models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Users implements Collection<User> {
    public List<User> _Users;

    public Users()
    {
        _Users = new ArrayList<User>();
    }

    public User get(int position)
    {
        return _Users.get(position);
    }

    @Override
    public boolean add(User object) {
        return _Users.add(object);
    }

    @Override
    public boolean addAll(Collection<? extends User> collection) {
        return _Users.addAll(collection);
    }

    @Override
    public void clear() {
        _Users.clear();
    }

    @Override
    public boolean contains(Object object) {
        return _Users.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return _Users.containsAll(collection);
    }

    @Override
    public boolean equals(Object object) {
        return _Users.equals(object);
    }

    @Override
    public int hashCode() {
        return _Users.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return _Users.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<User> iterator() {
        return _Users.iterator();
    }

    @Override
    public boolean remove(Object object) {
        return _Users.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return _Users.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return _Users.retainAll(collection);
    }

    @Override
    public int size() {
        return _Users.size();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return _Users.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] array) {
        return _Users.toArray(array);
    }
}
