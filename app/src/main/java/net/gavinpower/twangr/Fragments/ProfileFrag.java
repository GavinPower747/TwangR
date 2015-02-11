package net.gavinpower.twangr.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.gavinpower.twangr.R;

/**
 * Created by Gavin on 11/02/2015.
 */
public class ProfileFrag extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        return rootView;
    }
}
