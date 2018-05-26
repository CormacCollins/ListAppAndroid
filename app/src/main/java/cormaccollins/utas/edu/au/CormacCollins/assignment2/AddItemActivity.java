package cormaccollins.utas.edu.au.CormacCollins.assignment2;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


public class AddItemActivity extends AppCompatActivity {

    private String listName = "";

    @SuppressLint("SetTextI18n")
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
        TextView name = findViewById(R.id.nameEdit);

        //Stop empty string errors
        int price = 0;
        String txtPrice = e1.getText().toString();
        int count = 0;
        String txtCount = e2.getText().toString();
        String itemName = name.getText().toString();

        if(txtPrice.equals("") || txtCount.equals("") || itemName.equals("")){
            noItemAddedAlert();
            return;
        }

        price = Integer.parseInt(txtPrice);
        count = Integer.parseInt(txtCount);
        int sum = count * price;
        t1.setText(Integer.toString(sum));



        EditText itemEntry = findViewById(R.id.AmountEdit);

        //todo page needs tag adding
        //add new item to code storage
        Item itm = new Item(itemName, "", price);
        for(int i = 1; i < count; i++){
            itm.incrementCount();
        }

        //not adding duplicate items
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

    public void noItemAddedAlert(){
        AlertDialog alertDialog = new AlertDialog.Builder(AddItemActivity.this).create();
        alertDialog.setTitle("");
        alertDialog.setMessage("Please fill in all fields");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


}