package cormaccollins.utas.edu.au.CormacCollins.assignment2;

public class Item {
    private String name;
    private String tag;
    private int price;
    private int unique_id;  //may be assigned by sql lite in future?

    public Item(String itmName, String itmTag){
        name = itmName;
        tag = itmTag;
    }

    public Item(String itmName, String itmTag, int price_, int id){
        name = itmName;
        tag = itmTag;
        price = price_;
        unique_id = id;
    }

    public String getItemName(){
        return name;
    }

    public int getItemPrice(){
        return price;
    }

    public String getItemTag(){
        return tag;
    }

    public int getItemId(){
        return unique_id;
    }

}
