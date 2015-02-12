package de.triggit.android.triggit.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import de.triggit.android.triggit.R;
import de.triggit.android.triggit.SettingsActivity;
import de.triggit.android.triggit.model.GPSTriggItem;
import de.triggit.android.triggit.service.GPSDAO;
import de.triggit.android.triggit.service.ScanService;
/**
 * Created by DooM on 11.02.2015.
 */
public class GPSDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Neue GPS-Konfiguration erstellen")
                .setPositiveButton("Aktuelle Position", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Location currentLocation = ScanService.getInstance(getActivity()).getLocation();
                        Intent inten = new Intent(getActivity(), SettingsActivity.class);
                        inten.putExtra("long", currentLocation.getLongitude());
                        inten.putExtra("lat", currentLocation.getLatitude());
                        inten.putExtra("item_name", "Placeholder");
                        inten.putExtra("item_type", "gps");
                        inten.putExtra("area", 1000L);
                        inten.putExtra("add", true);
                        getActivity().startActivity(inten);
                    }
                })
                .setNegativeButton("Öffne Karte", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Create WLAN
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}