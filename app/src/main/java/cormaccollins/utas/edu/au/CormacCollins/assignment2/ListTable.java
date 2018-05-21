package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.security.cert.PKIXRevocationChecker;
import java.util.ArrayList;
import java.util.List;


// ----------------------------------------------------------------------------------------------------------------
// Adds a ListTable, which contains an itme_list (i.e. a list that contains a ref to each item), these items are
// an item_table, when a ListTable is added, within it's create statement it will then create the item_list,
// which will then create the item_tables - therefore the user needs only pass the 'ListData' reference into
// the initial ListTable constructor.
// ----------------------------------------------------------------------------------------------------------------


public class ListTable {
        public static final String TABLE_NAME = "list";
        public static final String KEY_LIST_ID = "list_id";
        public static final String LIST_NAME = "name";
        //Key value pair for item and unique key to the item table
        public static final String ITEM_TABLE_ID = "item_table_id";
        public static final String CATEGORIES = "categories";
        //For accessibility outside of this class

        //Creates a List Table
        public static final String CREATE_STATEMENT =
                "CREATE TABLE " + TABLE_NAME +  " (" + KEY_LIST_ID + " integer primary key autoincrement, "
                + LIST_NAME + " string not null, " + ITEM_TABLE_ID + " int, " + CATEGORIES + " string not null "
                        + ");";

        public static long add_new_list(SQLiteDatabase db, ListData ls, long table_id){
                ContentValues values = new ContentValues();
                values.put(LIST_NAME, ls.getListName());
                values.put(ITEM_TABLE_ID, table_id);
                values.put(CATEGORIES, ls.getCategories());
                return db.insert(TABLE_NAME, null, values);
        }

//        public static void add_items_existing_list(SQLiteDatabase db, ListData ls){
//                ItemListTable.add_items_to_existing_listTable(db, ls);
//
//        }
//
//        public static void add_items_to_list(SQLiteDatabase db, ListData ls){
//                ItemListTable.add_items_to_table_list(db, ls);
//
//        }
//
        public static long get_table_list_id(SQLiteDatabase db, ListData ls){
                String rawQuery = "SELECT " + ITEM_TABLE_ID  + " FROM " + TABLE_NAME + " WHERE " + KEY_LIST_ID + " = " + ls.getList_id();
                Cursor c = db.rawQuery(rawQuery, null);
                long id =  (c.getInt(c.getColumnIndex(ITEM_TABLE_ID)));
                return id;
        }
//
//        public static boolean list_exists(SQLiteDatabase db, ListData l){
//                String rawQuery = "SELECT * "  + " FROM " + TABLE_NAME;
//                Cursor c;
//                try {
//                        c = db.rawQuery(rawQuery, null);
//                }
//                catch(Exception ex){
//                        //no list exists
//                        return false;
//                }
//                if(c!= null) {
//                        if (c.moveToFirst()) {
//                                while (!c.isAfterLast()) {
//                                        String name =  (c.getString(c.getColumnIndex(LIST_NAME)));
//                                        if(name.equals(l.getListName())){
//                                                return true;
//                                        }
//                                        c.moveToNext();
//                                }
//                        }
//                }
//                return false;
//        }
//
//        public static int get_list_table_id(SQLiteDatabase db, ListData l){
//                String rawQuery = "SELECT * "  + " FROM " + TABLE_NAME;
//                Cursor c;
//                c = db.rawQuery(rawQuery, null);
//
//                if(c!= null) {
//                        if (c.moveToFirst()) {
//                                while (!c.isAfterLast()) {
//                                        String name =  (c.getString(c.getColumnIndex(LIST_NAME)));
//                                        if(name.equals(l.getListName())){
//                                                return c.getInt(c.getColumnIndex(ITEM_TABLE_ID));
//                                        }
//                                        c.moveToNext();
//                                }
//                        }
//                }
//                //No id found
//                return -1;
//        }
//
//        //Return lists without items
//        public static List<ListData> getLists(SQLiteDatabase db){
//                String rawQuery = "SELECT * FROM " + TABLE_NAME;
//                Cursor c;
//
//                try {
//                        c = db.rawQuery(rawQuery, null);
//                }
//                catch(Exception ex){
//                        //No lists
//                        return null;
//                }
//
//                List<ListData> lsData = new ArrayList<ListData>();
//                int nameCount = 0;
//                if (c.moveToFirst()) {
//                        while (!c.isAfterLast()) {
//                                String name =  (c.getString(c.getColumnIndex(LIST_NAME)));
//                                int list_id =  (c.getInt(c.getColumnIndex(KEY_LIST_ID)));
//                                int table_id =  (c.getInt(c.getColumnIndex(ITEM_TABLE_ID)));
//                                String categories = (c.getString(c.getColumnIndex(CATEGORIES)));
//
//                                //Create list without items
//                                List<Item> items = new ArrayList<Item>();
//                                ListData newList = new ListData(name, items , categories, list_id, table_id);
//                                //Use that list to get items tied to it's unique id
//                                items = ItemListTable.getItems(db, newList);
//                                //Add items
//                                newList.addNewItemList(items);
//                                //Add to set of lists
//                                lsData.add(newList);
//                                c.moveToNext();
//                        }
//                }
//
//                return lsData;
//        }
//
//
//
//        public static void insert(SQLiteDatabase db, ListData l) {
//                ContentValues values = new ContentValues();
//                if(!list_exists(db, l)) {
//
//                        //Get next unique id available (we will add them increment them ourselves)
//                        int list_unique_id = get_next_id_unique(db, TABLE_NAME, KEY_LIST_ID);
//
//                        l.setList_id(list_unique_id); // now will have the id to pass on to next table
//                        values.put(LIST_NAME, l.getListName());
//                        values.put(CATEGORIES, l.getCategories());
//                        int uniq_key = ItemListTable.create_item_list_table(db, l);
//                        values.put(ITEM_TABLE_ID, uniq_key);
//                }
//                else{
//                        //if we have selected  a pre-exsiting list this should be present in the ListData
//                        int existing_id = l.getList_id();
//                        Log.d("Insert list", "List exists - updating item_table id:" + existing_id);
//                        ItemListTable.add_items_to_existing_listTable(db, l);
//
//                        //nothing needs ot be added, except categories seperately
//                        return;
//                }
//
////                //By this stage the other tables should have been added, with their unique id's passed back up the heirarchy
////                long rowInserted = db.insert(TABLE_NAME, null, values);
////                if (rowInserted != -1)
////                        Log.d("New row added, row id ", Long.toString(rowInserted));
////                else
////                        Log.d("Something wrong ", Long.toString(rowInserted));
////
////                Log.d("ListTable", " - Printing tables");
////                //print_tables(db, l, TABLE_NAME);
//
//                db.insert(TABLE_NAME, null, values);
//
//        }
//
//
//        public static int get_next_id_unique(SQLiteDatabase db, String tableName, String table_id_name) {
//                String rawQuery = "SELECT " + table_id_name + " FROM " + tableName;
//
//                Cursor c;
//                try {
//                        c = db.rawQuery(rawQuery, null);
//                }
//                catch(Exception ex) {
//                        Log.d("get_unique_id: ", "Table does not exist - therefore id = -1");
//                        return -1;
//                }
//
//
//
//                int largest_id = 1;
//                if(c!= null) {
//                        if (c.moveToFirst()) {
//                                while (!c.isAfterLast()) {
//                                        int id =  (c.getInt(c.getColumnIndex(table_id_name)));
//                                        if(id >= largest_id){
//                                                largest_id = id;
//                                        }
//                                        c.moveToNext();
//                                }
//                        }
//                }
//                Log.d("get_next_id_unique", "largest id found: " + Integer.toString(largest_id));
//                //increment to next uniq id
//                return largest_id + 1;
//        }
//
//




}


