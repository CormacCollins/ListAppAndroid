package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//Usable only by the list table!
public class ItemListTable {
    public static final String ITEM_TABLE_NAME = "item_list_table";
    private static final String ITEM_LIST_TABLE_ID = "item_list_table_id";
    private static final String LIST_ID = "list_id"; //will be the primary key of the list it belongs to
    //Key value pair for item and unique key to the item table
    private static final String ITEM_ID = "item_id";
    private static final String ITEM_COUNT = "item_count";

    public static final String CREATE_STATEMENT_ITEM_LIST =
            "CREATE TABLE " + ITEM_TABLE_NAME +  " (" + ITEM_LIST_TABLE_ID + " integer primary key autoincrement, "
                    + ITEM_ID + " long not null "
                    + ");";

    public static long get_table_list_id(SQLiteDatabase db, long list_id){
        String rawQuery = "SELECT " + ITEM_LIST_TABLE_ID  + " FROM " + ITEM_TABLE_NAME
                + " WHERE " + ITEM_LIST_TABLE_ID + " = " + list_id;
        Cursor c = db.rawQuery(rawQuery, null);

        long id = -1;
        c.moveToFirst();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                id = (c.getInt(c.getColumnIndex(ITEM_LIST_TABLE_ID)));
                c.moveToNext();
            }
        }

        return id;
    }

    public static long add_to_new_item_list_table(SQLiteDatabase db, long[] ids){
        ContentValues values = new ContentValues();
        for(int i = 0; i < ids.length; i++) {
            values.put(ITEM_ID, ids[i]);
        }
        return db.insert(ITEM_TABLE_NAME, null, values);
    }

    public static void add_items_to_existing_listTable(SQLiteDatabase db, long item_id, long item_table_id) {
        ContentValues values = new ContentValues();
        values.put(ITEM_LIST_TABLE_ID, item_table_id);
        values.put(ITEM_ID, item_id);
        db.update(ITEM_TABLE_NAME, values,
                ITEM_LIST_TABLE_ID + " = " + Long.toString(item_table_id),
                null);
    }

//
//        c.moveToFirst();
//        if (c.moveToFirst()) {
//            while (!c.isAfterLast()) {
//                return c.getInt(c.getColumnIndex(ITEM_ID));
//            }
//        }

    //}
//        List<Item> items = ls.getItems();
//
//        //to remove allready existing items
//        int[] ids =  getItemIDS(db, ls);
//        int count = 0;
//
//        boolean finished = false;
//
//        while(!finished) {
//            for (Item it : items) {
//                if (is_same_id(ids, it.getItemId())) {
//                    break;
//                }
//                count++;
//
//            }
//
//            if(count == items.size()){
//                finished = true;
//            }
//            else{
//                items.remove(count);
//                count = 0;
//            }
//
//        }
//
//        //setup for getting ids
//        ids = new int[items.size()];
//        count = 0;
//
//        //add all items and get id's - WE HAVE HANDLED DUPLICATES BEFORE THE ADD ITEM FUNCTION
//        for(Item item : items){
//            int item_id = get_next_id_unique(db, ListTable.ItemTable.ITEM_TABLE, ListTable.ItemTable.ITEM_ID);
//            ListTable.ItemTable.addItem(db, item, ls.getItem_table_id(), item_id);
//            ids[count++] = item_id;
//        }
//
//        //add each id of item tables to the item_table list
//        for(int i = 0; i < ids.length; i++){
//            String rawQuery = "UPDATE " + ITEM_TABLE_NAME
//                    + " SET " + ITEM_ID + " = " + ids[i] + ", "
//                    + ITEM_COUNT + " = " + 1
//                    + " WHERE " + ITEM_LIST_TABLE_ID + " = " + ls.getList_id();
//
//            db.rawQuery(rawQuery, null);
//        }

 //   }


    public static void add_items_to_table_list(SQLiteDatabase db, ListData lstData){
        //Need ot add the items to the item_table
        ContentValues values = new ContentValues();
        String lstName = lstData.getListName();
        List<Item> items = lstData.getItems();

        //print_tables(db, lstData, ITEM_TABLE_NAME);
//
//        int unique_id_item = get_next_id_unique(db, ListTable.ItemTable.ITEM_TABLE, ListTable.ItemTable.ITEM_ID);
//        int listTableID = get_next_id_unique(db, ItemListTable.ITEM_TABLE_NAME, ItemListTable.ITEM_LIST_TABLE_ID);
//
//        //Creating all the items needed (as ItemTable) in the sql lite
//        //then adding those id's to the item list
//        for (Item i : items ) {
//            ListTable.ItemTable.addItem(db, i, listTableID, unique_id_item);
//
//            values.put(ITEM_LIST_TABLE_ID, listTableID);
//            values.put(LIST_ID, lstName);
//            values.put(ITEM_ID, unique_id_item);
//            unique_id_item++;
//            //need to refactor for count
//
//            //TODO query the amount of an item in db to change the count here
//            values.put(ITEM_COUNT, 1);
//
//        }
//
//        db.update(ITEM_TABLE_NAME, values,
//                ITEM_LIST_TABLE_ID + " = " + listTableID, null);


    }

    private static boolean is_same_id(int[] ids, int existingid){
        for(int i = 0; i < ids.length; i++){
            if(ids[i] == existingid){
                return true;
            }
        }
        return false;
    }

    public static int create_item_list_table(SQLiteDatabase db, ListData lstData) {

        ContentValues values = new ContentValues();
        String lstName = lstData.getListName();
        List<Item> items = lstData.getItems();


        //print_tables(db, lstData, ITEM_TABLE_NAME);

//        int unique_item_table_id = get_next_id_unique(db, ITEM_TABLE_NAME, ITEM_LIST_TABLE_ID);
//        int item_id = get_next_id_unique(db, ListTable.ItemTable.ITEM_TABLE, ListTable.ItemTable.ITEM_ID);
//
//        //Creating all the items needed (as ItemTable) in the sql lite
//        //then adding those id's to the item list
//        for (Item i : items ) {
//            //this creates the item before added to the list - obviously needed
//            //We get back the unique id of the item too
//            ListTable.ItemTable.addItem(db, i, unique_item_table_id, item_id);
//
//            values.put(ITEM_LIST_TABLE_ID, unique_item_table_id);
//            values.put(LIST_ID, lstName);
//            values.put(ITEM_ID, item_id);
//            item_id++;
//            //TODO query the amount of an item in db to change the count here
//            values.put(ITEM_COUNT, 1);
//
//        }
////
////        db.insert(ITEM_TABLE_NAME, null, values);
//        //to be given to the table with the list
//        return unique_item_table_id;
        return 1;
    }



//    public static List<Item> getItems(SQLiteDatabase db, ListData ls){
////        List<Item> items = new ArrayList<Item>();
////        int [] ids = getItemIDS(db, ls);
////        for(int i = 0; i < ids.length; i++){
////            items.add(ListTable.ItemTable.getItem(db, ids[i]));
////
////        }
////        return items;
//    }

//    private static int[] getItemIDS(SQLiteDatabase db, ListData ls){
//        String rawQuery = "SELECT * " + " FROM "
//                + ITEM_TABLE_NAME + " WHERE " + ITEM_LIST_TABLE_ID + " = " + ls.getItem_table_id();
//
//        Cursor c;
//        try {
//            c = db.rawQuery(rawQuery, null);
//        }
//        catch(Exception ex){
//            Log.d("GetTableItem ids (SQL)", "TableID " + ls.getItem_table_id() + " does not exist");
//            return null;
//        }
//
//        int[] ids = new int[c.getCount()];
//        if(c!= null) {
//            if (c.moveToFirst()) {
//                while (!c.isAfterLast()) {
//                    int count = 0;
//                    int id =  (c.getInt(c.getColumnIndex(ListTable.ItemTable.ITEM_ID)));
//                    ids[count++] = id;
//
//                    c.moveToNext();
//                }
//            }
//        }
//
//        return ids;
//    }
//
//    public static int getTableIDForList(SQLiteDatabase db, int id){
//        return 1;
//    }

}