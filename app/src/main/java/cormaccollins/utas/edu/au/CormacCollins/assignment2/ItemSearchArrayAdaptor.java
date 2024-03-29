package cormaccollins.utas.edu.au.CormacCollins.assignment2;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

// --------------------------------------------------------------------
// For lists of Items gathered through UI which need various details
// --------------------------------------------------------------------

public class ItemSearchArrayAdaptor extends ArrayAdapter<Item> {
    private int mLayoutResourceID;
    public ItemSearchArrayAdaptor(Context context, int resource, List<Item> objects){
        super(context, resource, objects);

        //Resource is our custom view 'add_item_layout'
        mLayoutResourceID = resource;
    }



    // -----------------------------------------------------------------------------
    // Will populate the add_item_layout rows with item details
    // ---------------------------------------------------------------------------
    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {

        //Get an inflator
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        //inflate view, relative to parent etc.
        View row = layoutInflater.inflate(mLayoutResourceID, parent, false);

        TextView lblItem = row.findViewById(R.id.lblItemSettings);
        TextView lblPrice = row.findViewById(R.id.lblPriceSettings);
        TextView lblCount = row.findViewById(R.id.settingsItemViewCount);
        //Get this item attached to this adaptor at 'position'
        final Item item = this.getItem(position);




        //Set the text to information from our property
        //textView.setText(p.getAddress());
        lblItem.setText(item.getItemName());
        lblPrice.setText(Float.toString(item.getItemPrice() * item.getCount()));
        lblCount.setText(Integer.toString(item.getCount()));
        return row;
    }


}
