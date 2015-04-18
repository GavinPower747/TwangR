package net.gavinpower.twangr.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.gavinpower.ListAdaptors.FriendListAdaptor;
import net.gavinpower.ListAdaptors.StatusListAdaptor;
import net.gavinpower.Models.User;
import net.gavinpower.Models.Users;
import net.gavinpower.twangr.R;

import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentActivity;
import static net.gavinpower.twangr.TwangR.currentUser;
import static net.gavinpower.twangr.TwangR.onlineFriends;

public class OnlineFriendsFrag extends Fragment {

    private FriendListAdaptor adaptor;
    private ListView listView;

    public void refreshOnlineFriends(final Users users)
    {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(users.size() == 0)
                    users.add(new User(0, "Try searching for more above", "", "You have no online friends :(", "", "", ""));

                listView = (ListView)currentActivity.findViewById(R.id.onlinefriends);
                adaptor = new FriendListAdaptor(currentActivity, listView, users);
                listView.setAdapter(adaptor);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        refreshOnlineFriends(onlineFriends);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        View rootView = inflater.inflate(R.layout.fragment_onlinefriends, container, false);

        return rootView;
    }
}
