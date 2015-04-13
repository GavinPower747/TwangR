package net.gavinpower.Models;

import java.util.List;

public class Chat {

    public Chat() {}
    public Chat(String chatId, List<Integer> Participants)
    {
        this.ChatId = chatId;
        this.Participants = Participants;
    }

    public String ChatId;
    public List<Integer> Participants;
}
