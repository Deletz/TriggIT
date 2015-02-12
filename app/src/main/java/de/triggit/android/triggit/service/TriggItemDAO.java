package de.triggit.android.triggit.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.triggit.android.triggit.model.TriggitItem;
import de.triggit.android.triggit.model.WLANTriggItem;

/**
 * Created by DooM on 06.02.2015.
 */
public class TriggItemDAO {

    private SQLiteDatabase database;
    private DatabaseService dbService;
    public TriggItemDAO(SQLiteDatabase database) {
        this.database = database;
    }

    public TriggItemDAO(Context context) {
        dbService = new DatabaseService(context);
    }

    public void open() {
        this.database = dbService.getReadableDatabase();
    }

    public void close() {
        this.dbService.close();
    }

    public TriggitItem createTriggItem(TriggitItem item) {
        ContentValues values = getContentValues(item);
        long insertId = database.insert(TriggitItem.TABLE_NAME, null, values);
        Cursor cursor = database.query(TriggitItem.TABLE_NAME, TriggitItem.ALL_COLUMNS, TriggitItem.COLUMN_ITEM_ID + " = " + insertId, null,null,null,null);
        cursor.moveToFirst();
        TriggitItem dbItem = cursorToTriggitItem(cursor);
        dbItem.setItemId(insertId);
        cursor.close();
        return dbItem;
    }

    public void deleteTriggitItem(TriggitItem item) {
        long id = item.getItemId();
        Log.d("TriggItemDAO", "Delete Item with id: "+ id);
        database.delete(TriggitItem.TABLE_NAME, TriggitItem.COLUMN_ITEM_ID + " = " + id, null);
    }

    public List<TriggitItem> getAllItems() {
        List<TriggitItem> result = new ArrayList<>();
        Cursor cursor = database.query(TriggitItem.TABLE_NAME, TriggitItem.ALL_COLUMNS, null,null,null,null,null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            TriggitItem item = cursorToTriggitItem(cursor);
            result.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public TriggitItem getItemForId(long id) {
        for(TriggitItem item : getAllItems()) {
            if(item.getItemId() == id) {
                return item;
            }
        }
        return null;
    }


    private TriggitItem cursorToTriggitItem(Cursor cursor) {
        String name = cursor.getString(getIndexForColumn(TriggitItem.COLUMN_NAME, cursor));
        boolean bluetooth = cursor.getInt(getIndexForColumn(TriggitItem.COLUMN_BLUETOOTH, cursor)) != 0;
        float brightness = cursor.getFloat(getIndexForColumn(TriggitItem.COLUMN_BRIGHTNESS, cursor));
        boolean nfc = cursor.getInt(getIndexForColumn(TriggitItem.COLUMN_NFC, cursor)) != 0;
        boolean sbeam = cursor.getInt(getIndexForColumn(TriggitItem.COLUMN_SBEAM, cursor)) != 0;
        boolean sync = cursor.getInt(getIndexForColumn(TriggitItem.COLUMN_SYNC, cursor)) != 0;
        boolean vibrate = cursor.getInt(getIndexForColumn(TriggitItem.COLUMN_VIBRATE, cursor)) != 0;
        double volume = cursor.getDouble(getIndexForColumn(TriggitItem.COLUMN_VOLUME, cursor));
        long id = (cursor.getLong(getIndexForColumn(TriggitItem.COLUMN_ITEM_ID, cursor)));
        boolean active = cursor.getInt(getIndexForColumn(TriggitItem.COLUMN_ACTIVE, cursor)) != 0;

        return new TriggitItem(id, name, volume, bluetooth, vibrate, brightness, nfc, sync, sbeam, false, active);
    }


    public long updateItem(TriggitItem item) {
        ContentValues con = getContentValues(item);
        return database.update(TriggitItem.TABLE_NAME, con, "id ='" + item.getItemId()+ "'", null);
    }

    private ContentValues getContentValues(TriggitItem item) {
        ContentValues values = new ContentValues();
        values.put(TriggitItem.COLUMN_NAME, item.getName());
        values.put(TriggitItem.COLUMN_BLUETOOTH, item.isBluetooth());
        values.put(TriggitItem.COLUMN_BRIGHTNESS, item.getBrightness());
        values.put(TriggitItem.COLUMN_NFC, item.isNfc());
        values.put(TriggitItem.COLUMN_SBEAM, item.isSbeam());
        values.put(TriggitItem.COLUMN_SYNC, item.isSync());
        values.put(TriggitItem.COLUMN_VIBRATE, item.isVibrate());
        values.put(TriggitItem.COLUMN_VOLUME, item.getVolume());
        values.put(TriggitItem.COLUMN_ACTIVE, item.getActive());
        return values;
    }


    private int getIndexForColumn(String column, Cursor cursor) {
        return cursor.getColumnIndex(column);
    }

}
