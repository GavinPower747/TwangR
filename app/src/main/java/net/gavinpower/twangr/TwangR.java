package net.gavinpower.twangr;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import net.gavinpower.Models.Chat;
import net.gavinpower.Models.Chats;
import net.gavinpower.Models.User;
import net.gavinpower.Models.Users;
import net.gavinpower.SignalR.Connection;
import net.gavinpower.Tasks.SignalRConnection;
import net.gavinpower.twangr.Activities.FriendsListActivity;

import java.util.concurrent.ExecutionException;

public class TwangR extends Application {

    public static Connection HubConnection;
    public static User currentUser;
    public static Users friendList;
    public static Users friendRequests;
    public static Users onlineFriends;
    public static Chats activeChats;
    public static Activity currentActivity;
    public NetworkInfo Wifi;
    public NetworkInfo MobileData;

    @Override
    public void onCreate()
    {
        super.onCreate();
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        Wifi = mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        MobileData = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        onlineFriends = new Users();
        activeChats = new Chats();
    }

    public void initConnection()
    {
        Object[] TaskParams = {"http://37.187.35.32:8081/signalr", currentActivity, Wifi, MobileData};
        SignalRConnection ConnectionTask = new SignalRConnection();

        try {
            HubConnection = (Connection) ConnectionTask.execute(TaskParams).get();
        } catch (InterruptedException | ExecutionException ex) {
            Toast.makeText(this, "Unable to connect to web service!", Toast.LENGTH_LONG).show();
        }
    }

<<<<<<< HEAD
    public static Chat chatExists(int chatee)
=======
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


    public void notifyFriendRequest(User requestee)
>>>>>>> origin/Chat
    {
        for (Chat chat : activeChats) {
            for (int userId : chat.Participants) {
                if (userId == chatee)
                    return chat;
            }
        }

        Chat NF = new Chat();
        NF.chatId = "NotFound";
        return NF;
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
