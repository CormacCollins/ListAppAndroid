package cormaccollins.utas.edu.au.CormacCollins.assignment2;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ItemTable {
    public static final String ITEM_TABLE = "item_table";
    private static final String ITEM_TABLE_ID = "item_table_id";
    private static final String ITEM_ID = "item_id";

    private static final String LIST_ID = "list_id";

    private static final String ITEM_NAME = "item_name";
    private static final String TAG = "tag";
    private static final String PRICE = "price";
    private static final String COUNT = "count";

    //Creates a List Table
    public static final String CREATE_STATEMENT_ITEM =
            "CREATE TABLE " + ITEM_TABLE +  " (" + ITEM_ID + " integer primary key autoincrement, "
                    + LIST_ID + " int not null, "
                    + ITEM_NAME + " string not null, "
                    + TAG + " string not null, " + PRICE + " real not null, " +
                    COUNT + " int not null " +
                    ");";


    public static boolean deleteItem(SQLiteDatabase db, long item_id){
        String rawQuery = "DELETE " + "FROM " + ITEM_TABLE + " WHERE " + ITEM_ID + " = " + "'" + item_id + "'";

        try {
            Cursor c = db.rawQuery(rawQuery, null);
        }
        catch(Exception ex){
            Log.d("DELETE ITEM", "Could not delete item number " + item_id);
            return false;
        }

        return true;
    }

    public static boolean itemExists(SQLiteDatabase db, String item_name){
        String rawQuery = "SELECT COUNT(*) " + "FROM " + ITEM_TABLE + " WHERE " + ITEM_NAME + " = " + "'" +item_name + "'";
        Cursor c = db.rawQuery(rawQuery, null);
        int count = c.getCount();
        if(count <= 0){
            return false;
        }
        return true;
    }

    public static List<Item> getItemsFromItemList(SQLiteDatabase db, long list_id){
        String rawQuery = "SELECT * " + "FROM " + ITEM_TABLE ;
        Cursor c = db.rawQuery(rawQuery, null);

        //long[] ids = itemTablegetIDS(db);

        List<Item> items = new ArrayList<>();
        c.moveToFirst();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                long items_list_id = (c.getLong(c.getColumnIndex(LIST_ID)));
                String item_name = (c.getString(c.getColumnIndex(ITEM_NAME)));
                String tag = (c.getString(c.getColumnIndex(TAG)));
                int count = (c.getInt(c.getColumnIndex(COUNT)));
                Float price = (c.getFloat(c.getColumnIndex(PRICE)));

                Item it = new Item(item_name, tag, price);
                it.set_id(items_list_id);
                if(count > 1){
                    it.incrementCount();
                }

                if(items_list_id == list_id){
                    items.add(it);
                }

                c.moveToNext();
            }
        }



        return items;
    }

    //Will return -1 if insert fails - maybe it already exists
    public static long addItem(SQLiteDatabase db, Item i, long list_id){
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, i.getItemName());
        values.put(PRICE, i.getItemPrice());
        values.put(TAG, i.getItemTag());
        values.put(COUNT, i.getCount());
        values.put(LIST_ID, list_id);
        return db.insert(ITEM_TABLE, null, values);

    }
}