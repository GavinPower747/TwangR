package net.gavinpower.twangr;

import android.app.Activity;
import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import net.gavinpower.Models.User;
import net.gavinpower.SignalR.Connection;
import net.gavinpower.Tasks.SignalRConnection;

import java.util.concurrent.ExecutionException;

public class TwangR extends Application {

    public static Connection HubConnection;
    public static User currentUser;
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

        Object[] TaskParams = {"http://37.187.35.32:8081/signalr", currentActivity, Wifi, MobileData};
        SignalRConnection ConnectionTask = new SignalRConnection();

        try {
            HubConnection = (Connection) ConnectionTask.execute(TaskParams).get();
        } catch (InterruptedException | ExecutionException ex) {
            Toast.makeText(this, "Unable to connect to web service!", Toast.LENGTH_LONG).show();
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
