package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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
                "CREATE TABLE " + TABLE_NAME +  " (" + KEY_LIST_ID + " int primary key, "
                + LIST_NAME + " string not null, " + ITEM_TABLE_ID + " int, " + CATEGORIES + " string not null "
                        + ");";

        public static boolean list_exists(SQLiteDatabase db, ListData l){
                String rawQuery = "SELECT * "  + " FROM " + TABLE_NAME;
                Cursor c;
                try {
                        c = db.rawQuery(rawQuery, null);
                }
                catch(Exception ex){
                        //no list exists
                        return false;
                }
                if(c!= null) {
                        if (c.moveToFirst()) {
                                while (!c.isAfterLast()) {
                                        String name =  (c.getString(c.getColumnIndex(LIST_NAME)));
                                        if(name.equals(l.getListName())){
                                                return true;
                                        }
                                        c.moveToNext();
                                }
                        }
                }
                return false;
        }

        public static int get_list_table_id(SQLiteDatabase db, ListData l){
                String rawQuery = "SELECT * "  + " FROM " + TABLE_NAME;
                Cursor c;
                c = db.rawQuery(rawQuery, null);

                if(c!= null) {
                        if (c.moveToFirst()) {
                                while (!c.isAfterLast()) {
                                        String name =  (c.getString(c.getColumnIndex(LIST_NAME)));
                                        if(name.equals(l.getListName())){
                                                return c.getInt(c.getColumnIndex(ITEM_TABLE_ID));
                                        }
                                        c.moveToNext();
                                }
                        }
                }
                //No id found
                return -1;
        }

        public static String[] getListNames(SQLiteDatabase db){
                String rawQuery = "SELECT " + LIST_NAME  + " FROM " + TABLE_NAME;
                Cursor c;

                try {
                        c = db.rawQuery(rawQuery, null);
                }
                catch(Exception ex){
                        //No lists
                        return new String[0];
                }

                String[] names = new String[c.getCount()];
                int nameCount = 0;
                if (c.moveToFirst()) {
                        while (!c.isAfterLast()) {
                                String name =  (c.getString(c.getColumnIndex(LIST_NAME)));
                                names[nameCount] = name;
                                c.moveToNext();
                        }
                }

                return names;
        }

        public static void insert(SQLiteDatabase db, ListData l) {

                //print_tables_in_db(db);

                ContentValues values = new ContentValues();

                if(!list_exists(db, l)) {
                        //Get next unique id available (we will add them increment them ourselves)
                        int list_unique_id = get_next_id_unique(db, TABLE_NAME, KEY_LIST_ID);
                        l.setList_id(list_unique_id); // now will have the id to pass on to next table

                        //Add to values here ------------
                        //list id is set in db - have the property in our class so we can add it when we retrieve..
                        //the information during a user session within an activity
                        values.put(LIST_NAME, l.getListName());

                        //creates an 'item_list' and gets back it's id
                        //This will also cascade, creating the items inside the itemlist (in the db)
                        // int item_list_unique_id = ItemListTable.create_item_list_table(db, l);
                        //int item_list_unique_id = 1;
                        //values.put(ITEM_TABLE_ID, item_list_unique_id);
                        //WHen retrieving this it is just a giant string comma separated, remember :)
                        values.put(CATEGORIES, l.getCategories());

                        int uniq_key = ItemListTable.create_item_list_table(db, l);
                        values.put(ITEM_TABLE_ID, uniq_key);
                }
                else{
                        int existing_id = get_list_table_id(db, l);

                        Log.d("Insert list", "List exists - updating item_table id:" + existing_id);
                        int uniq_key = ItemListTable.add_items_to_existing_listTable(db, l, existing_id);
                        values.put(ITEM_TABLE_ID, uniq_key);

                        return;
                }

//                //By this stage the other tables should have been added, with their unique id's passed back up the heirarchy
//                long rowInserted = db.insert(TABLE_NAME, null, values);
//                if (rowInserted != -1)
//                        Log.d("New row added, row id ", Long.toString(rowInserted));
//                else
//                        Log.d("Something wrong ", Long.toString(rowInserted));
//
//                Log.d("ListTable", " - Printing tables");
//                //print_tables(db, l, TABLE_NAME);

                db.insert(TABLE_NAME, null, values);

        }


        public static int get_next_id_unique(SQLiteDatabase db, String tableName, String table_id_name) {
                String rawQuery = "SELECT " + table_id_name + " FROM " + tableName;

                Cursor c;
                try {
                        c = db.rawQuery(rawQuery, null);
                }
                catch(Exception ex) {
                        Log.d("get_unique_id: ", "Table does not exist - therefore id = 1");
                        return 1;
                }



                int largest_id = 1;
                if(c!= null) {
                        if (c.moveToFirst()) {
                                while (!c.isAfterLast()) {
                                        int id =  (c.getInt(c.getColumnIndex(table_id_name)));
                                        if(id >= largest_id){
                                                largest_id = id;
                                        }
                                        c.moveToNext();
                                }
                        }
                }
                Log.d("get_next_id_unique", "largest id found: " + Integer.toString(largest_id));
                //increment to next uniq id
                return largest_id + 1;
        }





        //Usable only by the list table!
        public static class ItemListTable {
                public static final String ITEM_TABLE_NAME = "item_list_table";
                private static final String ITEM_LIST_TABLE_ID = "item_list_table_id";
                private static final String LIST_ID = "list_id"; //will be the primary key of the list it belongs to
                //Key value pair for item and unique key to the item table
                private static final String ITEM_ID = "item_id";
                private static final String ITEM_COUNT = "item_count";

                public static final String CREATE_STATEMENT_ITEM_LIST =
                        "CREATE TABLE " + ITEM_TABLE_NAME +  " (" + ITEM_LIST_TABLE_ID + " int primary key, "
                                + LIST_ID + " int not null, " + ITEM_ID + " int not null, " + ITEM_COUNT + " int not null " + ");";

                public static int add_items_to_existing_listTable(SQLiteDatabase db, ListData lstData, int listTableID){
                        //Need ot add the items to the item_table
                        ContentValues values = new ContentValues();
                        String lstName = lstData.getListName();
                        List<Item> items = lstData.getItems();

                        //print_tables(db, lstData, ITEM_TABLE_NAME);

                        int unique_item_table_id = listTableID;
                        int unique_id_item = get_next_id_unique(db, ItemTable.ITEM_TABLE, ItemTable.ITEM_ID);

                        //Creating all the items needed (as ItemTable) in the sql lite
                        //then adding those id's to the item list
                        for (Item i : items ) {
                                //this creates the item before added to the list - obviously needed
                                //We get back the unique id of the item too
                                ItemTable.create_item(db, i, unique_item_table_id, unique_id_item);

                                //values.put(ITEM_LIST_TABLE_ID, unique_item_table_id);
                                //values.put(LIST_ID, lstName);
                                values.put(ITEM_ID, unique_id_item);
                                unique_id_item++;
                                //need to refactor for count

                                //TODO query the amount of an item in db to change the count here
                                values.put(ITEM_COUNT, 1);

                        }

                        db.update(ITEM_TABLE_NAME, values,
                                ITEM_LIST_TABLE_ID + " = " + unique_item_table_id, null);

                        return unique_item_table_id;

                }

                public static int create_item_list_table(SQLiteDatabase db, ListData lstData) {

                        ContentValues values = new ContentValues();
                        String lstName = lstData.getListName();
                        List<Item> items = lstData.getItems();

                        //print_tables(db, lstData, ITEM_TABLE_NAME);

                        int unique_item_table_id = get_next_id_unique(db, ITEM_TABLE_NAME, ITEM_LIST_TABLE_ID);
                        int item_id = get_next_id_unique(db, ItemTable.ITEM_TABLE, ItemTable.ITEM_ID);

                        //Creating all the items needed (as ItemTable) in the sql lite
                        //then adding those id's to the item list
                        for (Item i : items ) {
                                //this creates the item before added to the list - obviously needed
                                //We get back the unique id of the item too
                                ItemTable.create_item(db, i, unique_item_table_id, item_id);

                                values.put(ITEM_LIST_TABLE_ID, unique_item_table_id);
                                values.put(LIST_ID, lstName);
                                values.put(ITEM_ID, item_id);
                                item_id++;
                                //TODO query the amount of an item in db to change the count here
                                values.put(ITEM_COUNT, 1);

                        }

                        db.insert(ITEM_TABLE_NAME, null, values);
                        //to be given to the table with the list
                        return unique_item_table_id;

                }

        }


        public static class ItemTable {
                public static final String ITEM_TABLE = "item_table";
                private static final String ITEM_TABLE_ID = "item_table_id";
                private static final String ITEM_ID = "item_id";

                private static final String ITEM_NAME = "item_name";
                private static final String TAG = "tag";
                private static final String PRICE = "price";

                //Creates a List Table
                public static final String CREATE_STATEMENT_ITEM =
                        "CREATE TABLE " + ITEM_TABLE +  " (" + ITEM_ID + " int primary key, "
                                + ITEM_TABLE_ID + " int not null, "
                                + ITEM_NAME + " string not null, "
                                + TAG + " string not null, " + PRICE + " int not null " + ");";

                //create item with option to give an existing table_id
                public static void create_item(SQLiteDatabase db, Item item, int table_id, int item_id) {

                        //print_tables_in_db(db);

                        ContentValues values = new ContentValues();

                        //Need to change
                        int unique_id_itemTable = table_id;
                        values.put(ITEM_TABLE_ID, unique_id_itemTable);
                        values.put(ITEM_ID, item_id);
                        values.put(ITEM_NAME, item.getItemName());
                        values.put(TAG, item.getItemTag());
                        values.put(PRICE, item.getItemPrice());

                        db.insert(ITEM_TABLE, null, values);
                }



                //Get the unique id
                private static int getItemPrimaryKey(SQLiteDatabase db){



                        //String select = "SELECT " + ITEM_ID + " FROM " + ITEM_TABLE;
                        //Cursor c = db.rawQuery(select, null);
                        Cursor c= db.rawQuery("SELECT last_insert_rowid()", null);

                        if (c == null || c.isAfterLast() || c.isBeforeFirst()) {
                                return -1;
                        }
                        else{
                                return c.getInt(c.getColumnIndex(ITEM_ID));
                        }
                }

        }
}


