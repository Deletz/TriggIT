package de.triggit.android.triggit.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.triggit.android.triggit.model.TriggitItem;
import de.triggit.android.triggit.model.GPSTriggItem;

/**
 * Created by DooM on 06.02.2015.
 */
public class GPSDAO {

    private SQLiteDatabase database;
    private DatabaseService dbService;

    public GPSDAO(Context context) {
        dbService = new DatabaseService(context);
    }
    public void open() {


        database = dbService.getWritableDatabase();
    }

    public void close() {
        dbService.close();
    }

    public GPSTriggItem createTriggItem(GPSTriggItem item) {
        Log.d("GPSDAO", "create new Item");
        TriggItemDAO triggDAO = new TriggItemDAO(database);
        TriggitItem newItem = triggDAO.createTriggItem(item);

        ContentValues values = getContentValues(item, newItem.getItemId());

        long insertId = database.insert(GPSTriggItem.TABLE_NAME, null, values);
        Cursor cursor = database.query(GPSTriggItem.TABLE_NAME, GPSTriggItem.ALL_COLUMNS, GPSTriggItem.COLUMN_PRIMARY_KEY + " = " + insertId, null,null,null,null);
        cursor.moveToFirst();
        GPSTriggItem dbItem = cursorToGPSTriggitItem(cursor);
        cursor.close();
        Log.d("GPSDAO", "Item created: " + dbItem.getPrimarykey());
        return dbItem;
    }

    private ContentValues getContentValues(GPSTriggItem item, Long newItem) {
        ContentValues values = new ContentValues();
        values.put(GPSTriggItem.COLUMN_LONGITUDE, item.getLon());
        values.put(GPSTriggItem.COLUMN_LATITUDE, item.getLat());
        values.put(GPSTriggItem.COLUMN_AREA, item.getArea());
        if(newItem != null) {
            values.put(GPSTriggItem.COLUMN_ITEM_FOREIGN_KEY, newItem);
        } else {
            values.put(GPSTriggItem.COLUMN_ITEM_FOREIGN_KEY, item.getItemId());
        }
        return values;
    }

    public long updateItem(GPSTriggItem item) {
        ContentValues con = getContentValues(item, null);
        TriggItemDAO triggItemDAO = new TriggItemDAO(database);
        triggItemDAO.updateItem(item);
        return database.update(GPSTriggItem.TABLE_NAME, con, "id ='" + item.getPrimarykey() + "'", null);
    }

    public void deleteGPSItem(GPSTriggItem item) {
        long id = item.getPrimarykey();
        Log.d("GPSTriggItemDAO", "Delete Item with id: " + id);
        database.delete(GPSTriggItem.TABLE_NAME, GPSTriggItem.COLUMN_PRIMARY_KEY+ " = " + id, null);
        TriggItemDAO triggitDAO = new TriggItemDAO(database);
        triggitDAO.deleteTriggitItem(item);
    }

    public List<GPSTriggItem> getAllItems() {
        List<GPSTriggItem> result = new ArrayList<>();
        Cursor cursor = database.query(GPSTriggItem.TABLE_NAME, GPSTriggItem.ALL_COLUMNS , null,null,null,null,null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            GPSTriggItem item = cursorToGPSTriggitItem(cursor);
            result.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }


    private GPSTriggItem cursorToGPSTriggitItem(Cursor cursor) {

        double lon = cursor.getDouble(getIndexForColumn(GPSTriggItem.COLUMN_LONGITUDE,cursor));
        double lat = cursor.getDouble(getIndexForColumn(GPSTriggItem.COLUMN_LATITUDE,cursor));
        long area = cursor.getLong(getIndexForColumn(GPSTriggItem.COLUMN_AREA,cursor));
        long triggitItem = cursor.getLong(getIndexForColumn(GPSTriggItem.COLUMN_ITEM_FOREIGN_KEY, cursor));
        long primaryKey = cursor.getLong(getIndexForColumn(GPSTriggItem.COLUMN_PRIMARY_KEY, cursor));
        TriggItemDAO itemDAO = new TriggItemDAO(database);
        TriggitItem item = itemDAO.getItemForId(triggitItem);

        return new GPSTriggItem(primaryKey, lon, lat, area, item);
    }

    public GPSTriggItem getGPSItembyId(long id) {
        for(GPSTriggItem item : getAllItems()) {
            if(item.getPrimarykey() == id) {
                return item;
            }
        }
        return null;
    }

    private int getIndexForColumn(String column, Cursor cursor) {
        return cursor.getColumnIndex(column);
    }
}


