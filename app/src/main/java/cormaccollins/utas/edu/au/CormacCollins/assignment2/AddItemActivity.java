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


        int price = 0; String txtPrice = e1.getText().toString();
        if(txtPrice.equals("")){
            price = Integer.parseInt(txtPrice);
        }


        //to stop empty string errors
        int count = 0; String txt = e2.getText().toString();
        if(txt.equals("")){
            count = Integer.parseInt(txt);
        }

        EditText itemEntry = findViewById(R.id.NameEdit);
        String itemName = itemEntry.getText().toString();

        //todo page needs tag adding
        //add new item to code storage
        Item itm = new Item(itemName, "", price);
        for(int i = 1; i < count; i++){
            itm.incrementCount();
        }

        boolean alreadyInList = false;
        for(Item i : CurrentList.list.getItems()){

            if(itm.getItemName().equals(i.getItemName())){
                for(int j = 0; j < itm.getCount(); j++){
                    i.incrementCount();
                }
                alreadyInList = true;
            }
        }

        if(!alreadyInList){
            CurrentList.list.addItem(itm);

        }

        Intent i = new Intent(v.getContext(), ListActivity.class);
        i.putExtra("ListName", listName);
        startActivity(i);

    }

}