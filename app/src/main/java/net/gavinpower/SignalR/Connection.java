package net.gavinpower.SignalR;

import android.app.Activity;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import net.gavinpower.Models.User;
import net.gavinpower.twangr.Activities.ChatActivity;
import net.gavinpower.twangr.Activities.LoginActivity;
import net.gavinpower.twangr.Activities.RegisterActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler;
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

        this.connection.start().done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {
                InitListeners();
                TestConnection();
            }
        });

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

    public void login(String username, String password)
    {
        distributionHub.invoke("Login", username, password);
    }


    public void InitListeners()
    {
        Runnable listener = new Runnable() {
            @Override
            public void run() {
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
                        if(activeActivity instanceof ChatActivity && !message.isSelf())
                            ((ChatActivity) activeActivity).addMessageToContainer(message);
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

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void loginSuccess(int userId, String username, String RealName, String Email, String NickName)
                    {
                        User user = new User(userId, username, RealName, Email, NickName);
                        ((LoginActivity) activeActivity).loginSuccess(user);
                    }
                });

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void loginFailure(String status)
                    {
                        ((LoginActivity) activeActivity).loginFailure(status);
                    }
                });

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void registerSuccess(int userId, String username, String RealName, String Email, String NickName)
                    {
                        User user = new User(userId, username, RealName, Email, NickName);
                        ((RegisterActivity)activeActivity).registerSuccess(user);
                    }
                });

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void registerFailure(String reason)
                    {
                        switch(reason)
                        {

                        }
                        ((RegisterActivity)activeActivity).registerFailure(reason);
                    }
                });
            }
        };
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
    }

    public void register(String userName, String userPassword, String userRealName, String userEmail, String userNickName)
    {
        Object[] TaskParams = {userName, userPassword, userRealName, userEmail, userNickName};
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                distributionHub.invoke("Register", params[0], params[1], params[2], params[3], params[4]);
                return null;
            }
        }.execute(TaskParams);
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
