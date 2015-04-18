package net.gavinpower.twangr.Fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import net.gavinpower.Models.Chat;
import net.gavinpower.Utilities.ActiveChatsAdaptor;
import net.gavinpower.twangr.Activities.ChatActivity;
import net.gavinpower.twangr.R;

import static net.gavinpower.twangr.TwangR.currentActivity;

public class ActiveChatsFrag extends Fragment {

    private ActiveChatsAdaptor adaptor;

    @Override
    public void onResume()
    {
        super.onResume();
        adaptor.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        View rootView = inflater.inflate(R.layout.fragment_onlinefriends, container, false);
        final ListView listView = (ListView)rootView.findViewById(R.id.onlinefriends);
        adaptor = new ActiveChatsAdaptor(currentActivity);
        listView.setAdapter(adaptor);
        adaptor.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chat chat = (Chat) listView.getAdapter().getItem(position);

                Bundle bundle = new Bundle();
                Intent intent = new Intent();

                bundle.putString("ChatID", chat.ChatId);
                intent.putExtras(bundle);
                intent.setClass(currentActivity, ChatActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
