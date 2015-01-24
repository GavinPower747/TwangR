package net.gavinpower.SignalR;

@SuppressWarnings("unused")
public class Message {
    private String sender;
    private String message;
    private boolean isSelf;

    public Message(String sender, String message, boolean isSelf)
    {
        this.sender = sender;
        this.message = message;
        this.isSelf = isSelf;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }
}
