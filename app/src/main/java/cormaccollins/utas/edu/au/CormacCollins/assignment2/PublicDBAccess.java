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
    private static long addItem(SQLiteDatabase db, Item i, long list_id){
        return ItemTable.addItem(db, i, list_id);
    }


    //create list
    //get the list id and use it to create items with references to that list
    //updates the ListData object with new id
    public static void addNewList(SQLiteDatabase db, ListData ls){

        //make new list
        long list_id = ListTable.add_new_list(db, ls);
        ls.setList_id(list_id);

        int count = ls.getItems().size();
        long[] ids = new long[count]; count = 0;
        for(Item i : ls.getItems()){
            //add items to db and retain their unique id's
            ids[count++] = addItem(db, i, list_id);
        }
    }

    //updates list by adding items to item_table
    public static void addItemsToExistingList(SQLiteDatabase db, long list_id, List<Item> items){
        for(Item i : items) {
            //add items to db
            if(!(ItemTable.itemExists(db, i.getItemName()))) {
                long item_id = addItem(db, i, list_id);
            }
            else{
                Log.d("add item to list", "Could not insert " + i.getItemName() + " - already exists");
            }
        }

    }

    public static boolean list_exists(SQLiteDatabase db, ListData ls){
        return ListTable.list_exists(db, ls);
    }



    // -----------------------------------------------------------------
    // -------------- SELECT QUERIES -----------------------------------
    // -----------------------------------------------------------------
    public static List<ListData> getAllLists(SQLiteDatabase db){
        List<ListData> lists = ListTable.get_lists(db);
        //need to get all their items
        for(ListData l : lists){
            //get items and add to the list object
            List<Item> items = ItemTable.getItemsFromItemList(db, l.getList_id());
            for(Item i : items){
                l.addItem(i);
            }
        }
        return lists;
    }


    // ----------------------------------------------------------------
    // -------------- DELETE -----------------------------------------
    // -----------------------------------------------------------------
    public static boolean deleteItem(SQLiteDatabase db, Item i){
        return ItemTable.deleteItem(db, i.getItemId());
    }

    public static boolean deleteList(SQLiteDatabase db, ListData ls){
        return ListTable.deleteList(db, ls.getList_id());
    }
}
