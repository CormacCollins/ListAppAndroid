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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private String listName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        setListTitle();

        ArrayList<Item> items = new ArrayList<Item>();
        Item item1 = new Item("New Item", "Fruit");
        Item item2 = new Item("apple", "Fruit");
        items.add(item1);
        items.add(item2);


        final ItemArrayAdapter myListAdapter = new ItemArrayAdapter( getApplicationContext(), R.layout.list_item_layout, items);

        //Adapter attached each item in 'items' to the listView in the list_layout - showing all the items
        ListView myList = findViewById(R.id.item_list_view);
        myList.setAdapter(myListAdapter);



        ListDatabase databaseConnection = new ListDatabase(this);
        final SQLiteDatabase db = databaseConnection.open();
        ListData ls = new ListData("cormac_list", items);
        ListTable.insert(db, ls);



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



}
