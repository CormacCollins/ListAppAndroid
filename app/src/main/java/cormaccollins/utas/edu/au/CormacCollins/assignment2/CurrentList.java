package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import java.util.ArrayList;
import java.util.List;

public  class CurrentList {
    public static ListData list;
    public static List<Item> itemsToAdd = new ArrayList<>();
    public static boolean hasList;
    public static void addList(ListData ls){
        list = ls;
        hasList = true;
    }

    public static void removeList(){
        list = null;
        hasList = false;
    }

    public static boolean hasNewItems(){
        return itemsToAdd.size() > 0;
    }

    public static void addItem(Item i){
        itemsToAdd.add(i);
    }

    public static List<Item> getNewItems(){
        return itemsToAdd;
    }

}
