package cormaccollins.utas.edu.au.CormacCollins.assignment2;

public class Item {
    private String name;
    private String tag;
    private int unique_id;  //may be assigned by sql lite in future?

    public Item(String itmName, String itmTag, int id){
        name = itmName;
        tag = itmTag;
        unique_id = id;
    }

}
