package net.gavinpower.Tasks;

import android.net.NetworkInfo;
import android.os.AsyncTask;

import net.gavinpower.SignalR.Connection;


public class SignalRConnection extends AsyncTask {

    @Override
    protected Connection doInBackground(Object... params)
    {
        String HubAddress = (String)params[0];
        NetworkInfo Wifi = (NetworkInfo)params[2];
        NetworkInfo MobileData = (NetworkInfo)params[3];
        return new Connection(HubAddress, Wifi, MobileData);
    }

    @Override
    protected void onPostExecute(Object HubConnection)
    {
        super.onPostExecute(HubConnection);
    }

}
