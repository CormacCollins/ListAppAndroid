package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListData  {
    private String name;
    private Integer list_id = null;
    private Integer item_list_id = null;
    private ArrayList<Item> items;
    //We will just have comma seperates names - varchar in sql lite is 500 in size,
    // we should not go over this and we can prevent any greater
    private String categories;

    public ListData(String listName, ArrayList<Item> itemList){
        name = listName;
        items = itemList;
        categories = ""; //empty if not used
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

    public Integer getList_id(){
        return list_id;
    }

    //will only set the id if there is none already
    //Stops user from changing a unique db id
    public void setList_id(Integer id){
        if(list_id == null){
            list_id = id;
        }
    }


    public Integer getItem_list_id(){
        return item_list_id;
    }

    //Same as list id pattern
    public void setItem_list_id(Integer id){
        if(item_list_id == null){
            item_list_id = id;
        }
    }

}
