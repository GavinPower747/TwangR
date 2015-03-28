package net.gavinpower.SignalR;

import java.util.Date;

@SuppressWarnings("unused")
public class Message {

    private String messageID;
    private String sender;
    private String message;
    private boolean isSelf;
    private Date TimeStamp;
<<<<<<< HEAD
    private String chatId;

    public Message() {}

    public Message(String messageID, String sender, String message, boolean isSelf, Date TimeStamp, String chatId)
=======
    private String ChatId;

    public Message() {}

    public Message(String messageID, String sender, String message, boolean isSelf, Date TimeStamp, String ChatId)
>>>>>>> origin/Chat
    {
        this.messageID = messageID;
        this.sender = sender;
        this.message = message;
        this.isSelf = isSelf;
        this.TimeStamp = TimeStamp;
<<<<<<< HEAD
        this.chatId = chatId;
=======
        this.ChatId = ChatId;
>>>>>>> origin/Chat
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

<<<<<<< HEAD
    public String getChatId() { return this.chatId; }

    public void setChatId(String chatId) { this.chatId = chatId; }

=======
    public String getChatId() {
        return ChatId;
    }

    public void setChatId(String chatId) {
        ChatId = chatId;
    }
>>>>>>> origin/Chat
}
