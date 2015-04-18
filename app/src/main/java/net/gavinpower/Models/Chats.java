package net.gavinpower.Models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Chats implements Collection<Chat> {
    public List<Chat> _Chats;

    public Chats()
    {
        _Chats = new ArrayList<Chat>();
    }

    public Chat get(int position)
    {
        return _Chats.get(position);
    }

    @Override
    public boolean add(Chat object) {
        return _Chats.add(object);
    }

    @Override
    public boolean addAll(Collection<? extends Chat> collection) {
        return _Chats.addAll(collection);
    }

    @Override
    public void clear() {
        _Chats.clear();
    }

    @Override
    public boolean contains(Object object) {
        return _Chats.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return _Chats.containsAll(collection);
    }

    @Override
    public boolean equals(Object object) {
        return _Chats.equals(object);
    }

    @Override
    public int hashCode() {
        return _Chats.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return _Chats.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<Chat> iterator() {
        return _Chats.iterator();
    }

    @Override
    public boolean remove(Object object) {
        return _Chats.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return _Chats.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return _Chats.retainAll(collection);
    }

    @Override
    public int size() {
        return _Chats.size();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return _Chats.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] array) {
        return _Chats.toArray(array);
    }
}
