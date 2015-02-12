package de.triggit.android.triggit.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by DooM on 11.02.2015.
 */
public class GPSScheduleReceiver extends BroadcastReceiver {

    private ScanService scanService;
    private static final long REPEAT_TIME = 1000 * 45;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("GPS Scheduler", "Start scheduler");
        AlarmManager service = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, GPSStartReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar cal = Calendar.getInstance();
        // start 30 seconds after boot completed
        cal.add(Calendar.SECOND, 30);
        service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(), REPEAT_TIME, pending);
        Log.d("GPS Scheduler", "repeat after 30sec");
    }


};