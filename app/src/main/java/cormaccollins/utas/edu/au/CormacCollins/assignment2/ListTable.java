package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.security.cert.PKIXRevocationChecker;
import java.util.ArrayList;
import java.util.List;

public class ListTable {
        public static final String TABLE_NAME = "list";
        public static final String KEY_LIST_ID = "list_id";
        public static final String LIST_NAME = "name";
        //Key value pair for item and unique key to the item table
        //public static final String ITEM_TABLE_ID = "item_table_id";

        public static final String ITEM_ID = "item_id";
        public static final String CATEGORIES = "categories";
        //For accessibility outside of this class

        //Creates a List Table
        public static final String CREATE_STATEMENT =
                "CREATE TABLE " + TABLE_NAME +  " (" + KEY_LIST_ID + " integer primary key autoincrement, "
                + LIST_NAME + " string not null, " + CATEGORIES + " string not null "
                        + ");";

    public static List<ListData> get_lists(SQLiteDatabase db){
        String rawQuery = "SELECT * " + " FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(rawQuery, null);

        List<ListData> lists = new ArrayList<>();
        c.moveToFirst();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                long list_id = (c.getLong(c.getColumnIndex(KEY_LIST_ID)));
                String name = (c.getString(c.getColumnIndex(LIST_NAME)));
                //long item_table_id = (c.getLong(c.getColumnIndex(ITEM_TABLE_ID)));
                String cats = (c.getString(c.getColumnIndex(CATEGORIES)));
                ListData ls = new ListData(name, new ArrayList<Item>(), cats, list_id, 0);
                lists.add(ls);
                c.moveToNext();
            }
        }

        return lists;
    }

    public static boolean list_exists(SQLiteDatabase db, ListData checkingList){
        List<ListData> lists = get_lists(db);

        for(ListData l : lists){
            if(l.getList_id() == checkingList.getList_id()){
                return true;
            }
        }

        return false;
    }


        public static long add_new_list(SQLiteDatabase db, ListData ls){
                ContentValues values = new ContentValues();
                values.put(LIST_NAME, ls.getListName());
                //values.put(ITEM_TABLE_ID, table_id);
                values.put(CATEGORIES, ls.getCategories());
                return db.insert(TABLE_NAME, null, values);
        }

        public static int deleteList(SQLiteDatabase db, long list_id){

            List<Item> items = ItemTable.getItemsFromItemList(db, list_id);

            //first remove all items
            for(Item i : items){
                ItemTable.deleteItem(db, i.getItemId());
            }

            //next delete the existing list
            String rawQuery = "DELETE " + "FROM " + TABLE_NAME + " WHERE " + KEY_LIST_ID + " = " + "'" + list_id + "'";

            int res = 0;
            try {
                res = db.delete(TABLE_NAME, KEY_LIST_ID + "=?",new String[]{Long.toString(list_id)});
            }
            catch(Exception ex){
                Log.d("DELETE LIST", "Could not delete LIST number " + list_id);
                return -1;
            }

            return res;
        }



}


