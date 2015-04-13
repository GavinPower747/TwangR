package net.gavinpower.twangr;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import net.gavinpower.Database.DBManager;
import net.gavinpower.Models.Chat;
import net.gavinpower.Models.Chats;
import net.gavinpower.Models.Statuses;
import net.gavinpower.Models.User;
import net.gavinpower.Models.Users;
import net.gavinpower.SignalR.Connection;
import net.gavinpower.twangr.Activities.FriendsListActivity;

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

    @Override
    public void onCreate()
    {
        super.onCreate();

        onlineFriends = new Users();
        activeChats = new Chats();
        myPosts = new Statuses();
        newsFeed = new Statuses();
    }



    public void notifyMessage(String sender, String message, String chatId)
    {
        Intent notificationIntent = new Intent(this, FriendsListActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder noteBuild = new NotificationCompat.Builder(this);
        noteBuild.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Message From " + sender)
                .setContentText(message)
                .setContentIntent(contentIntent);

        NotificationManager noteManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noteManager.notify(0, noteBuild.build());

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(this, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void notifyFriendRequest(User requestee)
    {

    }

    public void notifyFriendAccept(User requester)
    {

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
