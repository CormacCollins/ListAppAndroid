package cormaccollins.utas.edu.au.CormacCollins.assignment2;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements ItemAdaptorCallBack {

    private String listName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        setListTitle();

        ArrayList<Item> items = new ArrayList<Item>();
        List<Item> dbItems;
        //if it doens't it's a new list
        if(CurrentList.hasList) {
            dbItems = CurrentList.list.getItems();
            //add the updates to db
            for(Item i : dbItems){
                items.add(i);
            }
        } else{
            //We are setting the current list since it is a new one - this will be used to store all information until we add it to the db
            //when clicking save
            List<Item> emptyItems = new ArrayList<Item>();
            ListData ls = new ListData(listName, emptyItems);
            CurrentList.addList(ls);
        }

        final ItemArrayAdapter myListAdapter = new ItemArrayAdapter( getApplicationContext(), R.layout.list_item_layout, items);

        //Adapter attached each item in 'items' to the listView in the list_layout - showing all the items
        ListView myList = findViewById(R.id.item_list_view);

        myList.setAdapter(myListAdapter);
        myListAdapter.setCallback(this);


        // ------------------------------------------------
        // ------------- Button Setup ---------------------
        // ------------------------------------------------

        //Event for return from list button
        final Button bckButton = findViewById(R.id.backButton);
        bckButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //no data saved - just pressing back
                    Intent i = new Intent(view.getContext(), MainActivity.class);
                    startActivity(i);
                    CurrentList.removeList();
                }
        });

        //PRESS BUTTON TO ADD LIST/SAVE
        ImageView addListImageView = findViewById(R.id.addListImageView);
        addListImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                view.setEnabled(false);
                Intent i = new Intent(view.getContext(), MainActivity.class);
                //Add list to db now that it is saved
                ListDatabase databaseConnection = new ListDatabase(ListActivity.this);
                final SQLiteDatabase db = databaseConnection.open();
                ListData ls = CurrentList.list;
                //used so we only add the new items

                boolean listExists = PublicDBAccess.list_exists(db, ls);

                try {
                    if (!listExists) {
                        PublicDBAccess.addNewList(db, ls);
                    } else {
                        PublicDBAccess.addItemsToExistingList(db, ls.getList_id(), ls.getItems());
                    }
                }
                catch(Exception ex){

                }
                //remove code storage of list as we are returning to home page (keep no memory of this list in the 'Current List'
                CurrentList.removeList();
                db.close();
                startActivity(i);
            }
        });


        //Event for clicking add item
        Button newItemButton = findViewById(R.id.create_new_item_button);
        newItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                view.setEnabled(false);
                Intent i = new Intent(view.getContext(), AddItemActivity.class);
                i.putExtra("ListName", listName);
                i.putExtra("IsEdit", false);
                startActivity(i);
            }
        });


    }

    private void setListTitle(){
        Intent i = getIntent();
        String s = i.getExtras().getString("ListName");
        TextView txtView = findViewById(R.id.listName);
        txtView.setText(s);
        listName = s;
    }


    public void itemHeld(final long itemId, final String itemName){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("");
        alertDialog.setMessage("Edit/Delete item " + "'" + itemName + "' " + "?"
        + '\n' + "");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "edit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getBaseContext(), AddItemActivity.class);
                        i.putExtra("ItemID", itemId);
                        i.putExtra("IsEdit", true);
                        startActivity(i);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int removeItemIndex = 0;
                        for(Item itm : CurrentList.list.getItems()){
                            //If the item we have clicked on is found in our list
                            if(itemName.equals(itm.getItemName())){

                                //if -1 then it hasn't been assigned therefore its only a new local one
                                //only remove from the local list - not in db yet anyway
                                if(itemId < 0){
                                    break;
                                }
                                else {
                                    //else it's id is >1 and has been added via the db - therefore exists in it and can be deleted
                                    ListDatabase databaseConnection = new ListDatabase(ListActivity.this);
                                    SQLiteDatabase db = databaseConnection.open();
                                    PublicDBAccess.deleteItem(db, itm);
                                    databaseConnection.close();
                                    break;
                                }
                            }
                            removeItemIndex++;
                        }

                        //index inside loop is saved where it broke (above) - removes specific object
                        CurrentList.list.getItems().remove(removeItemIndex);
                        //reset page
                        finish();
                        startActivity(getIntent());
                    }
                });
        alertDialog.show();
    }

    //callback form adaptor when onToggle is fired - to update db
    public void checkItemUpdate(Item i){
        ListDatabase databaseConnection = new ListDatabase(ListActivity.this);
        SQLiteDatabase db = databaseConnection.open();
        PublicDBAccess.checkOffItem(db, i);
        databaseConnection.close();
        setCallBackInProgress(false);
    }


    private boolean callBackInProgress = false;
    public boolean isCallBackInProgress() {return callBackInProgress;}
    public void setCallBackInProgress(Boolean isInProgress){
        callBackInProgress = isInProgress;
    }


}
