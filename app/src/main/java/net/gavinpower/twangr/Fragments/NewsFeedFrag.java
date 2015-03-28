package net.gavinpower.twangr.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.gavinpower.Utilities.StatusListAdaptor;
import net.gavinpower.Models.Statuses;
import net.gavinpower.twangr.R;

import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentActivity;
import static net.gavinpower.twangr.TwangR.currentUser;


public class NewsFeedFrag extends Fragment {

    private StatusListAdaptor adaptor;
    private ListView listView;

    @Override
    public void onResume()
    {
        super.onResume();
        HubConnection.getNewsFeed(currentUser.getUserId());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        View rootView = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        return rootView;
    }

    public void populateNewsFeed(final Statuses statuses)
    {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView = (ListView)currentActivity.findViewById(R.id.newsFeed);
                adaptor = new StatusListAdaptor(currentActivity, statuses);
                listView.setAdapter(adaptor);
            }
        });
    }
}
