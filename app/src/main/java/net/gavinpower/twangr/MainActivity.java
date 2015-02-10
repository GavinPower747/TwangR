package net.gavinpower.twangr;

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
import net.gavinpower.SignalR.MessageListAdaptor;

import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentUser;

public class MainActivity extends Activity {

    TwangR TwangR;

    private EditText messageBox;

    private MessageListAdaptor adaptor;
    private List<Message> messageList;
    private ListView messageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TwangR = ((TwangR) getApplicationContext());
        TwangR.setActivity(this);

        if(TwangR.getCurrentUser() == null)
        {
            startActivity(new Intent(this, LoginActivity.class));
        }
        else {
            messageBox = (EditText) findViewById(R.id.messageBox);
            messageContainer = (ListView) findViewById(R.id.list_view_messages);

            messageList = new ArrayList<Message>();
            adaptor = new MessageListAdaptor(this, messageList);
            messageContainer.setAdapter(adaptor);
        }
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
                if(!message.isSelf())
                    notifyUser(message);
            }

        });
    }

    public void Send(View view)
    {
        String message = messageBox.getText().toString();
        Message msg = new Message(currentUser.getUserRealName() + HubConnection.getMessageCount(), currentUser.getUserRealName(), message, true, new Date());
        addMessageToContainer(msg);
        HubConnection.Send(msg);
        messageBox.setText("");
    }

    public void notifyUser(Message m)
    {
        NotificationCompat.Builder noteBuild = new NotificationCompat.Builder(this);
        noteBuild.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(m.getSender())
                .setContentText(m.getMessage());

        NotificationManager noteManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noteManager.notify(0, noteBuild.build());

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
