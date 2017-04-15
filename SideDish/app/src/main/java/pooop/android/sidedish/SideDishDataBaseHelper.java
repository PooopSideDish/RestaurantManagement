package pooop.android.sidedish;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SideDishDataBaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "sidedish.db";
    // No need to do a database upgrade as it's created entirely by the user
    private static final int DB_VERSION = 1;

    private SQLiteDatabase mDatabase;

    public SideDishDataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mDatabase = getDatabase();
    }

    private SQLiteDatabase getDatabase() {
        if(mDatabase == null) mDatabase = getWritableDatabase();
        return mDatabase;
    }

    /* Query the database to retrieve all the menu items */
    public ArrayList<SideDishMenuItem> getMenu(){
        // It would be more elegant to use a CursorWrapper but we're short on time so...
        Cursor cursor = mDatabase.rawQuery("SELECT title, price FROM menu ORDER BY title ASC;", null);
        ArrayList<SideDishMenuItem> retList = new ArrayList<>();

        cursor.moveToFirst();
        try {
            while (!cursor.isAfterLast()) {
                retList.add(new SideDishMenuItem(cursor.getString(0), cursor.getDouble(1)));
                cursor.moveToNext();
            }
        }
        finally{
            cursor.close();
        }

        return retList;
    }

    /* Query the database to retrieve all the tables */
    public ArrayList<Table> getTables(){
        // It would be more elegant to use a CursorWrapper but we're short on time so...
        Cursor cursor = mDatabase.rawQuery("SELECT id, section FROM tables ORDER BY title ASC;", null);
        ArrayList<Table> retList = new ArrayList<>();

        cursor.moveToFirst();
        try {
            while (!cursor.isAfterLast()) {
                retList.add(new Table(cursor.getInt(0), cursor.getString(1)));
                cursor.moveToNext();
            }
        }
        finally{
            cursor.close();
        }

        return retList;
    }

    /* add a new menu item to the database */
    public void addMenuItem(String title, double price){
        mDatabase.execSQL("INSERT INTO menu VALUES (NULL, ?, ?, 0);",
                new String[]{title, String.valueOf(price)});
    }

    public void editMenuItem(String title, double price) {
        mDatabase.execSQL("update menu set title=?, price=? where title=?;",
                new String[]{title, String.valueOf(price), title});
    }


    /* Create all the tables for the database if it doesn't exist */
    @Override
    public void onCreate(SQLiteDatabase db) {
        mDatabase = db;

        // Table of tables
        mDatabase.execSQL("CREATE TABLE tables (id INTEGER PRIMARY KEY, section TEXT COLLATE NOCASE);");

        // Menu items table
        // sales count counts how many times each menu item has been sold total (for easy statistics)
        mDatabase.execSQL("CREATE TABLE menu " +
                "(id INTEGER PRIMARY KEY, title TEXT COLLATE NOCASE, price FLOAT, sales_count INTEGER);");

        // Table of all previous orders taken (for statistics)
        // month column is numbered [1,12] (1 -> Jan, ... 12 -> Dec)
        // item_id will map to the id column of the menu item table
        mDatabase.execSQL("CREATE TABLE history " +
                "(year INTEGER, month INTEGER, day INTEGER item_id INTEGER);");

        // Users table
        // Using implicit primary key id. The id column is the user's id used to login
        // type integer distinguishes privaleges: 1 -> manager, 2 -> waitstaff, 3 -> kitchen
        mDatabase.execSQL("CREATE TABLE users (id TEXT COLLATE NOCASE, type INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // unused
    }
}
