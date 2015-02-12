package de.triggit.android.triggit.model;

import android.location.Location;

/**
 * Created by DooM on 08.01.2015.
 */
public class GPSTriggItem extends TriggitItem {


    public static final String TABLE_NAME = "GPS_ITEM";
    public static final String COLUMN_PRIMARY_KEY = "primary_key";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_ITEM_FOREIGN_KEY = "item_id";
    public static final String COLUMN_AREA = "area";

    public static final String[] ALL_COLUMNS = new String[] {COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_PRIMARY_KEY, COLUMN_AREA, COLUMN_ITEM_FOREIGN_KEY};

    private double lat;
    private double lon;
    private long area;
    private long primarykey;

    public GPSTriggItem() {

    }

    public GPSTriggItem(String name, Double volume, Boolean bluetooth, Boolean vibrate, Float brightness, Boolean nfc, Boolean sync, Boolean sbeam, Boolean energy, double lat, double lon, long area, Boolean active) {
        super(name, volume, bluetooth, vibrate, brightness, nfc, sync, sbeam, energy, active);
        this.lat = lat;
        this.lon = lon;
        this.area = area;
    }

    public long getPrimarykey() {
        return primarykey;
    }

    public void setPrimarykey(long primarykey) {
        this.primarykey = primarykey;
    }
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getArea() {
        return area;
    }

    public void setArea(long area) {
        this.area = area;
    }

    public Location getLocation() {
        Location loc = new Location("self");
        loc.setLatitude(lat);
        loc.setLongitude(lon);
        return loc;
    }

    public GPSTriggItem(long id, double lon, double lat, long area, TriggitItem item) {
        super(item);
        this.primarykey = id;
        this.lon = lon;
        this.lat = lat;
        this.area = area;
    }


}
