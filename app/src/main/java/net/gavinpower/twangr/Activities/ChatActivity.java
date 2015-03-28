package net.gavinpower.twangr.Activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import net.gavinpower.SignalR.Message;
import net.gavinpower.Utilities.MessageListAdaptor;
import net.gavinpower.twangr.R;
import net.gavinpower.twangr.TwangR;

import microsoft.aspnet.signalr.client.Action;

import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentUser;

public class ChatActivity extends Activity {

    net.gavinpower.twangr.TwangR TwangR;

    private String ChatId;
    private int UserId;

    private EditText messageBox;

    private MessageListAdaptor adaptor;
    private List<Message> messageList;
    private ListView messageContainer;

    private String ChatID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Bundle information = getIntent().getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TwangR = ((TwangR) getApplicationContext());
        TwangR.setActivity(this);

<<<<<<< HEAD
        if(TwangR.getCurrentUser() == null)
        {
            startActivity(new Intent(this, LoginActivity.class));
        }
        else {

            ChatId = this.getIntent().getExtras().getString("ChatId");

            messageBox = (EditText) findViewById(R.id.messageBox);
            messageContainer = (ListView) findViewById(R.id.list_view_messages);
=======
        ChatID = information.getString("ChatID");

        messageBox = (EditText) findViewById(R.id.messageBox);
        messageContainer = (ListView) findViewById(R.id.list_view_messages);

        messageList = new ArrayList<Message>();
        adaptor = new MessageListAdaptor(this, messageList);
        messageContainer.setAdapter(adaptor);
>>>>>>> origin/Chat

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        TwangR.setActivity(this);
        if(TwangR.getCurrentUser() == null)
        {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


    public void addMessageToContainer(final Message message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                messageList.add(message);
                adaptor.notifyDataSetChanged();
            }

        });
    }

    public void Send(View view)
    {
        String message = messageBox.getText().toString();
<<<<<<< HEAD
        Message msg = new Message(currentUser.getUserRealName() + HubConnection.getMessageCount(), currentUser.getUserRealName(), message, true, new Date(), this.ChatId);
=======
        Message msg = new Message(currentUser.getUserRealName() + HubConnection.getMessageCount(), currentUser.getUserRealName(), message, true, new Date(), ChatID);
>>>>>>> origin/Chat
        addMessageToContainer(msg);
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
