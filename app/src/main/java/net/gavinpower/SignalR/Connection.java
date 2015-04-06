package net.gavinpower.SignalR;

import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import net.gavinpower.Models.Chat;
import net.gavinpower.Models.Chats;
import net.gavinpower.Models.Statuses;
import net.gavinpower.Models.User;
import net.gavinpower.Models.Users;
import net.gavinpower.twangr.Activities.ChatActivity;
import net.gavinpower.twangr.Activities.LoginActivity;
import net.gavinpower.twangr.Activities.MainActivity;
import net.gavinpower.twangr.Activities.OtherProfileActivity;
import net.gavinpower.twangr.Activities.RegisterActivity;
import net.gavinpower.twangr.TwangR;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler;
import microsoft.aspnet.signalr.client.Logger;

import static net.gavinpower.twangr.TwangR.currentActivity;
import static net.gavinpower.twangr.TwangR.currentUser;
import static net.gavinpower.twangr.TwangR.friendList;
import static net.gavinpower.twangr.TwangR.friendRequests;
import static net.gavinpower.twangr.TwangR.onlineFriends;
import static net.gavinpower.twangr.TwangR.activeChats;

public class Connection
{
    public HubConnection connection;
    public HubProxy distributionHub;

    public ArrayList<Message> messageList = new ArrayList<>();
    public HashMap<String, Message> unsentMessages = new HashMap<>();

    private TwangR TwangR;

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

        TwangR = (TwangR) currentActivity.getApplication();

        this.connection.start().done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {
                InitListeners();
                TestConnection();
            }
        }).onError(new ErrorCallback() {
            public void onError(Throwable error)
            {
                connection.start().done(new Action<Void>() {
                    @Override
                    public void run(Void aVoid) throws Exception {
                        InitListeners();
                        TestConnection();
                    }
                });
            }
        });

        this.connection.closed(new Runnable() {
            @Override
            public void run() {
                connection.start().done(new Action<Void>() {
                    @Override
                    public void run(Void aVoid) throws Exception {
                        InitListeners();
                        TestConnection();
                    }
                }).onError(new ErrorCallback() {
                    @Override
                    public void onError(Throwable error)
                    {
                        currentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(currentActivity, "Unable to establish connection! Please check your internet connection.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
    }

    public void TestConnection()
    {
        distributionHub.invoke("TestConnection");
    }

    public SignalRFuture<Message> Send(Message message)
    {
        messageList.add(message);
        unsentMessages.put(message.getMessageID(), message);

        String MessageID = message.getMessageID();
        String messageUp = message.getMessage();
        String sender = message.getSender();
        boolean isSelf = message.isSelf();
        Date TimeStamp = message.getTimeStamp();
        String ChatId = message.getChatId();

        return distributionHub.invoke(Message.class, "Send", MessageID, messageUp ,sender, isSelf, ChatId);
    }

    public SignalRFuture<String> startChat(int chatee)
    {
        return distributionHub.invoke(String.class, "AddChat", currentUser.getUserId(), chatee);
    }

    public void getChats()
    {
        distributionHub.invoke(Chats.class, "GetChats", currentUser.getUserId()).done(new Action<Chats>()
        {
            public void run(Chats chats)
            {
                activeChats = chats;
            }
        });
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
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        Toast toast = Toast.makeText(currentActivity, "There was an error in getting your news feed", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
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

    public SignalRFuture<User> getUserById(int userId)
    {
        return distributionHub.invoke(User.class, "GetUserByID", userId);
    }

    public void getPostsByUserId(int userId)
    {
        distributionHub.invoke(Statuses.class, "GetPostsByUser", userId).done(new Action<Statuses>() {
            @Override
            public void run(Statuses statuses)
            {
                ((OtherProfileActivity) currentActivity).populateNewsFeed(statuses);
            }
        });
    }

    public void getFriendsList(int UserId)
    {
        distributionHub.invoke(Users.class, "GetFriendsList", UserId).done(new Action<Users>() {
            @Override
            public void run(Users list)
            {
                friendList = list;
            }
        });
    }

    public void sendFriendRequest(int sender, int reciever)
    {
        distributionHub.invoke(String.class, "SendFriendRequest", sender, reciever).done(new Action<String>() {
            @Override
            public void run(String rtnMsg)
            {
                Log.v("HubResponse", rtnMsg);
            }
        }).onError(new ErrorCallback() {
            @Override
            public void onError(Throwable error)
            {
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        Toast toast = Toast.makeText(currentActivity, "There was an error in saving your Friend Request", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        });
    }

    public void acceptFriendRequest(int sender, int reciever)
    {
        distributionHub.invoke(String.class, "AcceptFriendRequest", sender, reciever).done(new Action<String>() {
            @Override
            public void run(String rtnMsg)
            {
                Log.v("HubResponse", rtnMsg);
            }
        }).onError(new ErrorCallback() {
            @Override
            public void onError(Throwable throwable)
            {
                throwable.printStackTrace();
                Toast toast = Toast.makeText(currentActivity, "There was an error accepting the friend request", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void getFriendRequests(int userId)
    {
        distributionHub.invoke(Users.class, "GetFriendRequests", userId).done(new Action<Users>() {
            @Override
            public void run(Users users)
            {
                friendRequests = users;
            }
        });
    }

    public void declineFriendRequest(int Sender, int Reciever)
    {
        distributionHub.invoke("DeclineFriendRequest", Sender, Reciever);
    }

    public void getOnlineFriends(int userid)
    {
        distributionHub.invoke(Users.class, "GetOnlineFriends", userid).done(new Action<Users>() {
            @Override
            public void run(Users users) throws Exception {
                onlineFriends = users;
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
                    public void friendChangedStatus()
                    {
                        getOnlineFriends(currentUser.getUserId());
                    }

                });

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void addMessage(String MessageID, String messageUp, String sender, boolean isSelf, String ChatId) {
                        Message message = new Message(MessageID, sender, messageUp, isSelf, new Date(), ChatId);
                        Log.v("Message Recieved", "Name = " + message.getSender() + ", message = " + message.getMessage());
                        if(currentActivity instanceof ChatActivity && !message.isSelf())
                            ((ChatActivity) currentActivity).addMessageToContainer(message);
                    }
                });

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void messageRecieved(String MessageID, String messageUp, String sender, boolean isSelf, String ChatId)
                    {
                        Message message =  new Message(MessageID, sender, messageUp, isSelf, new Date(), ChatId);
                        if(unsentMessages.containsKey(message.getMessageID()))
                        {
                            unsentMessages.remove(message.getMessageID());
                        }
                    }
                });

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void addChat(String ChatId)
                    {
                        Chat chat = new Chat(ChatId, new ArrayList<Integer>());

                        activeChats.add(chat);
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

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void notifyFriendRequest(int UserId)
                    {
                        distributionHub.invoke(User.class, "GetUserById", UserId).done(new Action<User>() {
                            @Override
                            public void run(User user)
                            {
                                TwangR.notifyFriendRequest(user);
                            }
                        });
                    }
                });

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void notifyFriendAccept(int UserId)
                    {
                        distributionHub.invoke(User.class, "GetUserById", UserId).done(new Action<User>() {
                            @Override
                            public void run(User user)
                            {
                                TwangR.notifyFriendAccept(user);
                            }
                        });
                    }
                });
            }
        };
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
    }

    @SuppressWarnings("unchecked")
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
