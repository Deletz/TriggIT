package de.triggit.android.triggit.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import de.triggit.android.triggit.util.GPSUtil;
import de.triggit.android.triggit.util.WLANUtil;

/**
 * Created by DooM on 11.02.2015.
 */
public class AddDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Neue Konfiguration erstellen")
                .setPositiveButton("GPS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (GPSUtil.isGPSEnabled(getActivity())) {
                            new GPSDialog().show(getFragmentManager(), "GPSDIALOG");
                        } else {
                            GPSUtil.openGPSPrompt(getActivity());
                        }
                    }
                })
                .setNegativeButton("WLAN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Create WLAN
                        if (WLANUtil.isWifiAvaible(getActivity())) {
                            new WLANDialog().show(getFragmentManager(), "WLANDIALOG");
                        } else {
                            WLANUtil.openWLANPrompt(getActivity());
                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}