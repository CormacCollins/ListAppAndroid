package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Setting_activity extends AppCompatActivity {

    public enum SearchType{Name, Category}
    private SearchType srchType = SearchType.Name;
    Setting_activity callbackRef = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        setPreviousSearchParam();

        final SearchView srchView = findViewById(R.id.searchBarView);
        srchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Item> items = queryForItems(srchType ,query);
                callbackRef.resfreshItemList(items, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // do something when text changes
                return false;
            }
        });

        final Button bckButton = findViewById(R.id.backButtonSettings);
        bckButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //no data saved - just pressing back
                Intent i = new Intent(view.getContext(), MainActivity.class);
                startActivity(i);
                CurrentList.removeList();
            }
        });

        final Spinner settingsSpinner = findViewById(R.id.settingsSpinnerCategory);
        //may have prviously been searching another category and the page was refreshed
        //we can set the selection to our previous search Type which has been retained


        settingsSpinner.setSelection(
                getSpinnerIndex(settingsSpinner, srchType.toString()));


        settingsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView viewText = (TextView)view;
                String sType = viewText.getText().toString();
                if(sType.equals("Name")){
                    srchType = SearchType.Name;
                }
                else if(sType.equals("Category")){
                    srchType = SearchType.Category;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //either set a category heading or none
        final TextView searchParam = findViewById(R.id.searchParamTextViewSettings);
        if(srchType == SearchType.Name) {
            searchParam.setText("");
            searchParam.setHeight(0);
        }
        else if(srchType == SearchType.Category){
            Intent intent = getIntent();
            //get previously searched category
            String category = intent.getStringExtra("Query");
            searchParam.setText(category);
        }


        //empty list or list of items depending on the refresh
        List<Item> items;
        if(CurrentList.hasList){
            items = CurrentList.list.getItems();
        } else{
            ListDatabase listdb = new ListDatabase(this);
            SQLiteDatabase db = listdb.open();
            items = PublicDBAccess.getAllItems(db);
        }

        //item list specific to search
        final ItemSearchArrayAdaptor myListAdapter = new ItemSearchArrayAdaptor( getApplicationContext(), R.layout.list_item_in_search_layout, items);

        //Adapter attached each item in 'items' to the listView in the list_layout - showing all the items
        ListView myList = findViewById(R.id.settings_item_list_view);
        myList.setAdapter(myListAdapter);


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

//                Intent i = new Intent(view.getContext(), ListActivity.class);
//                i.putExtra("ListName", s);
//                startActivity(i);
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

    public List<Item> queryForItems(SearchType srchType, String srchQuery){
        if(srchType == SearchType.Name){
            ListDatabase databaseConnection = new ListDatabase(this);
            final SQLiteDatabase db = databaseConnection.open();
            List<Item> items = PublicDBAccess.getItemsByName(db, srchQuery);
            if(items == null){
                Log.d("queryForItems", "No items exist");
                return new ArrayList<>();
            }

            return items;
        }
        else if(srchType == SearchType.Category){
            //search by category
            ListDatabase databaseConnection = new ListDatabase(this);
            final SQLiteDatabase db = databaseConnection.open();
            List<Item> items = PublicDBAccess.getItemsByCategory(db, srchQuery);
            return items;
        }

        return new ArrayList<>();
    }

    public void resfreshItemList(List<Item> items, String query){
        CurrentList.addList(new ListData("", items));
        //reset page
        finish();
        Intent i = getIntent();
        String searchParam = srchType.toString();
        i.putExtra("Category", searchParam);
        i.putExtra("Query", query);
        startActivity(i);
    }

    public void setPreviousSearchParam(){
        Intent i = getIntent();
        String s = "";

        //incase there is no extra
        try {
           s = i.getExtras().getString("Category");
        }
        catch(Exception ex){
            return;
        }

        if(s.equals("Category")){
            srchType = SearchType.Category;
        }
        else if(s.equals("Name")){
            srchType = SearchType.Name;
        }
    }

    //https://stackoverflow.com/questions/8769368/how-to-set-position-in-spinner
    private int getSpinnerIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
}


