package net.gavinpower.Utilities;

import net.gavinpower.Models.Message;
import net.gavinpower.Models.Messages;
import net.gavinpower.twangr.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessageListAdaptor extends BaseAdapter {
    private Context context;
    private Messages messageList;

    public MessageListAdaptor(Context context, Messages messages)
    {
        this.context = context;
        this.messageList = messages;
    }

    @Override
    public int getCount()
    {
        return messageList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Message m = messageList.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(m.isSelf(m.getSender()))
            convertView = inflater.inflate(R.layout.chat_messageself_frag, null);
        else
            convertView = inflater.inflate(R.layout.chat_message_frag, null);

        TextView sender = (TextView) convertView.findViewById(R.id.lblMsgFrom);
        TextView message = (TextView) convertView.findViewById(R.id.txtMsg);

        sender.setText(m.getSender());
        message.setText(m.getMessage());

        return convertView;
    }


}
