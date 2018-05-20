package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ListData  {
    private String name;
    private Integer list_id = null;
    private Integer item_table_id = null;
    private ArrayList<Item> items;
    //We will just have comma seperates names - varchar in sql lite is 500 in size,
    // we should not go over this and we can prevent any greater
    private String categories;

    public ListData(String listName, ArrayList<Item> itemList){
        name = listName;
        items = itemList;
        categories = ""; //empty if not used
        list_id = -1;
        item_table_id = -1;
    }

    public ListData(String listName, ArrayList<Item> itemList, String cat, int listID, int tableID){
        name = listName;
        items = itemList;
        categories = ""; //empty if not used
        list_id = listID;
        item_table_id = -tableID;
    }

    public void addItem(Item item){
        items.add(item);
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

    public int getList_id(){
        return list_id;
    }

    //will only set the id if there is none already
    //Stops user from changing a unique db id
    public void setList_id(Integer id){
        if(list_id == null){
            list_id = id;
        }
    }


    public int getItem_table_id(){
        return item_table_id;
    }

    //Same as list id pattern
    public void setItem_table_id(Integer id){
        if(item_table_id == null){
            item_table_id = id;
        }
    }

}
