package de.triggit.android.triggit.service;

import android.app.NotificationManager;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.triggit.android.triggit.R;
import de.triggit.android.triggit.model.GPSTriggItem;
import de.triggit.android.triggit.model.TriggitItem;
import de.triggit.android.triggit.model.WLANTriggItem;
import de.triggit.android.triggit.util.GPSUtil;
import de.triggit.android.triggit.util.WLANUtil;

/**
 * Created by DooM on 09.01.2015.
 */
public class ScanService implements LocationListener {

    private ActivationService activationService;
    private Context context;
    private LocationManager locationManager;
    private String provider;
    private TriggitItem lastItem;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private long MIN_TIME_BW_UPDATES = 120;
    private float MIN_DISTANCE_CHANGE_FOR_UPDATES = 2000;

    private static ScanService instance = null;

    private ScanService(Context context) {
        this.context = context;
        lastItem = null;
        this.activationService = new ActivationService(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
    }

    public static ScanService getInstance(Context context) {
        if(instance == null) {
            instance = new ScanService(context);
        }
        return instance;
    }



    public void checkWLAN() {
        for (WLANTriggItem item : loadAllWLANs()) {
            log("Check Item: " + item.getName() + " active: (" + item.getActive() + ")");
            if (WLANUtil.isNetworkPresent(context) && item.getActive()) {
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    if (checkWLANTrigger(item, wifi)) {
                        log("WLAN Item checked network:" + item.getNetworkid());
                        notifi("Trigged to WLAN " + item.getNetworkid() + " " + item.getName(), "Connected with WLAN");
                        activationService.activateTrigger(item);
                    }
            }
        }
    }

    public void checkGPS() {
        if(!GPSUtil.isGPSEnabled(context) || lastItem != null) {
            return;
        }
        Location location = getLocation();
        if(location == null) {
            return;
        }
        for (GPSTriggItem item : loadAllGPSs()) {
            log("Check Item: " + item.getName());
            if (checkGPSTrigger(item, location) && item.getActive()) {
                //Trigg this GPS
                Log.d("ScanService", "GPS Coordinate found!");
                notifi("Trigged to GPS: " + item.getName(), "Connected with GPS");
                activationService.activateTrigger(item);
                lastItem = item;
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        if(lastItem == null) {
            return;
        }
        if(((GPSTriggItem)lastItem).getLocation().distanceTo(location) > ((GPSTriggItem) lastItem).getArea()) {
            lastItem = null;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        if(provider.equals(LocationManager.GPS_PROVIDER)) {
            lastItem = null;
        }

    }

    public Location getLocation() {
        Location location = null;
        try {
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {


            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    private boolean checkWLANTrigger(WLANTriggItem item, WifiManager wifi) {
        log("Connected WLAN: " + wifi.getConnectionInfo().getNetworkId() + " AND ITEM: " + item.getNetworkid());
        if( wifi.getConnectionInfo().getNetworkId() == item.getNetworkid()) {
            return true;
        }

        return false;
    }

    private boolean checkGPSTrigger(GPSTriggItem item, Location currentLocation) {
        if(currentLocation.distanceTo(item.getLocation()) <= item.getArea()) {
            return true;
        }
        return false;
    }


    private List<GPSTriggItem> loadAllGPSs() {
        List<GPSTriggItem> result = new ArrayList<>();
        GPSDAO gpsdao = new GPSDAO(context);
        gpsdao.open();
        for(GPSTriggItem item : gpsdao.getAllItems()) {
            result.add(item);
        }
        gpsdao.close();
        return result;
    }

    private List<WLANTriggItem> loadAllWLANs() {
        List<WLANTriggItem> result = new ArrayList<>();
        WLANDAO dao = new WLANDAO(context);
        dao.open();
        for(WLANTriggItem item : dao.getAllItems()) {
            result.add(item);
        }
        dao.close();
        return result;
    }

    private void notifi(String text, String title) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(text);


        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(001, mBuilder.build());
    }



    private void log(String message) {
        Log.d("ScanService", message);
    }

    private void error(String message) {
        Log.e("ScanService", message);
    }
}
