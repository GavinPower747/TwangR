package net.gavinpower.SignalR;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import net.gavinpower.Models.Chat;
import net.gavinpower.Models.Chats;
import net.gavinpower.Models.Message;
import net.gavinpower.Models.Messages;
import net.gavinpower.Models.Statuses;
import net.gavinpower.Models.User;
import net.gavinpower.Models.Users;
import net.gavinpower.twangr.Activities.ChatActivity;
import net.gavinpower.twangr.Activities.LoginActivity;
import net.gavinpower.twangr.Activities.MainActivity;
import net.gavinpower.twangr.Activities.RegisterActivity;

import java.util.ArrayList;
import java.util.HashMap;

import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;

import static net.gavinpower.twangr.TwangR.currentActivity;
import static net.gavinpower.twangr.TwangR.currentUser;
import static net.gavinpower.twangr.TwangR.friendList;
import static net.gavinpower.twangr.TwangR.friendRequests;
import static net.gavinpower.twangr.TwangR.messageList;
import static net.gavinpower.twangr.TwangR.onlineFriends;
import static net.gavinpower.twangr.TwangR.activeChats;
import static net.gavinpower.twangr.TwangR.chatExists;
import static net.gavinpower.twangr.TwangR.repo;

public class Connection
{
    public HubConnection connection;
    public HubProxy distributionHub;
    public HashMap<String, Message> unsentMessages = new HashMap<>();

    public Connection(String hubURL, NetworkInfo Wifi, NetworkInfo MobileData)
    {
        Logger logger = new Logger() {
            @Override
            public void log(String s, LogLevel logLevel) {
                Log.w("SignalR", s);
            }
        };

        this.connection = new HubConnection(hubURL, "", true, logger);

        distributionHub = this.connection.createHubProxy("DistributionHub");

        this.connection.start().done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {
                InitListeners();
            }
        }).onError(new ErrorCallback() {
            public void onError(Throwable error)
            {
                connection.start().done(new Action<Void>() {
                    @Override
                    public void run(Void aVoid) throws Exception {
                        InitListeners();
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
                distributionHub.invoke("JoinRealTime", currentUser);
            }
        });
    }

    public SignalRFuture<Message> Send(Message message)
    {
        messageList.add(message);
        unsentMessages.put(message.getMessageID(), message);

        String MessageID = message.getMessageID();
        String messageUp = message.getMessage();
        String sender = message.getSender();
        boolean isSelf = message.isSelf(sender);
        String TimeStamp = message.getTimeStamp();
        String ChatId = message.getChatId();

        repo.insertMessage(message);
        messageList.add(message);

        return distributionHub.invoke(Message.class, "Send", MessageID, messageUp ,sender, TimeStamp, isSelf, ChatId);
    }

    public SignalRFuture<String> startChat(int chatee)
    {
        return distributionHub.invoke(String.class, "AddChat", currentUser.getUserId(), chatee);
    }
    @SuppressWarnings("unchecked")
    public void getChats()
    {
        distributionHub.invoke(Chats.class, "GetChats", currentUser.getUserId()).done(new Action<Chats>()
        {
            public void run(Chats chats)
            {
                activeChats = chats;
                    new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] params) {
                            for(Chat chat : activeChats)
                            {
                                try {
                                    Messages messages = getMessagesByChatId(chat.ChatId).get();
                                    for (Message message : messages)
                                        messageList.add(message);
                                }
                                catch(Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                            return null;
                        }
                    }.execute();
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

    public SignalRFuture<Statuses> getNewsFeed(int UserId)
    {
        return distributionHub.invoke(Statuses.class, "GetNewsFeed", UserId);
    }

    public SignalRFuture<Statuses> getMyPosts(int UserId)
    {
        return distributionHub.invoke(Statuses.class, "GetPostsByUser", UserId);
    }

    public SignalRFuture<Integer> insertPost(String postContent, int userId)
    {
        return distributionHub.invoke(Integer.class,"InsertStatus", postContent, userId);
    }

    public SignalRFuture<Messages> getMessagesByChatId(String ChatId)
    {
        return distributionHub.invoke(Messages.class, "GetMessagesByChatId", ChatId);
    }

    public SignalRFuture<User> getUserById(int userId)
    {
        return distributionHub.invoke(User.class, "GetUserByID", userId);
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
                                Log.w("CallBack from Hub", "Successful Connection");
                            }
                        });

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void friendChangedStatus()
                    {
                        getOnlineFriends(currentUser.getUserId());
                    }

                });

                distributionHub.on("addMessage", new SubscriptionHandler2<Message, String>()
                {
                    @Override
                    public void run(Message message, String ChatId)
                    {
                        if(currentActivity instanceof ChatActivity && chatExists(ChatId) && !message.isSelf(message.getSender())) {
                            ((ChatActivity) currentActivity).addMessageToContainer(message, ChatId);
                            repo.insertMessage(message);
                            messageList.add(message);
                        }
                        else if(chatExists(ChatId))
                        {
                            Intent broadcastIntent = new Intent("TwangR_MessageRecieved");
                            Bundle content = new Bundle();

                            content.putString("ChatID", ChatId);
                            content.putString("UserName", message.sender);
                            content.putString("MessageContent", message.message);

                            broadcastIntent.putExtras(content);

                            currentActivity.sendBroadcast(broadcastIntent);

                            repo.insertMessage(message);
                            messageList.add(message);
                        }
                    }
                }, Message.class, String.class);

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
                                Intent broadcastIntent = new Intent();
                                Bundle content = new Bundle();
                                broadcastIntent.setAction("TwangR_FriendRequest");
                                content.putString("UserName", user.UserName);
                                broadcastIntent.putExtras(content);

                                currentActivity.sendBroadcast(broadcastIntent);
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
                                Intent broadcastIntent = new Intent();
                                Bundle content = new Bundle();
                                broadcastIntent.setAction("TwangR_FriendAccept");
                                content.putString("UserName", user.UserName);
                                broadcastIntent.putExtras(content);

                                currentActivity.sendBroadcast(broadcastIntent);
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
        return messageList.size();
    }
}
