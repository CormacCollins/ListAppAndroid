package cormaccollins.utas.edu.au.CormacCollins.assignment2;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnEnter = findViewById(R.id.addListButton);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createAlertDialog(view);
            }
        });

        ListDatabase databaseConnection = new ListDatabase(this);
        final SQLiteDatabase db = databaseConnection.open();


//        Item newItem = new Item("Apple", "Fuirt", 1.5);
//        List<Item> items = new ArrayList<>();
//        items.add(newItem);
//        ListData ls = new ListData("new list", items);
//        PublicDBAccess.addNewList(db, ls);
//        db.close();
//
//        final SQLiteDatabase db2 = databaseConnection.open();
//        List<Item> items2 = new ArrayList<>();
//        Item newItem2 = new Item("Apple2", "Fuirt", 1.5);
//        items2.add(newItem2);
//        PublicDBAccess.addItemsToExistingList(db2, ls.getList_id(), items2);




        final ListView listNames = findViewById(R.id.list_view_names);
        if(listNames.getCount() == 0) {
            final List<ListData> lists = PublicDBAccess.getAllLists(db);

            int count = lists.size();
            String[] names = new String[count];

            count = 0;
            for(ListData ls : lists){
                names[count++] = ls.getListName();
            }

            ArrayAdapter<String> myListAdapter =new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, names){
                //Need tochange to set text as black - quick and easy
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    final TextView textView = (TextView) view.findViewById(android.R.id.text1);

                    /*YOUR CHOICE OF COLOR*/
                    textView.setTextColor(Color.BLACK);

                    //If item is cliecked go to listActivity with the selected list
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Log.d("On click list item", "clicked!");
                            Intent i = new Intent(view.getContext(), ListActivity.class);
                            String name = textView.getText().toString();
                            i.putExtra("ListName", name);

                            ListData newList = null;
                            for(ListData l : lists){
                                if(l.getListName() == name){
                                    newList = new ListData(l.getListName(), l.getItems(), l.getCategories(), l.getList_id(), 0);
                                }
                            }

                            //Should be list by that name because the name came form that same original List<i>
                            CurrentList.addList(newList);

                            startActivity(i);
                        }
                    });

                    return view;
                }

            };



            listNames.setAdapter(myListAdapter);
        }

        db.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    //    int id = item.getItemId();

        //noinspection SimplifiableIfStatement
    //    if (id == R.id.action_settings) {
    //        return true;
    //    }

        switch (item.getItemId())
        {
            case R.id.action_settings:
                Intent action_settings = new Intent(this, Setting_activity.class );
                startActivity(action_settings);
                break;
            case R.id.Today:
                break;
            case R.id.This_Week:
                break;
            case R.id.This_Month:
                break;
            case R.id.This_year:
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void openNewList(){

    }

    private void createAlertDialog(final View view){


        //https://stackoverflow.com/questions/10903754/input-text-dialog-android
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("List Name:");

        final EditText input = new EditText(view.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        builder.setPositiveButton("Create List", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = input.getText().toString();

                Intent i = new Intent(view.getContext(), ListActivity.class);
                i.putExtra("ListName", s);
                startActivity(i);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}

