package net.gavinpower.twangr.Activities;


import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import net.gavinpower.Models.Message;
import net.gavinpower.Models.Messages;
import net.gavinpower.Utilities.MessageListAdaptor;
import net.gavinpower.twangr.R;
import net.gavinpower.twangr.TwangR;

import microsoft.aspnet.signalr.client.Action;

import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentUser;
import static net.gavinpower.twangr.TwangR.repo;

public class ChatActivity extends Activity {

    net.gavinpower.twangr.TwangR TwangR;

    private EditText messageBox;

    private MessageListAdaptor adaptor;
    private Messages currentMessages;
    private ListView messageContainer;

    private String ChatID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle information = getIntent().getExtras();

        TwangR = ((TwangR) getApplicationContext());
        TwangR.setActivity(this);

        ChatID = information.getString("ChatID");

        messageBox = (EditText) findViewById(R.id.messageBox);
        messageContainer = (ListView) findViewById(R.id.list_view_messages);

        currentMessages =  new Messages();
        adaptor = new MessageListAdaptor(this, currentMessages);
        messageContainer.setAdapter(adaptor);

        currentMessages.getMessagesByChatId(ChatID);
        adaptor.notifyDataSetChanged();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        TwangR.setActivity(this);

        currentMessages.getMessagesByChatId(ChatID);
    }


    public void addMessageToContainer(final Message message, String ChatId)
    {
        if(ChatId.equals(this.ChatID)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currentMessages.add(message);
                    adaptor.notifyDataSetChanged();
                }

            });
        }
        else
        {
            //Notify
        }
    }

    public void Send(View view)
    {
        String message = messageBox.getText().toString();
        Message msg = new Message(currentUser.getUserRealName() + HubConnection.getMessageCount(), currentUser.getUserRealName(), message, true, new Date().toString(), ChatID);

        addMessageToContainer(msg, this.ChatID);
        HubConnection.Send(msg).done(new Action<Message>()
        {
            public void run(Message message)
            {
                //code for guaranteed delivery here
            }
        });
        messageBox.setText("");
    }
}
