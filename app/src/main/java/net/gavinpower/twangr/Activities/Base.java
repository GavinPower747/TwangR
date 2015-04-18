package net.gavinpower.twangr.Activities;


import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import net.gavinpower.Database.DBManager;
import net.gavinpower.SignalR.Connection;
import net.gavinpower.Tasks.SignalRConnection;
import net.gavinpower.twangr.R;

import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentActivity;

import java.util.concurrent.ExecutionException;

import static net.gavinpower.twangr.TwangR.repo;

public class Base extends Activity
{

    public NetworkInfo Wifi;
    public NetworkInfo MobileData;

    public final int SPLASH_DISPLAY_LENGTH = 600;

    @Override
    @SuppressWarnings("Unchecked")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        currentActivity = this;
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        Wifi = mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        MobileData = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        Object[] TaskParams = {"http://37.187.35.32:8081/signalr", currentActivity, Wifi, MobileData};
        SignalRConnection ConnectionTask = new SignalRConnection();
        try {
            HubConnection = (Connection) ConnectionTask.execute(TaskParams).get();
        } catch (InterruptedException | ExecutionException ex) {
            Toast.makeText(this, "Unable to connect to web service!", Toast.LENGTH_LONG).show();
        }
        repo = new DBManager(currentActivity);
        repo.open();


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(Base.this,LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
