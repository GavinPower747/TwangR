package net.gavinpower.SignalR;

import java.util.Date;

@SuppressWarnings("unused")
public class Message {


    private String messageID;
    private String sender;
    private String message;
    private boolean isSelf;
    private Date TimeStamp;
    private String chatId;

    public Message() {}

    public Message(String messageID, String sender, String message, boolean isSelf, Date TimeStamp, String chatId)
    {
        this.messageID = messageID;
        this.sender = sender;
        this.message = message;
        this.isSelf = isSelf;
        this.TimeStamp = TimeStamp;
        this.chatId = chatId;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
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

    public Date getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getChatId() { return this.chatId; }

    public void setChatId(String chatId) { this.chatId = chatId; }

}
