package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.util.Log;

public class Item {
    private String name;
    private String tag;
    private float price;
    private long unique_id = -1;  //may be assigned by sql lite in future?
    private int count = 0;
    private boolean isCheckedInList = false;

    public Item(String itmName, String itmTag){
        name = itmName;
        tag = itmTag;
    }

    public Item(String itmName, String itmTag, float price_){
        name = itmName;
        tag = itmTag;
        price = price_;
        count = 1;
    }

    public Item(String itmName, String itmTag, double price_){
        name = itmName;
        tag = itmTag;
        price = (float)price_;
        count = 1;
    }

    public Item(String itmName, String itmTag, int price_){
        name = itmName;
        tag = itmTag;
        price = price_;
        count = 1;
    }

    public void incrementCount(){
        count++;
        price = (float)count*price;
    }

    public int getCount(){
        return count;
    }

    public void set_id(long id){
        if(id != -1){
            unique_id = id;
        }
        else{
            Log.d("Item setid", "Cannot add id - already exists");
        }

    }

    public String getItemName(){
        return name;
    }

    public float getItemPrice(){
        return price;
    }

    public String getItemTag(){
        return tag;
    }

    public long getItemId(){
        return unique_id;
    }

    public void toggleChecked(){
        if(isCheckedInList){
            isCheckedInList = false;
        }
        else{
            isCheckedInList = true;
        }
    }

    public boolean isChecked(){
        return isCheckedInList;
    }

}
