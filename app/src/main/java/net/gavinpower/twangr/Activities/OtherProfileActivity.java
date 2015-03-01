package net.gavinpower.twangr.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import net.gavinpower.Models.Statuses;
import net.gavinpower.Models.User;
import net.gavinpower.ListAdaptors.StatusListAdaptor;
import net.gavinpower.twangr.R;

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
        HubConnection.getUserById(UserId);
        HubConnection.getPostsByUserId(UserId);
    }

    public void populateUser(User user)
    {
        this.user = user;
        TextView RealName = (TextView) findViewById(R.id.OtherProfile_Name);
        TextView NickName = (TextView) findViewById(R.id.OtherProfile_NickName);

        RealName.setText(user.getUserRealName());
        NickName.setText(user.getUserNickName());

        for(int i = 0; i < friendList.size(); i++)
        {
            if(UserId == friendList.get(i).getUserId())
                friend = true;
        }

        for(int i = 0; i < friendRequests.size(); i++)
        {
            if(UserId == friendRequests.get(i).getUserId())
                requestSent = true;
        }

        if(friend)
        {
            Button friend = (Button) findViewById(R.id.OtherProfile_AddFriend);
            friend.setText("Un-Friend");
        }
        else if(requestSent)
        {
            Button friend = (Button) findViewById(R.id.OtherProfile_AddFriend);
            friend.setText("Request Sent");
        }
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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
