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

import net.gavinpower.Models.User;
import net.gavinpower.Models.Users;
import net.gavinpower.SignalR.Connection;
import net.gavinpower.Tasks.SignalRConnection;
import net.gavinpower.twangr.Activities.FriendsListActivity;
import net.gavinpower.twangr.Activities.LoginActivity;

import java.util.concurrent.ExecutionException;

public class TwangR extends Application {

    public static Connection HubConnection;
    public static User currentUser;
    public static Users friendList;
    public static Users friendRequests;
    public static Users onlineFriends;
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

    public void notifyFriendRequest(User requestee)
    {
        Intent notificationIntent = new Intent(this, FriendsListActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder noteBuild = new NotificationCompat.Builder(this);
        noteBuild.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("New Friend Request!")
                .setContentText(requestee.getUserRealName() + " wants to be your friend!")
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

    public void notifyFriendAccept(User requester)
    {
        Intent notificationIntent = new Intent(this, FriendsListActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder noteBuild = new NotificationCompat.Builder(this);
        noteBuild.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("New Friend Request!")
                .setContentText(requester.getUserRealName() + " wants to be your friend!")
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
