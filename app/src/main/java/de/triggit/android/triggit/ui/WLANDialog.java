package de.triggit.android.triggit.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.List;

import de.triggit.android.triggit.SettingsActivity;
import de.triggit.android.triggit.util.WLANUtil;

/**
 * Created by DooM on 11.02.2015.
 */
public class WLANDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("WLAN auswählen");
        final List<CharSequence> wlans = WLANUtil.getAllSavedWLANs(getActivity());
        Log.d("WLANDialog", wlans.toString());
        if(wlans.size() > 0) {

        builder.setItems(wlans.toArray(new CharSequence[wlans.size()]),  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                intent.putExtra("item_type", "wlan");
                long networkId = WLANUtil.getNetworkID(getActivity(), wlans.get(which).toString());
                intent.putExtra("item_id", networkId);
                intent.putExtra("item_name",  wlans.get(which).toString());
                intent.putExtra("add", true);
                startActivity(intent);
            }});
        } else {
            builder.setMessage("Keine Konfigurierbaren W-LANs verfügbar!");
            builder.setNegativeButton("abbrechen", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
        }

       return builder.create();
    }
}