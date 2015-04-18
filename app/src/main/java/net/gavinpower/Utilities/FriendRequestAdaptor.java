package net.gavinpower.Utilities;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import net.gavinpower.Models.User;
import net.gavinpower.Models.Users;
import net.gavinpower.twangr.Activities.FriendsListActivity;
import net.gavinpower.twangr.R;

import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentActivity;
import static net.gavinpower.twangr.TwangR.currentUser;

public class FriendRequestAdaptor extends BaseAdapter {

    protected ListView listView;
    private Context context;
    private Users users;

    public FriendRequestAdaptor(Context context, ListView listView, Users users)
    {
        super();
        this.context = context;
        this.listView = listView;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    protected static class FriendRequestHolder {
        public TextView RealName;
        public TextView UserName;
        public Button Accept;
        public Button Decline;
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = users.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.friendlist_request, null);

        FriendRequestHolder holder = new FriendRequestHolder();

        holder.RealName = (TextView) convertView.findViewById(R.id.FriendRealName);
        holder.UserName = (TextView) convertView.findViewById(R.id.FriendUserName);

        holder.RealName.setText(user.getUserRealName());
        holder.UserName.setText(user.getUserName());

        holder.Accept = (Button) convertView.findViewById(R.id.FriendAccept);
        holder.Decline = (Button) convertView.findViewById(R.id.FriendDecline);

        holder.Accept.setOnClickListener(AcceptClickListener);
        holder.Decline.setOnClickListener(DeclineClickListener);

        convertView.setTag(holder);

        return convertView;
    }

    private View.OnClickListener AcceptClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int position = listView.getPositionForView((View) v.getParent());
            HubConnection.acceptFriendRequest(((User)getItem(position)).getUserId(), currentUser.getUserId());
            ((FriendsListActivity)currentActivity).refreshRequestList();
        }
    };

    private View.OnClickListener DeclineClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int position = listView.getPositionForView((View) v.getParent());
            HubConnection.declineFriendRequest(((User)getItem(position)).getUserId(), currentUser.getUserId());
            ((FriendsListActivity)currentActivity).refreshRequestList();
        }
    };
}
