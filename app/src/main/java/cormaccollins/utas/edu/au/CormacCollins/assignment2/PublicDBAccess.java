package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.content.ClipData;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

public class PublicDBAccess {


    //----------------------------------------------------
    // ---------- INSERT STATEMENTS----------------------
    // --------------------------------------------------

    //returns the primary key for the item
    private static long addItem(SQLiteDatabase db, Item i){
        return ItemTable.addItem(db, i);
    }


    public static void addNewList(SQLiteDatabase db, ListData ls){
        int count = ls.getItems().size();
        long[] ids = new long[count]; count = 0;
        for(Item i : ls.getItems()){
            //add items to db and retain their unique id's
            ids[count++] = addItem(db, i);
        }

        long item_table_id = addToNewItemTable(db, ids);
        ls.set_table_id(item_table_id);
        long list_id = ListTable.add_new_list(db, ls, item_table_id);
        ls.setList_id(list_id);
    }

    //updates list by adding items to item_table
    public static void addItemsToExistingList(SQLiteDatabase db, long list_id, List<Item> items){
        long item_table_id = ItemListTable.get_table_list_id(db, list_id);

        if(item_table_id == -1){
            Log.d("addItemsToExistingList", "Could not find item_table_id");
            return;
        }

        for(Item i : items) {
            //add items to db
            long item_id = addItem(db, i);
            //add reference to items into item table - if id -1 then it is a new item
            if(i.getItemId() == -1) {
                addItemToExistingItemTable(db, item_id, item_table_id);
            }
        }

    }

    private static void addItemToExistingItemTable (SQLiteDatabase db, long item_id, long item_table_id){
        ItemListTable.add_items_to_existing_listTable(db, item_id, item_table_id);
    }

    public static boolean list_exists(SQLiteDatabase db, ListData ls){
        return ListTable.list_exists(db, ls);
    }

    //returns unique id
    private static long addToNewItemTable(SQLiteDatabase db, long[] ids){
        return ItemListTable.add_to_new_item_list_table(db, ids);
    }



    // -----------------------------------------------------------------
    // -------------- SELECT QUERIES -----------------------------------
    // -----------------------------------------------------------------
    public static List<ListData> getAllLists(SQLiteDatabase db){
        List<ListData> lists = ListTable.get_lists(db);
        //need to get all their items
        for(ListData l : lists){
            //get items and add to the list object
            List<Item> items = ItemTable.getItemsFromItemTable(db, l.getItem_table_id());
            for(Item i : items){
                l.addItem(i);
            }
        }

        return lists;
    }
}
