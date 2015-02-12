package de.triggit.android.triggit.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import de.triggit.android.triggit.model.GPSTriggItem;
import de.triggit.android.triggit.model.TriggitItem;
import de.triggit.android.triggit.model.WLANTriggItem;

/**
 * Created by DooM on 22.01.2015.
 */
public class DatabaseService extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "triggit.db";
    private static final int DATABASE_VERSION = 1;


    private static final String CREATE_TABLE_ITEM = "create table "+ TriggitItem.TABLE_NAME
            + " ("+ TriggitItem.COLUMN_ITEM_ID + " integer primary key autoincrement, "
            + TriggitItem.COLUMN_NAME + " text, "
            + TriggitItem.COLUMN_BLUETOOTH + " integer, "
            + TriggitItem.COLUMN_BRIGHTNESS + " double, "
            + TriggitItem.COLUMN_NFC + " integer, "
            + TriggitItem.COLUMN_SBEAM + " integer, "
            + TriggitItem.COLUMN_SYNC + " integer, "
            + TriggitItem.COLUMN_ACTIVE + " integer, "
            + TriggitItem.COLUMN_VIBRATE + " integer, "
            + TriggitItem.COLUMN_VOLUME + " double);";


   private static final String CREATE_TABLE_WLAN = "create table " + WLANTriggItem.TABLE_NAME
           + " (" + WLANTriggItem.COLUMN_PRIMARY_KEY + " integer primary key autoincrement, "
           + WLANTriggItem.COLUMN_ITEM_FOREIGN_KEY + " integer, "
           + WLANTriggItem.COLUMN_NETWORK_ID + " integer, "
           + "FOREIGN KEY (" + WLANTriggItem.COLUMN_ITEM_FOREIGN_KEY + ") REFERENCES " + TriggitItem.TABLE_NAME+ " ("+TriggitItem.COLUMN_ITEM_ID+"));";


    private static final String CREATE_TABLE_GPS = "create table " + GPSTriggItem.TABLE_NAME
            + " (" + GPSTriggItem.COLUMN_PRIMARY_KEY + " integer primary key autoincrement, "
            + GPSTriggItem.COLUMN_ITEM_FOREIGN_KEY + " integer, "
            + GPSTriggItem.COLUMN_LONGITUDE+ " integer, "
            + GPSTriggItem.COLUMN_LATITUDE+ " integer, "
            + GPSTriggItem.COLUMN_AREA+ " integer, "
            + "FOREIGN KEY (" + GPSTriggItem.COLUMN_ITEM_FOREIGN_KEY + ") REFERENCES " + TriggitItem.TABLE_NAME+ " ("+TriggitItem.COLUMN_ITEM_ID+"));";


    public DatabaseService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_ITEM);
        database.execSQL(CREATE_TABLE_WLAN);
        database.execSQL(CREATE_TABLE_GPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TriggitItem.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WLANTriggItem.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GPSTriggItem.TABLE_NAME);
        onCreate(db);
    }

}