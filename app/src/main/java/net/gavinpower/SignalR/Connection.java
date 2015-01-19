package net.gavinpower.SignalR;

import android.app.Activity;
import android.net.NetworkInfo;
import android.util.Log;

import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler;
import microsoft.aspnet.signalr.client.transport.LongPollingTransport;
import microsoft.aspnet.signalr.client.Logger;

public class Connection
{
    public HubConnection connection;
    public HubProxy distributionHub;
    public Activity activeActivity;

    private NetworkInfo Wifi;
    private NetworkInfo MobileData;

    public Connection(String hubURL, Activity currentActivity, NetworkInfo Wifi, NetworkInfo MobileData)
    {
        Logger logger = new Logger() {
            @Override
            public void log(String s, LogLevel logLevel) {
                Log.w("SignalR", s);
            }
        };
        this.connection = new HubConnection(hubURL);
        this.activeActivity = currentActivity;
        this.Wifi = Wifi;
        this.MobileData = MobileData;
        distributionHub = this.connection.createHubProxy("DistributionHub");

        if(Wifi.isConnected() && Wifi != null) {
            this.connection.start().done(new Action<Void>() {
                @Override
                public void run(Void aVoid) throws Exception {
                    InitListeners();
                    TestConnection();
                }
            });
        }
        else
        {
            this.connection.start(new LongPollingTransport(logger)).done(new Action<Void>() {
                @Override
                public void run(Void aVoid) throws Exception {
                    InitListeners();
                    TestConnection();
                }
            });
        }
    }

    public void TestConnection()
    {
        distributionHub.invoke("TestConnection");
    }

    public void InitListeners()
    {
        distributionHub.on("ConnectionSuccessful",
                new SubscriptionHandler() {
                    @Override
                    public void run() {
                        Log.w("CallBack from Hub to Client", "Successful Connection");
                    }
                });
    }

    public void changeActivity(Activity act)
    {
        this.activeActivity = act;
    }
}
