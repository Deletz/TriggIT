package de.triggit.android.triggit.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

/**
 * Created by DooM on 11.02.2015.
 */
public class GPSUtil {



    public static boolean isGPSEnabled(Context context) {
        LocationManager service = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }



    public static void openGPSPrompt(Context context) {

        if (!isGPSEnabled(context)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }

    }
}
