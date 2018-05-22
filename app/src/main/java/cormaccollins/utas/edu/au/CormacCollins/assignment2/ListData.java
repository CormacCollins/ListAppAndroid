package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ListData  {
    private String name;
    private Long list_id = null;
    //private Long item_table_id = null;
    private List<Item> items;
    //We will just have comma seperates names - varchar in sql lite is 500 in size,
    // we should not go over this and we can prevent any greater
    private String categories;

    public ListData(String listName, List<Item> itemList){
        name = listName;
        items = itemList;
        categories = ""; //empty if not used
        list_id = (long)0;
       // item_table_id = (long)0;
    }

    public ListData(String listName, List<Item> itemList, String cat, long listID, long tableID){
        name = listName;
        items = itemList;
        categories = ""; //empty if not used
        list_id = listID;
      //  item_table_id = tableID;
    }

    public ListData(ListData ls, long listID, long tableID){
        name = ls.getListName();
        items = ls.getItems();
        categories = ls.getCategories(); //empty if not used
        list_id = listID;
       // item_table_id = tableID;
    }

    public void addItem(Item item){
            items.add(item);
    }

    public void addNewItemList(List<Item> itemsNew){
        items = itemsNew;
    }

    public String getListName(){
        return name;
    }

    public List<Item> getItems(){
        return items;
    }

    public String getCategories(){
        return categories;
    }

    public void addCategory(String s){
        Integer size = categories.length() + s.length();
        if(size > 500){
            Log.d("ListData", "Cannot add anymore catgories, length is " + size);
        }
        else{
            categories += ", " + s;
        }
    }

    public long getList_id(){
        return list_id;
    }

//    public void set_table_id(long id){
//        if(item_table_id == 0){
//            item_table_id = id;
//        }
//    }

    //will only set the id if there is none already
    //Stops user from changing a unique db id
    public void setList_id(long id){
        if(list_id == 0){
            list_id = id;
        }
    }


//    public long getItem_table_id(){
//        return item_table_id;
//    }
//
//    //Same as list id pattern
//    public void setItem_table_id(long id){
//        if(item_table_id == null){
//            item_table_id = id;
//        }
//    }

}
