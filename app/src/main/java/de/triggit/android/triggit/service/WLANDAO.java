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
public class WLANDAO {

    private SQLiteDatabase database;
    private DatabaseService dbService;

    public WLANDAO(Context context) {
        dbService = new DatabaseService(context);
    }
        public void open() {

            database = dbService.getWritableDatabase();
         // dbService.onUpgrade(database, 0,1);
        }

        public void close() {
            dbService.close();
        }

        public WLANTriggItem createTriggItem(WLANTriggItem item) {
            TriggItemDAO triggDAO = new TriggItemDAO(database);
            TriggitItem newItem = triggDAO.createTriggItem(item);

            ContentValues values = getContentValues(item, newItem.getItemId());

            long insertId = database.insert(WLANTriggItem.TABLE_NAME, null, values);
            Cursor cursor = database.query(WLANTriggItem.TABLE_NAME, new String[] { WLANTriggItem.COLUMN_PRIMARY_KEY, WLANTriggItem.COLUMN_NETWORK_ID, WLANTriggItem.COLUMN_ITEM_FOREIGN_KEY}, WLANTriggItem.COLUMN_PRIMARY_KEY + " = " + insertId, null,null,null,null);
            cursor.moveToFirst();
            WLANTriggItem dbItem = cursorToWLANTriggitItem(cursor);
            cursor.close();
            return dbItem;
        }

    private ContentValues getContentValues(WLANTriggItem item, Long newItem) {
        ContentValues values = new ContentValues();
        values.put(WLANTriggItem.COLUMN_NETWORK_ID, item.getNetworkid());
        if(newItem != null) {
            values.put(WLANTriggItem.COLUMN_ITEM_FOREIGN_KEY, newItem);
        } else {
            values.put(WLANTriggItem.COLUMN_ITEM_FOREIGN_KEY, item.getItemId());
        }
        return values;
    }

    public long updateItem(WLANTriggItem item) {
            ContentValues con = getContentValues(item, null);
            TriggItemDAO triggItemDAO = new TriggItemDAO(database);
            triggItemDAO.updateItem(item);
            return database.update(WLANTriggItem.TABLE_NAME, con, "id ='" + item.getPrimaryKey() + "'", null);
    }

    public void deleteWLANItem(WLANTriggItem item) {
            long id = item.getItemId();
            Log.d("TriggItemDAO", "Delete Item with id: " + id);
            database.delete(WLANTriggItem.TABLE_NAME, WLANTriggItem.COLUMN_PRIMARY_KEY+ " = " + id, null);
            TriggItemDAO triggitDAO = new TriggItemDAO(database);
            triggitDAO.deleteTriggitItem(item);
        }

        public List<WLANTriggItem> getAllItems() {
            List<WLANTriggItem> result = new ArrayList<>();
            Cursor cursor = database.query(WLANTriggItem.TABLE_NAME, new String[] { WLANTriggItem.COLUMN_PRIMARY_KEY, WLANTriggItem.COLUMN_NETWORK_ID, WLANTriggItem.COLUMN_ITEM_FOREIGN_KEY} , null,null,null,null,null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()) {
                WLANTriggItem item = cursorToWLANTriggitItem(cursor);
                result.add(item);
                cursor.moveToNext();
            }
            cursor.close();
            return result;
        }


        private WLANTriggItem cursorToWLANTriggitItem(Cursor cursor) {
            int networkId = cursor.getInt(getIndexForColumn(WLANTriggItem.COLUMN_NETWORK_ID, cursor));
            long triggitItem = cursor.getLong(getIndexForColumn(WLANTriggItem.COLUMN_ITEM_FOREIGN_KEY, cursor));
            long primaryKey = cursor.getLong(getIndexForColumn(WLANTriggItem.COLUMN_PRIMARY_KEY, cursor));
            TriggItemDAO itemDAO = new TriggItemDAO(database);
            TriggitItem item = itemDAO.getItemForId(triggitItem);

            return new WLANTriggItem(primaryKey, networkId, item);
        }

    public WLANTriggItem getWLANItembyId(long id) {
        for(WLANTriggItem item : getAllItems()) {
            if(item.getPrimaryKey() == id) {
                return item;
            }
        }
        return null;
    }

        private int getIndexForColumn(String column, Cursor cursor) {
            return cursor.getColumnIndex(column);
        }
    }


