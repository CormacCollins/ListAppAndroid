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

    private static final String ITEM_NAME = "item_name";
    private static final String TAG = "tag";
    private static final String PRICE = "price";
    private static final String COUNT = "count";

    //Creates a List Table
    public static final String CREATE_STATEMENT_ITEM =
            "CREATE TABLE " + ITEM_TABLE +  " (" + ITEM_ID + " integer primary key autoincrement, "
                    + ITEM_NAME + " string not null, "
                    + TAG + " string not null, " + PRICE + " real not null, " +
                    COUNT + " int not null " +
                    ");";

    public static List<Item> getItemsFromItemTable(SQLiteDatabase db, long item_table_id){
        String rawQuery = "SELECT * " + "FROM " + ITEM_TABLE ;
        Cursor c = db.rawQuery(rawQuery, null);

        long[] ids = itemTablegetIDS(db);

        List<Item> items = new ArrayList<>();
        c.moveToFirst();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                long item_id = (c.getLong(c.getColumnIndex(ITEM_ID)));

                boolean isInTable = false;
                for(long l : ids){
                    if(item_id == l){
                        isInTable = true;
                    }
                }
                //if not from the table we skip adding it
                if(!isInTable){
                    c.moveToNext();
                    continue;
                }

                String item_name = (c.getString(c.getColumnIndex(ITEM_NAME)));
                String tag = (c.getString(c.getColumnIndex(TAG)));
                int count = (c.getInt(c.getColumnIndex(COUNT)));
                Float price = (c.getFloat(c.getColumnIndex(PRICE)));

                Item it = new Item(item_name, tag, price);
                it.set_id(item_id);
                if(count > 1){
                    it.incrementCount();
                }

                items.add(it);
                c.moveToNext();
            }
        }



        return items;
    }

    private static long[] itemTablegetIDS(SQLiteDatabase db){
        String rawQuery = "SELECT " + ITEM_ID + " FROM " + ITEM_TABLE ;
        Cursor c = db.rawQuery(rawQuery, null);

        long[] ids = new long[c.getCount()];
        int count = 0;
        c.moveToFirst();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                ids[count] = c.getLong(c.getColumnIndex(ITEM_ID));
                c.moveToNext();
            }
        }

        return ids;
    }

//    public static Item getItem(SQLiteDatabase db, int itemId){
//        String rawQuery = "SELECT * " + " FROM " + ITEM_TABLE + " WHERE " + ITEM_ID + " = " + itemId;
//
//        Cursor c;
//        try {
//            c = db.rawQuery(rawQuery, null);
//        }
//        catch(Exception ex){
//            Log.d("GetItem (SQL)", "Itemid " + itemId + " does not exist");
//            return null;
//        }
//
//        //should only be 1 item to that id!
//        Item item = null;
//        if (c.moveToFirst()) {
//            while (!c.isAfterLast()) {
//                String name =  c.getString(c.getColumnIndex(ITEM_NAME));
//                String tag =  c.getString(c.getColumnIndex(TAG));
//                int price =  c.getInt(c.getColumnIndex(PRICE));
//
//                item = new Item(name, tag, price);
//
//                return item;
//            }
//        }
//
//        return null;
//
//    }
//
    public static long addItem(SQLiteDatabase db, Item i){
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, i.getItemName());
        values.put(PRICE, i.getItemPrice());
        values.put(TAG, i.getItemTag());
        values.put(COUNT, i.getCount());
        return db.insert(ITEM_TABLE, null, values);

    }

    //Get the last unique id added
//    public static int getLastItemPrimaryKey(SQLiteDatabase db) {
//
//        Cursor c = db.insert("SELECT last_insert_rowid()", null);
//
//        c.moveToFirst();
//        if (c.moveToFirst()) {
//            while (!c.isAfterLast()) {
//                return c.getInt(c.getColumnIndex(ITEM_ID));
//            }
//        }
//
//        Log.d("getLastItemPrimaryKey", "Could not get last uniq id added");
//
//        return -1;
//    }

}