package cormaccollins.utas.edu.au.CormacCollins.assignment2;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ViewDebug;

import java.util.ArrayList;
import java.util.List;

public class ItemTable {
    public static final String ITEM_TABLE = "item_table";
    private static final String ITEM_ID = "item_id";
    private static final String LIST_ID = "list_id";
    private static final String ITEM_NAME = "item_name";
    private static final String TAG = "tag";
    private static final String PRICE = "price";
    private static final String COUNT = "count";
    private static final String IS_CHECKED = "is_checked";

    //Creates a List Table
    public static final String CREATE_STATEMENT_ITEM =
            "CREATE TABLE " + ITEM_TABLE +  " (" + ITEM_ID + " integer primary key autoincrement, "
                    + LIST_ID + " int not null, "
                    + ITEM_NAME + " string not null, "
                    + TAG + " string not null, " + PRICE + " real not null, "
                    + IS_CHECKED + " int not null, " +
                    COUNT + " int not null " +
                    ");";


    public static void deleteItem(SQLiteDatabase db, long item_id){
       // String rawQuery = "DELETE " + "FROM " + ITEM_TABLE + " WHERE " + ITEM_ID + " = " + "'" + item_id + "'";

        //int didDel = db.delete(ITEM_TABLE, ITEM_ID + "=" + item_id, null);
        int res = db.delete(ITEM_TABLE, ITEM_ID + "=?",new String[]{Long.toString(item_id)});

    }

    public static boolean itemExists(SQLiteDatabase db, String item_name){
        String rawQuery = "SELECT " + ITEM_ID + " FROM " + ITEM_TABLE + " WHERE " + ITEM_NAME + " = " + "'" +item_name + "'";
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

                //if not equal we can skip calcs and keep looking for the matching items to this list
                if(items_list_id != list_id){
                    continue;
                }


                long item_id = (c.getLong(c.getColumnIndex(ITEM_ID)));
                String item_name = (c.getString(c.getColumnIndex(ITEM_NAME)));
                String tag = (c.getString(c.getColumnIndex(TAG)));
                int count = (c.getInt(c.getColumnIndex(COUNT)));
                Float price = (c.getFloat(c.getColumnIndex(PRICE)));
                int isChecked = (c.getInt(c.getColumnIndex(IS_CHECKED)));


                //Setup Item
                Item it = new Item(item_name, tag, price);
                it.set_id(item_id);
                if(isChecked == 1){it.toggleChecked();}
                if(count >= 1){
                    //increment new item sufficient times
                    for(int j = 1; j < count; j++) {
                        it.incrementCount();
                    }
                }
                it.setList_id(items_list_id);
                items.add(it);
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
        int checked;
        if(i.isChecked()){
            checked = 1;
        }
        else{
            checked = 0;
        }
        values.put(IS_CHECKED, checked);

        return db.insert(ITEM_TABLE, null, values);

    }

    public static int toggleItemCheck(SQLiteDatabase db, Item i){
        ContentValues c = new ContentValues();
        int checked = i.isChecked() ? 1 : 0;
        c.put(IS_CHECKED, checked);
        int rowsEdited = db.update(ITEM_TABLE, c, ITEM_ID + "=" + i.getItemId(), null);
        return rowsEdited;
    }
}