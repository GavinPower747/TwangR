package net.gavinpower.twangr.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.gavinpower.Models.Status;
import net.gavinpower.twangr.R;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Action;

import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentUser;
import static net.gavinpower.twangr.TwangR.currentActivity;
import static net.gavinpower.twangr.TwangR.repo;

public class AddNewPostActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);
        currentActivity = this;
    }

    public void addNewPost(View view)
    {
        EditText postContent = (EditText)findViewById(R.id.Post_Content);
        Status status = new Status();

        status.StatusAuthorID = currentUser.UserId;
        status.StatusAuthor = currentUser.UserRealName;
        status.StatusContent = postContent.getText().toString();
        status.StatusLikes = 0;
        status.LogDate = new Date().toString();

        try
        {
            int id = HubConnection.insertPost(postContent.getText().toString(), currentUser.getUserId()).get();

            if (id == -1)
                Toast.makeText(currentActivity, "Unable to add status", Toast.LENGTH_LONG).show();
            else {
                status.StatusId = id;
                repo.insertStatus(status);
                currentActivity.finish();
            }
        }
        catch (InterruptedException | ExecutionException ex)
        {
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(currentActivity, "There was an error in inserting your post!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void Cancel(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
