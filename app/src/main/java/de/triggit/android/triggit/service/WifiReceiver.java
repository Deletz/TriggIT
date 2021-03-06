package de.triggit.android.triggit.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by DooM on 06.02.2015.
 */
public class WifiReceiver extends BroadcastReceiver {

    private ScanService scanService;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            scanService = ScanService.getInstance(context);
            scanService.checkWLAN();
            Log.d("WifiReceiver", "Have Wifi Connection");
        } else {
            Log.d("WifiReceiver", "Don't have Wifi Connection");
        }
    }

};
