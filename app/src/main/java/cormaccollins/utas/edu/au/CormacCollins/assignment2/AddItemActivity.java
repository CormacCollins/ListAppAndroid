package cormaccollins.utas.edu.au.CormacCollins.assignment2;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class AddItemActivity extends AppCompatActivity {

    private String listName = "";
    private int itemQuantity = 0;
    private boolean isPriceEntered = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_layout);


        Intent intent = getIntent();
        setListTitle(intent);

        //Have cursor start at top
        EditText editName = (EditText)findViewById(R.id.nameEdit);
        editName.requestFocus();

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


        final EditText itemPriceEdit = findViewById(R.id.ItemPriceEdit);
        itemPriceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //if we have a quantity change the total price
                if(itemQuantity > 0){
                    String priceText = itemPriceEdit.getText().toString();
                    if(priceText == ""){
                        return;
                    }
                    float priceVal;
                    try {
                        priceVal = Float.parseFloat(priceText);
                    }
                    catch (Exception ex){
                        priceVal = 0;
                    }

                    TextView totalPriceView = findViewById(R.id.CalculateView);

                    float totalPrice = (float) itemQuantity * priceVal;
                    totalPriceView.setText(String.valueOf(totalPrice));
                    isPriceEntered = true;
                }
            }
        });

        final EditText itemCountEdit = findViewById(R.id.ItemNumberEdit);
        itemCountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isPriceEntered){
                    String text = itemCountEdit.getText().toString();
                    if(!text.equals("")){
                        itemQuantity = Integer.parseInt(text);
                    }

                    if(itemQuantity > 0){
                        String priceText = itemPriceEdit.getText().toString();
                        float priceVal = Float.parseFloat(priceText);
                        TextView totalPriceView = findViewById(R.id.CalculateView);

                        float totalPrice = (float) itemQuantity * priceVal;
                        totalPriceView.setText(String.valueOf(totalPrice));
                        isPriceEntered = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = itemCountEdit.getText().toString();
                if(!text.equals("")){
                    itemQuantity = Integer.parseInt(text);
                }
            }
        });

        boolean isEditItem = intent.getExtras().getBoolean("IsEdit");
        long id = intent.getExtras().getLong("ItemID");
        if(isEditItem){
            for(Item it :CurrentList.list.getItems()) {
                if(it.getItemId() == id) {
                    editName.setText(it.getItemName());
                    itemPriceEdit.setText(Float.toString(it.getItemPrice()));
                    itemCountEdit.setText(Integer.toString(it.getCount()));
                    EditText commentEdit = findViewById(R.id.CommentEdit);
                    commentEdit.setText(it.getDescription());
                    Spinner spinnerCats = findViewById(R.id.CategorySpinner);
                    if (!it.getCategories().equals("")) {
                        int i = getSpinnerIndex(spinnerCats, it.getCategories());
                        spinnerCats.setSelection(i);
                    }
                }
            }

        }

    }

    private void setListTitle(Intent i){
        String s = i.getExtras().getString("ListName");
        listName = s;
    }

    public void onSubmitClick (View v)
    {
        EditText e1 = this.findViewById(R.id.ItemPriceEdit);
        EditText e2 = this.findViewById(R.id.ItemNumberEdit);
        TextView name = findViewById(R.id.nameEdit);
        TextView comment = findViewById(R.id.CommentEdit);
        Spinner category = findViewById(R.id.CategorySpinner);

        //Stop empty string errors
        Float price = 0F;
        String txtPrice = e1.getText().toString();
        int count = 0;
        String txtCount = e2.getText().toString();
        String itemName = name.getText().toString();

        if(itemName.equals("")){
            noItemAddedAlert();
            return;
        }

        if(!txtPrice.equals("")) {
            price = Float.parseFloat(txtPrice);
        }

        if(!txtCount.equals("")) {
            count = Integer.parseInt(txtCount);
        }

        //get category information
        String cat = "";
        if(category.getSelectedItem() != null){
            cat = category.getSelectedItem().toString();
        }

        //add new item to code storage
        Item itm = new Item(itemName, cat, price);
        String comm = comment.getText().toString();
        if(comm.equals("")){
            itm.setDescription(comm);
        }

        for(int i = 1; i < count; i++){
            itm.incrementCount();
        }

        //Either update the edited item or create new one to add to list
        if(getIntent().getExtras().getBoolean("IsEdit")){
            for(Item i : CurrentList.list.getItems()){
                if(i.getItemId() == getIntent().getExtras().getLong("ItemID") ||
                        i.getItemName() == itemName){
                    //replace the item and edit in db
                    i.setHasBeenEdited(true);
                    i.copyOfEditItemProperties = itm;
                    break;
                }
            }
        }
        else {
            CurrentList.list.addItem(itm);
        }


        Intent i = new Intent(v.getContext(), ListActivity.class);
        i.putExtra("ListName", listName);
        startActivity(i);

    }

    public void noItemAddedAlert(){
        AlertDialog alertDialog = new AlertDialog.Builder(AddItemActivity.this).create();
        alertDialog.setTitle("");
        alertDialog.setMessage("Please fill in name field");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
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