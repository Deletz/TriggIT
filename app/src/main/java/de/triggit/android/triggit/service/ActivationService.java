package de.triggit.android.triggit.service;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.nfc.NfcAdapter;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import de.triggit.android.triggit.model.GPSTriggItem;
import de.triggit.android.triggit.model.TriggitItem;
import de.triggit.android.triggit.model.WLANTriggItem;

/**
 * Created by DooM on 08.01.2015.
 */
public class ActivationService {
    private Context context;

    public ActivationService(Context context) {
    this.context = context;
    }


    public void activateTrigger(TriggitItem item) {
        if(item instanceof WLANTriggItem) {
        activateBluetooth(item);
        setVolume(item);
        setBrightness(item);
        }
        else if(item instanceof GPSTriggItem) {

        } else {
            throw new IllegalArgumentException("Wrong item");
        }
    }

    private void activateBluetooth(TriggitItem item) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {

        if(item.isBluetooth()) {
            Log.d("ActivationSerivce", "activate bluetooth");

            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        } else {
            log("disable bluetooth");
            if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            }
        }
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    private void activateNFC(TriggitItem item) {
        //Does not work
    }

    private void setVolume(TriggitItem item) {
        log("Set Volume to: " + item.getVolume());
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(item.getVolume() == 0) {
            log("Mute Volume");
            if(item.isVibrate()) {
                audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            } else {
                audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }
            audio.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            audio.setStreamMute(AudioManager.STREAM_RING, true);
            audio.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        } else {
            audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            log("Change Volume");
            audio.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            audio.setStreamMute(AudioManager.STREAM_RING, false);
            audio.setStreamMute(AudioManager.STREAM_SYSTEM, false);

            audio.setStreamVolume(AudioManager.STREAM_NOTIFICATION, toInt(audio.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) * item.getVolume()), 0);
            audio.setStreamVolume(AudioManager.STREAM_RING, toInt(audio.getStreamMaxVolume(AudioManager.STREAM_RING)*item.getVolume()), 0);
            audio.setStreamVolume(AudioManager.STREAM_SYSTEM, toInt(audio.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)*item.getVolume()), 0);
        }
        log("Finished Volume");
    }


    private void setBrightness(TriggitItem item) {
        android.provider.Settings.System.putInt(context.getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS, toInt(item.getBrightness()*254 + 1));
    }

    private int toInt(double d) {
        log("toInt : " + d);
        return (int) d;
    }

    private void log(String message) {
        Log.d("ActivationSerivce", message);
    }

    private void error(String message) {
        Log.e("ActivationService", message);
    }


}
