package de.triggit.android.triggit.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by DooM on 11.02.2015.
 */
public class GPSStartReceiver  extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        ScanService.getInstance(context).checkGPS();
    }
}
