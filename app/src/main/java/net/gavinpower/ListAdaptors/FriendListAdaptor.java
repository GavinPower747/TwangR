package net.gavinpower.ListAdaptors;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.gavinpower.Models.User;
import net.gavinpower.Models.Users;
import net.gavinpower.twangr.R;

public class FriendListAdaptor extends BaseAdapter{
    protected ListView listView;
    private Context context;
    private Users users;

    public FriendListAdaptor(Context context, ListView listView, Users users)
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

        convertView = inflater.inflate(R.layout.friendlist_friend, null);

        FriendRequestHolder holder = new FriendRequestHolder();

        holder.RealName = (TextView) convertView.findViewById(R.id.FriendList_RealName);
        holder.UserName = (TextView) convertView.findViewById(R.id.FriendList_UserName);

        holder.RealName.setText(user.getUserRealName());
        holder.UserName.setText(user.getUserName());

        convertView.setTag(holder);

        return convertView;
    }
}
