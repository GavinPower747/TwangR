package net.gavinpower.Models;


import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static net.gavinpower.twangr.TwangR.repo;

public class Messages implements Collection<Message>
{
    public List<Message> _Messages;

    public Messages()
    {
        _Messages = new ArrayList<Message>();
    }

    public Message get(int position)
    {
        return _Messages.get(position);
    }

    public void getMessagesByChatId(String ChatId)
    {
        _Messages = repo.getMessagesByChatId(ChatId);
    }

    public Message getLatestMessageByChatId(String chatId)
    {
        for(Message message : _Messages)
        {
            if(message.ChatId.equals(chatId))
            {
                Log.v("MessageTimeStamp", message.TimeStamp);
                return message;
            }
        }

        return new Message();
    }

    @Override
    public boolean add(Message object) {
        return _Messages.add(object);
    }

    @Override
    public boolean addAll(Collection<? extends Message> collection) {
        return _Messages.addAll(collection);
    }

    @Override
    public void clear() {
        _Messages.clear();
    }

    @Override
    public boolean contains(Object object) {
        return _Messages.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return _Messages.containsAll(collection);
    }

    @Override
    public boolean equals(Object object) {
        return _Messages.equals(object);
    }

    @Override
    public int hashCode() {
        return _Messages.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return _Messages.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<Message> iterator() {
        return _Messages.iterator();
    }

    @Override
    public boolean remove(Object object) {
        return _Messages.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return _Messages.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return _Messages.retainAll(collection);
    }

    @Override
    public int size() {
        return _Messages.size();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return _Messages.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] array) {
        return _Messages.toArray(array);
    }
}
