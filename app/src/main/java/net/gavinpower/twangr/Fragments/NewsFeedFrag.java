package net.gavinpower.twangr.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.gavinpower.SignalR.StatusListAdaptor;
import net.gavinpower.twangr.R;

import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentUser;


public class NewsFeedFrag extends Fragment {

    private StatusListAdaptor adapter;

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

    public void populateView()
    {

    }
}
