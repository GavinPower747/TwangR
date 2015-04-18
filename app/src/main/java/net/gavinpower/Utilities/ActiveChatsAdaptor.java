package net.gavinpower.Utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.gavinpower.Models.Chat;
import net.gavinpower.Models.Chats;
import net.gavinpower.Models.Message;
import net.gavinpower.Models.Messages;
import net.gavinpower.Models.User;
import net.gavinpower.twangr.R;

import static net.gavinpower.twangr.TwangR.activeChats;
import static net.gavinpower.twangr.TwangR.messageList;
import static net.gavinpower.twangr.TwangR.currentUser;
import static net.gavinpower.twangr.TwangR.repo;

public class ActiveChatsAdaptor extends BaseAdapter
{
    private Context context;

    public ActiveChatsAdaptor(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return activeChats.size();
    }

    @Override
    public Object getItem(int position) {
        return activeChats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        Chat chat = activeChats.get(position);
        Message latestMessage = messageList.getLatestMessageByChatId(chat.ChatId);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.chat_activechats, null);

        TextView ChatName = (TextView) convertView.findViewById(R.id.ChatName);
        TextView LastSender = (TextView) convertView.findViewById(R.id.LastSender);
        TextView LastMessage = (TextView) convertView.findViewById(R.id.LastMessage);

        User Chatee = new User();

        for(int user : chat.Participants)
        {
            if(user != currentUser.UserId)
                Chatee = repo.getUserById(user);

        }

        ChatName.setText(Chatee.UserName);

        if(latestMessage.message == null) {
            LastSender.setText("");
            LastMessage.setText("No message" + "...");
        }
        else if(latestMessage.message.length() > 20) {
            LastMessage.setText(latestMessage.message.substring(0, 20) + "...");
        }
        else
        {
            LastMessage.setText(latestMessage.message);
        }

        return convertView;
    }
}
