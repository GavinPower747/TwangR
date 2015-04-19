package net.gavinpower.Models;

import static net.gavinpower.twangr.TwangR.currentUser;

@SuppressWarnings("unused")
public class Message {

    public String messageID;
    public String sender;
    public String message;
    public boolean isSelf;
    public String TimeStamp;
    public String ChatId;

    public Message() {}

    public Message(String messageID, String sender, String message, boolean isSelf, String TimeStamp, String ChatId)
    {
        this.messageID = messageID;
        this.sender = sender;
        this.message = message;
        this.isSelf = isSelf;
        this.TimeStamp = TimeStamp;
        this.ChatId = ChatId;
    }

    public String toDBFields()
    {
        return "MessageID, Sender, Message, TimeStamp, ChatId";
    }

    public String toDBString()
    {
        return " '" + this.messageID + "','"
                + this.sender + "','"
                + this.message + "','"
                + this.TimeStamp + "','"
                + this.ChatId + "'";
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

    public boolean isSelf(String user) {
        return (user.equals(currentUser.getUserRealName()));
    }

    public void setSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getChatId() {
        return ChatId;
    }

    public void setChatId(String chatId) {
        ChatId = chatId;
    }
}
