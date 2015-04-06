package net.gavinpower.twangr.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.gavinpower.Models.Chat;
import net.gavinpower.Models.Statuses;
import net.gavinpower.Models.User;
import net.gavinpower.Utilities.StatusListAdaptor;
import net.gavinpower.twangr.R;

import java.util.ArrayList;

import microsoft.aspnet.signalr.client.Action;

import static net.gavinpower.twangr.TwangR.activeChats;
import static net.gavinpower.twangr.TwangR.chatExists;
import static net.gavinpower.twangr.TwangR.currentActivity;
import static net.gavinpower.twangr.TwangR.currentUser;
import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.friendList;
import static net.gavinpower.twangr.TwangR.friendRequests;

public class OtherProfileActivity extends ActionBarActivity {

    private int UserId;
    private boolean friend;
    private boolean requestSent;
    private User user;
    private Statuses newsFeed;
    private ListView status;
    private StatusListAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = this.getIntent().getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        UserId = bundle.getInt("UserId");
        status = (ListView) findViewById(R.id.OtherProfile_News);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        currentActivity = this;
        HubConnection.getUserById(UserId).done(new Action<User>() {
            @Override
            public void run(User user) throws Exception {
                populateUser(user);
            }
        });
        HubConnection.getPostsByUserId(UserId);
    }

    public void populateUser(final User user)
    {
        this.user = user;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView RealName = (TextView) findViewById(R.id.OtherProfile_Name);
                TextView NickName = (TextView) findViewById(R.id.OtherProfile_NickName);

                RealName.setText(user.getUserRealName());
                NickName.setText(user.getUserNickName());

                for (int i = 0; i < friendList.size(); i++) {
                    if (UserId == friendList.get(i).getUserId())
                        friend = true;
                }

                for (int i = 0; i < friendRequests.size(); i++) {
                    if (UserId == friendRequests.get(i).getUserId())
                        requestSent = true;
                }

                if (friend) {
                    Button friend = (Button) findViewById(R.id.OtherProfile_AddFriend);
                    friend.setText("Un-Friend");
                } else if (requestSent) {
                    Button friend = (Button) findViewById(R.id.OtherProfile_AddFriend);
                    friend.setText("Request Sent");
                }
            }
        });

    }

    public void populateNewsFeed(final Statuses statuses)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newsFeed = statuses;
                adaptor = new StatusListAdaptor(currentActivity, newsFeed);
                status.setAdapter(adaptor);
            }
        });

    }

    @SuppressWarnings("unchecked")
    public void startChat(View view) {
        final Chat chat = chatExists(user.getUserId());
        final int chatee = user.getUserId();
        if (chat.chatId.equals("NotFound"))
            HubConnection.startChat(user.getUserId()).done(new Action<String>()
            {
                public void run(String chatId)
                {
                    if(chatId.substring(0, 4).equals("Chat")) {
                        Intent intent = new Intent(currentActivity, ChatActivity.class);
                        Bundle info = new Bundle();

                        info.putString("ChatId", chatId);

                        intent.putExtras(info);
                        startActivity(intent);
                        Chat newchat = new Chat();

                        newchat.chatId = chatId;
                        newchat.Participants = new ArrayList<Integer>();
                        newchat.Participants.add(currentUser.getUserId());
                        newchat.Participants.add(chatee);

                        activeChats.add(newchat);
                    }
                    else
                    {
                        currentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                Toast.makeText(currentActivity, "Could not start chat, please check your connection!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        else
        {
            Intent intent = new Intent(currentActivity, ChatActivity.class);
            Bundle content = new Bundle();

            content.putString("ChatID", chat.chatId);

            intent.putExtras(content);
            startActivity(intent);
        }
    }

    public void sendFriendRequest(View view)
    {
        if(!friend)
            HubConnection.sendFriendRequest(currentUser.getUserId(), UserId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friends_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
