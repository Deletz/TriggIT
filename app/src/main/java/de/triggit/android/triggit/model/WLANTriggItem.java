package de.triggit.android.triggit.model;

/**
 * Created by DooM on 08.01.2015.
 */
public class WLANTriggItem extends TriggitItem {

    public static final String TABLE_NAME = "WLAN_ITEM";
    public static final String COLUMN_PRIMARY_KEY = "primary_key";
    public static final String COLUMN_NETWORK_ID = "network_id";
    public static final String COLUMN_ITEM_FOREIGN_KEY = "item_id";




    private int networkid;


    private long primaryKey;

    public WLANTriggItem() {

    }

    public WLANTriggItem(String name, Double volume, Boolean bluetooth, Boolean vibrate, Float brightness, Boolean nfc, Boolean sync, Boolean sbeam, Boolean energy, int networkid, Boolean active) {
        super(name, volume, bluetooth, vibrate, brightness, nfc, sync, sbeam, energy, active);
        this.networkid = networkid;
    }

    public WLANTriggItem(long id, int networkId, TriggitItem item) {
        super(item);
        this.networkid = networkId;
        this.primaryKey = id;
    }

    public int getNetworkid() {
        return networkid;
    }


    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setNetworkid(int networkid) {
        this.networkid = networkid;
    }

    public static WLANTriggItem dummyItem = new WLANTriggItem("eduroam", 0.8, true, true, 0.2f, true, true, true, true, 3, true);
    public static WLANTriggItem dummyItem2 = new WLANTriggItem("zuhause", 0.0, true, true, 0.2f, true, true, true, true, 1, true);
}
