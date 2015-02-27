package net.gavinpower.SignalR;

import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import net.gavinpower.Models.Statuses;
import net.gavinpower.Models.User;
import net.gavinpower.Models.Users;
import net.gavinpower.twangr.Activities.ChatActivity;
import net.gavinpower.twangr.Activities.LoginActivity;
import net.gavinpower.twangr.Activities.MainActivity;
import net.gavinpower.twangr.Activities.RegisterActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler;
import microsoft.aspnet.signalr.client.Logger;

import static net.gavinpower.twangr.TwangR.currentActivity;

public class Connection
{
    public HubConnection connection;
    public HubProxy distributionHub;

    public ArrayList<Message> messageList = new ArrayList<>();
    public HashMap<String, Message> unsentMessages = new HashMap<>();

    private NetworkInfo Wifi;
    private NetworkInfo MobileData;

    public Connection(String hubURL, NetworkInfo Wifi, NetworkInfo MobileData)
    {
        Logger logger = new Logger() {
            @Override
            public void log(String s, LogLevel logLevel) {
                Log.w("SignalR", s);
            }
        };
        this.connection = new HubConnection(hubURL, "", true, logger);
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
        distributionHub.invoke("Login", username, password).onError(new ErrorCallback() {
            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void getUsersByName(String queryText)
    {
        distributionHub.invoke(Users.class, "GetUsersByName", queryText).done(new Action<Users>() {
            @Override
            public void run(Users list) throws Exception {
                ((MainActivity)currentActivity).populateSuggestions(list);
            }
        });
    }

    public void getNewsFeed(int UserId)
    {
        distributionHub.invoke(Statuses.class,"GetNewsFeed", UserId).done(new Action<Statuses>() {
            @Override
            public void run(Statuses statuses) throws Exception {
                ((MainActivity)currentActivity).populateNewsFeed(statuses);
            }
        }).onError(new ErrorCallback() {
            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void getMyPosts(int UserId)
    {
        distributionHub.invoke(Statuses.class, "GetPostsByUser", UserId).done(new Action<Statuses>() {
            @Override
            public void run(Statuses statuses) {
                ((MainActivity)currentActivity).populateProfile(statuses);
            }
        }).onError(new ErrorCallback() {
            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void insertPost(String postContent, int userId)
    {
        distributionHub.invoke(String.class,"InsertStatus", postContent, userId).done(new Action<String>() {
            @Override
            public void run(String status)
            {
                Log.v("Insert Post", status);
                if(!status.equals("Passed"))
                    Toast.makeText(currentActivity, "Unable to add status", Toast.LENGTH_LONG).show();
                else
                    currentActivity.finish();
            }
        });
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
                        if(currentActivity instanceof ChatActivity && !message.isSelf())
                            ((ChatActivity) currentActivity).addMessageToContainer(message);
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
                        User user = new User(userId, username, "", RealName, Email, NickName, "");
                        ((LoginActivity) currentActivity).loginSuccess(user);
                    }
                });

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void loginFailure(String status)
                    {
                        ((LoginActivity) currentActivity).loginFailure(status);
                    }
                });

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void registerSuccess(int userId, String username, String RealName, String Email, String NickName)
                    {
                        User user = new User(userId, username, RealName, Email, NickName, "", "");
                        ((RegisterActivity)currentActivity).registerSuccess(user);
                    }
                });

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void registerFailure(String reason)
                    {
//                        switch(reason)
//                        {
//
//                        }
                        ((RegisterActivity)currentActivity).registerFailure(reason);
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

    public int getMessageCount()
    {
        return this.messageList.size();
    }
}
