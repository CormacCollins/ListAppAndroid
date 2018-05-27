package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import java.util.ArrayList;
import java.util.List;

public  class CurrentList {
    public static ListData list;
    public static List<Item> toBeEdited = new ArrayList<>();
    public static boolean hasList;

    public static void addList(ListData ls){
        list = ls;
        hasList = true;
    }

    public static void removeList(){
        list = null;
        hasList = false;
    }

    public static boolean isItemsToEdit(){
        return toBeEdited.size() > 0;
    }

    public static List<Item> itemsToEdit(){
        return toBeEdited;
    }

    public static void toggleItemChecked(Item i){
        for(Item item : list.getItems()){
            if(i.equals(item)){
                item.toggleChecked();
            }
        }
    }

}
