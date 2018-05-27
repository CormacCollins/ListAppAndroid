package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.util.Log;

public class Item {
    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private String tag;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    private float price;
    private long unique_id = -1;  //may be assigned by sql lite in future?

    public void setCount(int count) {
        this.count = count;
    }

    private int count = 0;
    private boolean isCheckedInList = false;
    private String description = "";
    private String categories = "";
    private boolean hasBeenEdited = false;
    public Item copyOfEditItemProperties;
    public boolean hasBeenEdited() {
        return hasBeenEdited;
    }

    public void setHasBeenEdited(boolean hasBeenEdited) {
        this.hasBeenEdited = hasBeenEdited;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    private long list_id = -1;

    public long getList_id() {
        return list_id;
    }

    public void setList_id(long list_id) {
        this.list_id = list_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
        float actualPrice = price / count;
        price = (float)actualPrice*count;
    }

    public int getCount(){
        return count;
    }

    public void set_id(long id){
        if(unique_id == -1){
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

    public void clone(Item i){
        name = i.name;
        price = i.getItemPrice();
        count = i.getCount();
        description = i.getDescription();
        categories = i.getCategories();
        unique_id = i.getItemId();
        tag = i.getItemTag();
    }

}
