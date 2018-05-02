package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ListData  {
    private String name;
    private List<Item> items = new ArrayList();

    public ListData(String listName){
        name = listName;
    }

    public void addItem(Item item){
        items.add(item);
    }

}
