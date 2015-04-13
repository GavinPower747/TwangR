package net.gavinpower.Exceptions;

public class LocalCacheException extends Exception {
    public LocalCacheException()
    {
        super("There was an issue with the local copy of the data");
    }

    public LocalCacheException(String message)
    {
        super(message);
    }
}
