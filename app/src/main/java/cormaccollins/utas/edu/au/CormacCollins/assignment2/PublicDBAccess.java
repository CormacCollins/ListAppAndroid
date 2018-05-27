package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.content.ClipData;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
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
        int editCount = 0;
        for(Item i : items) {
            //add items to db
            if(!(ItemTable.itemExists(db, i.getItemName()))) {
                long item_id = addItem(db, i, list_id);
                i.set_id(item_id);
            }
            else{
                if(i.hasBeenEdited()) {
                    //use the itm properties the item has stored to update it
                    i.setDescription(i.copyOfEditItemProperties.getDescription());
                    i.setCategories(i.copyOfEditItemProperties.getCategories());
                    i.setPrice(i.copyOfEditItemProperties.getPrice());
                    i.setName(i.copyOfEditItemProperties.getItemName());
                    i.setCount(i.copyOfEditItemProperties.getCount());
                    editCount += ItemTable.editItem(db, i);
                    i.setHasBeenEdited(false);
                }
            }
        }

        Log.d("add item to list", "edited " + editCount + " lists");

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

    public static List<Item> getItemsByName(SQLiteDatabase db, String srchString){
        return ItemTable.getItemsByName(db, srchString);
    }

    public static List<Item> getItemsByCategory(SQLiteDatabase db, String srchString){
        return ItemTable.getItemsByCategory(db, srchString);
    }

    public static List<Item> getAllItems(SQLiteDatabase db){
        List<Item> items = new ArrayList<>();
        List<ListData> lists =  PublicDBAccess.getAllLists(db);
        for(ListData ls : lists){
            for(Item it : ls.getItems()){
                items.add(it);
            }
        }
        return items;
    }


    // ----------------------------------------------------------------
    // -------------- DELETE -----------------------------------------
    // -----------------------------------------------------------------
    public static void deleteItem(SQLiteDatabase db, Item i){
        ItemTable.deleteItem(db, i.getItemId());
    }

    public static void deleteList(SQLiteDatabase db, long list_id){
        int res = ListTable.deleteList(db, list_id);
        if(res < 0){
            Log.d("delete list", "Could not delete list id " + list_id);
        }
    }


    // ----------------------------------------------------------------
    // ---------------- UPDATE ----------------------------------------
    // ----------------------------------------------------------------
    public static void checkOffItem(SQLiteDatabase db, Item i){
        int checked = ItemTable.toggleItemCheck(db, i);
        if(checked < 1){
            Log.d("checkOffItem", "item " + i.getItemName() + " was not checked in db");

        }
        else{
            Log.d("checkOffItem", "item " + i.getItemName() + " checked in db");
        }
    }

    public static void editItem(SQLiteDatabase db, Item i, long list_id){
        if(ItemTable.itemExists(db, i.getItemName())){
            int rowsEdited = ItemTable.editItem(db, i);
            Log.d("EditItem", "edited " + rowsEdited + " items");
        }
    }
}
