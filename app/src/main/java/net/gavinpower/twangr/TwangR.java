package net.gavinpower.twangr;

import android.app.Activity;
import android.app.Application;

import net.gavinpower.Database.DBManager;
import net.gavinpower.Models.Chat;
import net.gavinpower.Models.Chats;
import net.gavinpower.Models.Messages;
import net.gavinpower.Models.Statuses;
import net.gavinpower.Models.User;
import net.gavinpower.Models.Users;
import net.gavinpower.SignalR.Connection;

public class TwangR extends Application {

    public static Connection HubConnection;
    public static Statuses myPosts;
    public static Statuses newsFeed;
    public static User currentUser;
    public static Users friendList;
    public static Users friendRequests;
    public static Users onlineFriends;
    public static Chats activeChats;
    public static Activity currentActivity;
    public static DBManager repo;
    public static Messages messageList;

    @Override
    public void onCreate()
    {
        super.onCreate();

        onlineFriends = new Users();
        activeChats = new Chats();
        myPosts = new Statuses();
        newsFeed = new Statuses();
        messageList = new Messages();
    }

    public static Chat chatExists(int chatee)
    {
        for (Chat chat : activeChats) {
            for (int userId : chat.Participants) {
                if (userId == chatee)
                    return chat;
            }
        }

        Chat NF = new Chat();
        NF.ChatId = "NotFound";
        return NF;
    }

    public static boolean chatExists(String chatId)
    {
        for(Chat chat : activeChats) {
            if(chat.ChatId.equals(chatId))
                return true;
        }

        return false;
    }

    public void setActivity(Activity activity)
    {
        currentActivity = activity;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
