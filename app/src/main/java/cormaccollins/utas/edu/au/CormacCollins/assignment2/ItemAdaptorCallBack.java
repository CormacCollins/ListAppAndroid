package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.content.Context;

public interface ItemAdaptorCallBack {
    void itemHeld(long itemId, String itemName);
    void checkItemUpdate(Item it);
}
