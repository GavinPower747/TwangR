package net.gavinpower.twangr.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentUser;
import static net.gavinpower.twangr.TwangR.currentActivity;

import net.gavinpower.Models.Statuses;
import net.gavinpower.SignalR.StatusListAdaptor;
import net.gavinpower.twangr.R;

public class ProfileFrag extends Fragment {

    private StatusListAdaptor adaptor;
    private ListView status;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        HubConnection.getMyPosts(currentUser.getUserId());
    }



    public void populateMyPosts(final Statuses statuses)
    {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                status = (ListView)currentActivity.findViewById(R.id.myProfileStatus);
                adaptor = new StatusListAdaptor(currentActivity, statuses);
                status.setAdapter(adaptor);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView RealName = (TextView) rootView.findViewById(R.id.profile_username);
        TextView NickName = (TextView) rootView.findViewById(R.id.profile_nickname);

        RealName.setText(currentUser.getUserRealName());
        NickName.setText("(" + currentUser.getUserNickName() + ")");
        return rootView;
    }
}
