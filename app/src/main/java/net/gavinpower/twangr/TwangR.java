package net.gavinpower.twangr;

import android.app.Activity;
import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import net.gavinpower.SignalR.Connection;

/**
 * Created by New User on 24/01/2015.
 */
public class TwangR extends Application {
    public Connection HubConnection;
    public NetworkInfo Wifi;
    public NetworkInfo MobileData;
    public Activity currentActivity;

    @Override
    public void onCreate()
    {
        super.onCreate();

        ConnectivityManager mgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        Wifi = mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        MobileData = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        HubConnection = new Connection("http://37.187.35.32:8081/signalr", currentActivity, Wifi, MobileData);
    }

    public Connection getConnection()
    {
        return this.HubConnection;
    }

    public void setActivity(Activity activity)
    {
        this.currentActivity = activity;
        HubConnection.changeActivity(currentActivity);
    }
}
