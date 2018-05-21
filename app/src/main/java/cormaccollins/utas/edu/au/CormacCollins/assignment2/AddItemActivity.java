package cormaccollins.utas.edu.au.CormacCollins.assignment2;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


public class AddItemActivity extends AppCompatActivity {

    private String listName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_layout);

        setListTitle();


        Button bckButton = findViewById(R.id.return_to_shopping_list_button);
        bckButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ListActivity.class);

                i.putExtra("ListName", listName);
                //Code to organise what information we add to next intent
                //Do we want to send back any information if things are fileed out


                startActivity(i);
            }
        });

        //submit button action
        Button submitBtn = findViewById(R.id.SubmitButton);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onSubmitClick(view);
            }
        });


//
//        final String[] string_array = getResources().getStringArray(R.array.add_item_options);
//        ArrayList<String> addItemNames = new ArrayList<>(Arrays.asList(string_array));
//
//        //Adapter = controller, which takes in a 'model' (data source like our array) which will go into our 'View'
//        ArrayAdapter<String> myListAdapter = new ArrayAdapter<String>( getApplicationContext(), R.layout.add_item_layout , addItemNames);
//
//        final ListView optionsList = findViewById(R.id.options_list);
//        optionsList.setAdapter(myListAdapter);
//
//



//
//        Button addItemButton = findViewById(R.id.add_item_button);
//        addItemButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//
//                //Code for add item to database and list etc.
//
//            }
//        });

    }

    private void setListTitle(){
        Intent i = getIntent();
        String s = i.getExtras().getString("ListName");
        listName = s;
    }

    public void onSubmitClick (View v)
    {
        EditText e1 = this.findViewById(R.id.AmountEdit);
        EditText e2 = this.findViewById(R.id.UnitEdit);
        TextView t1 = this.findViewById(R.id.CalculateView);


        int price = Integer.parseInt(e1.getText().toString());
        int count = Integer.parseInt(e2.getText().toString());
        EditText itemEntry = findViewById(R.id.NameEdit);
        String itemName = itemEntry.getText().toString();

        //todo page needs tag adding
       // Item newItem = new Item(itemName, "no tag", price, -1);

        //add new item to code storage
        //CurrentList.list.addItem(newItem);


        Intent i = new Intent(v.getContext(), ListActivity.class);
        i.putExtra("ListName", listName);
        startActivity(i);

    }

}