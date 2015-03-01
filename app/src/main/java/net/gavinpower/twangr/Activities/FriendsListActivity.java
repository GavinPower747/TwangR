package net.gavinpower.twangr.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.gavinpower.ListAdaptors.FriendListAdaptor;
import net.gavinpower.ListAdaptors.FriendRequestAdaptor;
import net.gavinpower.Models.User;
import net.gavinpower.twangr.R;

import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentActivity;
import static net.gavinpower.twangr.TwangR.currentUser;
import static net.gavinpower.twangr.TwangR.friendList;
import static net.gavinpower.twangr.TwangR.friendRequests;

public class FriendsListActivity extends ActionBarActivity {

    private ListView Friends;
    private ListView FriendRequests;

    private FriendListAdaptor FriendsAdaptor;
    private FriendRequestAdaptor RequestAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        Friends = (ListView) findViewById(R.id.FriendsList_Friends);
        FriendRequests = (ListView) findViewById(R.id.FriendsList_Requests);
        refreshRequestList();
        refreshFriendList();
    }

    public void refreshRequestList()
    {
        HubConnection.getFriendRequests(currentUser.getUserId());
        RequestAdaptor = new FriendRequestAdaptor(currentActivity, FriendRequests, friendRequests);
        FriendRequests.setAdapter(RequestAdaptor);
    }

    public void refreshFriendList()
    {
        FriendsAdaptor = new FriendListAdaptor(currentActivity, FriendRequests, friendList);
        Friends.setAdapter(FriendsAdaptor);
        Friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle information = new Bundle();
                Intent intent = new Intent();

                information.putInt("UserId", ((User) Friends.getAdapter().getItem(position)).getUserId());
                intent.setClass(FriendsListActivity.this, OtherProfileActivity.class);
                intent.putExtras(information);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
