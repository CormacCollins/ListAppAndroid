package cormaccollins.utas.edu.au.CormacCollins.assignment2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

public class ListDatabase {

    private static final String TAG = "ListDatabase";

    private static final String DATABASE_NAME = "ListDatabase";

    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase mDb;

    private DatabaseHelper mDbHelper;

    private final Context mCtx;

    public ListDatabase(Context ctx){
        this.mCtx = ctx;
    }

    public SQLiteDatabase open(){
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return mDb;
    }

    public void close(){
        mDb.close();
        mDb = null;
    }





    //Helper class used to perform action during events
    private static class DatabaseHelper extends SQLiteOpenHelper {
        //Creates the database
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            Log.d(TAG, "DatabaseHelper onCreate");
            //Setting up templates for each table type (List, item_list, item)
            db.execSQL(ListTable.CREATE_STATEMENT);
//            db.execSQL(ListTable.ITEM_LIST_CREATE_STATEMENT);
//            db.execSQL(ListTable.ITEM_CREATE_STATEMENT);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.d(TAG, "DatabaseHelper onUpgrade");
            db.execSQL("DROP TABLE IF EXISTS " + ListTable.TABLE_NAME);
            onCreate(db); //this will recreate the database as if it were new
        }



    }
}
