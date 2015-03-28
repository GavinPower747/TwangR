package net.gavinpower.Models;

import java.util.List;

public class Chat {

    public Chat() {}
    public Chat(String chatId, List<Integer> Participants)
    {
        this.chatId = chatId;
        this.Participants = Participants;
    }

    public String chatId;
    public List<Integer> Participants;
}
