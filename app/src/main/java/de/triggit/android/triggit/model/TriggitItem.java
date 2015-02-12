package de.triggit.android.triggit.model;

/**
 * Created by DooM on 08.01.2015.
 */
public class TriggitItem {


    public static final String TABLE_NAME = "triggit_item";
    public static final String COLUMN_ITEM_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_VOLUME = "volume";
    public static final String COLUMN_BLUETOOTH = "bluetooth";
    public static final String COLUMN_VIBRATE = "vibrate";
    public static final String COLUMN_BRIGHTNESS = "brightness";
    public static final String COLUMN_NFC = "nfc";
    public static final String COLUMN_SYNC = "sync";
    public static final String COLUMN_SBEAM = "sbeam";
    public static final String COLUMN_ACTIVE = "active";

    public static final String[] ALL_COLUMNS = new String[] {
            COLUMN_ITEM_ID,
            COLUMN_VOLUME,
            COLUMN_BLUETOOTH,
            COLUMN_NAME,
            COLUMN_VIBRATE,
            COLUMN_SYNC,
            COLUMN_SBEAM,
            COLUMN_NFC,
            COLUMN_ACTIVE,
            COLUMN_BRIGHTNESS
    };

    private long id;
    private String name;
    private Double volume;
    private Boolean bluetooth;
    private Boolean vibrate;
    private Float brightness;
    private Boolean nfc; // not supported
    private Boolean sync; //not supported
    private Boolean sbeam; //
    private Boolean active;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isEnergy() {
        return energy;
    }

    public void setEnergy(Boolean energy) {
        this.energy = energy;
    }

    public Boolean isNfc() {
        return nfc;
    }

    public void setNfc(Boolean nfc) {
        this.nfc = nfc;
    }

    public Boolean isSync() {
        return sync;
    }

    public void setSync(Boolean sync) {
        this.sync = sync;
    }

    public Boolean isSbeam() {
        return sbeam;
    }

    public void setSbeam(Boolean sbeam) {
        this.sbeam = sbeam;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Boolean isBluetooth() {
        return bluetooth;
    }

    public long getItemId() {
        return id;
    }

    public void setItemId(long id) {
        this.id = id;
    }

    public void setBluetooth(Boolean bluetooth) {
        this.bluetooth = bluetooth;
    }

    public Boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(Boolean vibrate) {
        this.vibrate = vibrate;
    }

    public Float getBrightness() {
        return brightness;
    }

    public void setbrightness(Float brightness) {
        this.brightness = brightness;
    }

    private Boolean energy;

    public TriggitItem( String name, Double volume, Boolean bluetooth, Boolean vibrate, Float brightness, Boolean nfc, Boolean sync, Boolean sbeam, Boolean energy, boolean active) {
        this.name = name;
        this.volume = volume;
        this.bluetooth = bluetooth;
        this.vibrate = vibrate;
        this.brightness = brightness;
        this.nfc = nfc;
        this.sync = sync;
        this.sbeam = sbeam;
        this.energy = energy;
        this.active = active;
    }

    public TriggitItem(long id, String name, Double volume, Boolean bluetooth, Boolean vibrate, Float brightness, Boolean nfc, Boolean sync, Boolean sbeam, Boolean energy, Boolean active) {
        this(name, volume, bluetooth, vibrate, brightness, nfc, sync,sbeam, energy, active);
        this.id = id;
    }

    public TriggitItem(TriggitItem item) {
        this(item.getName(), item.getVolume(), item.isBluetooth(), item.isVibrate(), item.getBrightness(), item.isNfc(), item.isSync(), item.isSbeam(), false, item.getActive());
        this.id = item.getItemId();
    }

    public TriggitItem() {
        this.active = true;

    }





}
