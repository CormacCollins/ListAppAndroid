package cormaccollins.utas.edu.au.CormacCollins.assignment2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity {

    private String listName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        setListTitle();


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
