package cormaccollins.utas.edu.au.CormacCollins.assignment2;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private String listName = "";
    private boolean hasItems = false;

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



//        ListDatabase databaseConnection = new ListDatabase(this);
//        final SQLiteDatabase db = databaseConnection.open();
//        ListData ls = new ListData("new_list", items);
//        ListTable.insert(db, ls);
//        db.close();


        // --------------------------------------------------
        // When checkbox is tapped - check it
        // --------------------------------------------------
//        CheckBox chkBox = myList. (R.id.itemCheckBox);
//        chkBox.setOnClickListener(
//            new View.OnClickListener() {
//                  @Override
//                  public void onClick(View view) {
//
//                  }
//          });


                // ------------------------------------------------
                // ------------- Button Setup ---------------------
                // ------------------------------------------------

                //Event for return from list button
                Button bckButton = findViewById(R.id.backButton);
        bckButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MainActivity.class);

                //Code to organise what information we add to next intent
                //Do we want to send back any information if things are fileed out


                startActivity(i);
            }
        });

        ImageView addListImageView = findViewById(R.id.addListImageView);
        addListImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MainActivity.class);

//                //Add list to db now that it is saved
//                ListDatabase databaseConnection = new ListDatabase(ListActivity.this);
//                final SQLiteDatabase db = databaseConnection.open();
//                ListData ls = CurrentList.list;
//                CurrentList.removeList();
//                ListTable.insert(db, ls);
//                db.close();

                //Code to organise what information we add to next intent
                //Do we want to send back any information if things are fileed out


                startActivity(i);
            }
        });


        //Event for clicking add item
        Button newItemButton = findViewById(R.id.create_new_item_button);
        newItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AddItemActivity.class);


                i.putExtra("ListName", listName);
                //Code to organise what information we add to next intent
                //Do we want to send back any information if things are fileed out


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

//    // not needed?
//    public List<Item> getItems(ListData ls){
//        ListDatabase databaseConnection = new ListDatabase(this);
//        final SQLiteDatabase db = databaseConnection.open();
//        List<Item> items = ListTable.ItemListTable.getItems(db, ls);
//        db.close();
//        return items;
//    }



}
