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
import static net.gavinpower.twangr.TwangR.newsFeed;


public class NewsFeedFrag extends Fragment {

    private StatusListAdaptor adaptor;
    private ListView listView;

    @Override
    public void onResume()
    {
        super.onResume();
        newsFeed.getNewsFeed();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        View rootView = inflater.inflate(R.layout.fragment_newsfeed, container, false);
        listView = (ListView)rootView.findViewById(R.id.newsFeed);
        adaptor = new StatusListAdaptor(currentActivity, newsFeed);
        listView.setAdapter(adaptor);
        return rootView;
    }

    public void populateNewsFeed()
    {
        try {
            adaptor.notifyDataSetChanged();
        }
        catch(NullPointerException NPE)
        {

        }
    }
}
