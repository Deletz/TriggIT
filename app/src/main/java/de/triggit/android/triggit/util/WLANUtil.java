package de.triggit.android.triggit.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.triggit.android.triggit.model.WLANTriggItem;
import de.triggit.android.triggit.service.WLANDAO;

/**
 * Created by DooM on 11.02.2015.
 */
public class WLANUtil {


    public static boolean isNetworkPresent(Context context) {
        boolean isNetworkAvailable = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {

            if (cm != null) {
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null) {
                    isNetworkAvailable = netInfo.isConnectedOrConnecting();
                }
            }
        } catch (Exception ex) {
            Log.e("Network Avail Error", ex.getMessage());
        }
        //check for wifi also
        if(!isNetworkAvailable){
            isNetworkAvailable = isWifiAvaible(context);
        }
        Log.d("ScanService", "Network avaible!");
        return isNetworkAvailable;
    }

    public static boolean isWifiAvaible(Context context) {
        WifiManager connec = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (connec.isWifiEnabled()) {
            return true;
        }
        return false;

    }

    public static void openWLANPrompt(Context context) {
        if (!isWifiAvaible(context)) {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            context.startActivity(intent);
        }
    }

    public static List<CharSequence>  getAllSavedWLANs(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        List<CharSequence> result = new ArrayList<>();
        for(WifiConfiguration w :  wifi.getConfiguredNetworks()) {
            if(!isInDB(context, w)) {
                result.add(w.SSID.replaceAll("\"", ""));
            }
        }
        return result;
    }

    public static int getNetworkID(Context context, String SSID) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        for(WifiConfiguration w :  wifi.getConfiguredNetworks()) {
            if(w.SSID.replace("\"","").equals(SSID)) {
                return w.networkId;
            }
        }
        return 0;
    }


    private static boolean isInDB(Context context, WifiConfiguration wifi) {
        List<WLANTriggItem> dbItems = null;
        WLANDAO dao = new WLANDAO(context);
        dao.open();
        dbItems = dao.getAllItems();
        dao.close();

        for(WLANTriggItem item : dbItems) {
            if(item.getNetworkid() == wifi.networkId) {
                Log.d("WLANService", wifi.SSID + " is in db");
                return true;
            }
        }
        Log.d("WLANService", wifi.SSID + " is not in db");
        return false;
    }
}
