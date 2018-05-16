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
        public static final String ITEM_LIST_CREATE_STATEMENT = ItemListTable.CREATE_STATEMENT_ITEM_LIST;
        public static final String ITEM_CREATE_STATEMENT = ItemTable.CREATE_STATEMENT_ITEM;

        //Creates a List Table
        public static final String CREATE_STATEMENT =
                "CREATE TABLE " + TABLE_NAME +  " (" + KEY_LIST_ID + " integer primary key autoincrement, "
                + LIST_NAME + " string not null, " + ITEM_TABLE_ID + " int not null, " + CATEGORIES + " string not null "
                        + ");";

        public static void insert(SQLiteDatabase db, ListData l) {

                ContentValues values = new ContentValues();

                //Add to values here ------------
                //list id is set in db - have the property in our class so we can add it when we retrieve..
                //the information during a user session within an activity
                values.put(LIST_NAME, l.getListName());

                //creates an 'item_list' and gets back it's id
                //This will also cascade, creating the items inside the itemlist (in the db)
                //int item_list_unique_id = ItemListTable.create_item_list_table(db, l);
                int item_list_unique_id = 1;
                values.put(ITEM_TABLE_ID, item_list_unique_id);
                //WHen retrieving this it is just a giant string comma separated, remember :)
                values.put(CATEGORIES, l.getCategories());


                //By this stage the other tables should have been added, with their unique id's passed back up the heirarchy
                long rowInserted = db.insert(TABLE_NAME, null, values);
                if(rowInserted != -1)
                        Log.d("New row added, row id ", Long.toString(rowInserted));
                else
                        Log.d("Something wrong ", Long.toString(rowInserted));

                Log.d("ListTable", " - Printing tables");
                print_tables(db, l);

        }

        public static void print_tables(SQLiteDatabase db, ListData l) {
                ArrayList<String> arrTblNames = new ArrayList<String>();
                String tblName = l.getListName();
                Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);

                if(c!= null) {
                        arrTblNames.add(c.getColumnNames()[0]);
                        if (c.moveToFirst()) {
                                while (!c.isAfterLast()) {
                                        arrTblNames.add(c.getString(c.getColumnIndex(LIST_NAME)));
                                        arrTblNames.add(c.getString(c.getColumnIndex(KEY_LIST_ID)));
                                        c.moveToNext();
                                }
                        }
                }
                for (String s : arrTblNames
                     ) {
                        Log.d("TableName ", s);

                }
        }





        //Usable only by the list table!
        private static class ItemListTable {
                private static final String ITEM_TABLE_NAME = "item_list_table";
                private static final String ITEM_LIST_TABLE_ID = "item_list_table_id";
                private static final String LIST_ID = "list_id"; //will be the primary key of the list it belongs to
                //Key value pair for item and unique key to the item table
                private static final String ITEM_ID = "item_id";
                private static final String ITEM_COUNT = "item_count";

                //Creates a List Table
                private static final String CREATE_STATEMENT_ITEM_LIST =
                        "CREATE TABLE " + ITEM_TABLE_NAME +  "(" + ITEM_LIST_TABLE_ID + " integer primary key autoincrement, "
                                + LIST_ID + " int not null," + ITEM_ID + " int not null" + ITEM_COUNT + " int not null" + ");";

                public static int create_item_list_table(SQLiteDatabase db, ListData lstData) {

                        ContentValues values = new ContentValues();
                        String lstName = lstData.getListName();
                        List<Item> items = lstData.getItems();

                        //Creating all the items needed (as ItemTable) in the sql lite
                        //then adding those id's to the item list
                        for (Item i : items ) {
                                //this creates the item before added to the list - obviously needed
                                //We get back the unique id of the item too
                                int unique_id = ItemTable.create_item(db, i);
                                values.put(LIST_ID, lstName);
                                values.put(ITEM_ID, unique_id);
                                //need to refactor for count
                                values.put(ITEM_COUNT, 1);

                        }

                        db.insert(ITEM_TABLE_NAME, null, values);
                        return getItemListPrimaryKey(db);

                }

                //Get the unique id
                private static int getItemListPrimaryKey(SQLiteDatabase db){
//                        String select = "SELECT " + ITEM_LIST_TABLE_ID + " FROM " + ITEM_TABLE_NAME;
//                        Cursor c = db.rawQuery(select, null);

                        Cursor c= db.rawQuery("SELECT last_insert_rowid()", null);

                        if (c == null || c.isAfterLast() || c.isBeforeFirst()) {
                                return -1;
                        }
                        else{
                                return c.getInt(c.getColumnIndex(ITEM_ID));
                        }
                }

        }


        public static class ItemTable {
                private static final String ITEM_TABLE = "item_table";
                private static final String ITEM_TABLE_ID = "item_table_id";
                private static final String ITEM_ID = "item_id";

                private static final String ITEM_NAME = "item_name";
                private static final String TAG = "tag";
                private static final String PRICE = "price";


                //Creates a List Table
                private static final String CREATE_STATEMENT_ITEM =
                        "CREATE TABLE " + ITEM_TABLE +  "(" + ITEM_TABLE_ID + " int not null,"
                                + ITEM_ID + " int not null," + ITEM_NAME + " string not null,"
                                + TAG + " string not null," + PRICE + " int not null," + ");";

                public static int create_item(SQLiteDatabase db, Item item) {
                        ContentValues values = new ContentValues();
                        values.put(ITEM_NAME, item.getItemName());
                        values.put(TAG, item.getItemTag());
                        values.put(PRICE, item.getItemPrice());
                        db.insert(ITEM_TABLE, null, values);



                        return getItemPrimaryKey(db);
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


