package net.gavinpower.SignalR;

import android.app.Activity;
import android.net.NetworkInfo;
import android.util.Log;

import net.gavinpower.twangr.MainActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

    public ArrayList<Message> messageList = new ArrayList<>();
    public HashMap<String, Message> unsentMessages = new HashMap<>();

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

        if(Wifi.isConnected()) {
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

    public void Send(Message message)
    {
        messageList.add(message);
        unsentMessages.put(message.getMessageID(), message);


        String MessageID = message.getMessageID();
        String messageUp = message.getMessage();
        String sender = message.getSender();
        boolean isSelf = message.isSelf();
        Date TimeStamp = message.getTimeStamp();

        distributionHub.invoke("Send", MessageID, messageUp ,sender, isSelf);
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

        distributionHub.subscribe(new Object() {
            @SuppressWarnings("unused")
            public void addMessage(String MessageID, String messageUp, String sender, boolean isSelf) {
                Message message = new Message(MessageID, sender, messageUp, isSelf, new Date());
                Log.v("Message Recieved", "Name = " + message.getSender() + ", message = " + message.getMessage());
                if(activeActivity instanceof MainActivity && !message.isSelf())
                    ((MainActivity) activeActivity).addMessageToContainer(message);
            }
        });

        distributionHub.subscribe(new Object() {
            @SuppressWarnings("unused")
            public void messageRecieved(String MessageID, String messageUp, String sender, boolean isSelf)
            {
                Message message =  new Message(MessageID, sender, messageUp, isSelf, new Date());
                if(unsentMessages.containsKey(message.getMessageID()))
                {
                    unsentMessages.remove(message.getMessageID());
                }
            }
        });
    }

    public void changeActivity(Activity act)
    {
        this.activeActivity = act;
    }

    public int getMessageCount()
    {
        return this.messageList.size();
    }
}
