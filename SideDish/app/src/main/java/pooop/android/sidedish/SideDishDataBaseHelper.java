package pooop.android.sidedish;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

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
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM menu ORDER BY title ASC;", null);
        ArrayList<SideDishMenuItem> retList = new ArrayList<>();

        cursor.moveToFirst();
        try {
            while (!cursor.isAfterLast()) {
                //                                    item title            item price         item id
                retList.add(new SideDishMenuItem(cursor.getString(1), cursor.getDouble(2), cursor.getInt(0)));
                cursor.moveToNext();
            }
        }
        finally{
            cursor.close();
        }

        return retList;
    }

    public SideDishMenuItem getMenuItemByName(String name){
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM menu WHERE title=?",
                new String[]{name});
        SideDishMenuItem retItem = null;
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            //                                    item title            item price         item id
            retItem = new SideDishMenuItem(cursor.getString(1), cursor.getDouble(2), cursor.getInt(0));
        }
        cursor.close();
        return retItem;
    }

    public void addMenuItem(String title, double price){
        mDatabase.execSQL("INSERT INTO menu VALUES (NULL, ?, ?);",
                new String[]{title, String.valueOf(price)});
    }

    public void editMenuItem(String oldTitle, String newTitle, double price) {
        mDatabase.execSQL("UPDATE menu SET title=?, price=? WHERE title=?;",
                new String[]{newTitle, String.valueOf(price), oldTitle});
    }

    // NOTE: if two menu items have the same name, this deletes them both! :D
    public void deleteMenuItem(String title){
        mDatabase.execSQL("DELETE FROM menu WHERE title=?", new String[]{title});
    }

    public int getMenuItemIDByTitle(String title){
        Cursor cursor = mDatabase.rawQuery("SELECT id FROM menu WHERE title=?",
                new String[]{title});

        cursor.moveToFirst();
        int id = cursor.getInt(0);
        cursor.close();

        return id;
    }

    public ArrayList<Employee> getUsers(){
        Cursor cursor = mDatabase.rawQuery("SELECT id, type FROM users ORDER BY id ASC;", null);
        ArrayList<Employee> retList = new ArrayList<>();

        cursor.moveToFirst();
        try {
            while (!cursor.isAfterLast()) {
                retList.add(new Employee(cursor.getString(0), cursor.getInt(1), cursor.getString(0)));
                cursor.moveToNext();
            }
        }
        finally{
            cursor.close();
        }

        return retList;
    }

    public void addUser(String id, int type, String password){
        mDatabase.execSQL("INSERT INTO users VALUES (?, ?, ?);",
                new String[]{id, String.valueOf(type), password});
    }

    public void editUser(String oldId, String newId, int type, String password) {
        mDatabase.execSQL("UPDATE users SET id=?, type=?, password=? WHERE id=?;",
                new String[]{newId, String.valueOf(type), password, oldId});
    }

    public void deleteUser(String id){
        mDatabase.execSQL("DELETE FROM users WHERE id=?", new String[]{id});
    }

    /* Query the database to retrieve all the tables */
    public ArrayList<Table> getTables() {
        // Selecting all existing tables
        Cursor tableCursor = mDatabase.rawQuery("SELECT id, section, status FROM tables ORDER BY id ASC;", null);
        ArrayList<Table> tableList = new ArrayList<>();

        Log.d("LOG:", "Getting all tables from database");
        Log.d("LOG:", "There are *" + tableCursor.getCount() + "* tables");

        tableCursor.moveToFirst();
        while (!tableCursor.isAfterLast()) {
            //                           tableNum                    section                  status
            Table newTable = new Table(tableCursor.getInt(0), tableCursor.getString(1), tableCursor.getInt(2));
            Log.d("LOG:", "Retrieved Table #" + tableCursor.getInt(0));

            // Fetch all the active orders that belong to this table
            newTable.addOrders(getOrdersByTableID(newTable.getNumber()));

            tableList.add(newTable);
            tableCursor.moveToNext();
        }
        tableCursor.close();

        return tableList;
    }

    /* Query the database and build all orders for a specified table */
    public ArrayList<Order> getOrdersByTableID(int tableID) {
        // Selecting all active orders
        Cursor orderCursor = mDatabase.rawQuery("SELECT orders.id, orders.order_status, orders.table_id " +
                "FROM orders WHERE orders.table_id==?;", new String[]{String.valueOf(tableID)});
        ArrayList<Order> orderList = new ArrayList<>();

        Log.d("LOG:", "Getting all orders for table #" + tableID + " from database");
        Log.d("LOG:", "There are *" + orderCursor.getCount() + "* orders");

        orderCursor.moveToFirst();
        while(!orderCursor.isAfterLast()) {
            /* TESTING */
            Log.d("LOG:", "Retrieved order id = " + orderCursor.getInt(0) + " associated with table id = " + orderCursor.getInt(2));
            /* TESTING */

            //                               order id          order status as int
            Order newOrder = new Order(orderCursor.getInt(0), orderCursor.getInt(1));
            newOrder.addItems(getMenuItemsByOrderID(newOrder.getNumber()));

            orderList.add(newOrder);
            orderCursor.moveToNext();
        }
        orderCursor.close();

        return orderList;
    }

    /* Query the database and build all items for a specified order */
    public ArrayList<SideDishMenuItem> getMenuItemsByOrderID(int orderNum) {
        Log.d("LOG:", "Getting all items for order #" + orderNum + " from database");

        ArrayList<SideDishMenuItem> itemList = new ArrayList<>();

        // First get a set of all menu item ids
        String queryOrderDetails = "SELECT menu_item_id, menu_item_comment, menu_item_status, order_number " +
                "FROM order_details " +
                "WHERE order_number==?";
        Cursor detailsCursor = mDatabase.rawQuery(queryOrderDetails,new String[]{String.valueOf(orderNum)});

        Log.d("LOG:", "There are *" + detailsCursor.getCount() + "* items");

        // Now use that result set to get the menu items (we're going to get fancy here)
        detailsCursor.moveToFirst();
        while(!detailsCursor.isAfterLast()) {
            String itemId = String.valueOf(detailsCursor.getInt(0));
            String queryMenuItems = "SELECT title, price, id FROM menu WHERE id==?";
            Cursor itemCursor = mDatabase.rawQuery(queryMenuItems, new String[]{itemId});


            // Build a menu item object from the two cursors
            itemCursor.moveToFirst();
            Log.d("LOG:", "Retrieved item: " + itemCursor.getString(0) + " associated with order id = " + detailsCursor.getInt(3));
            String title = itemCursor.getString(0);
            double price = itemCursor.getDouble(1);
            String comment = detailsCursor.getString(1);
            int status = detailsCursor.getInt(2);

            itemList.add(new SideDishMenuItem(title, price, comment, status, detailsCursor.getInt(0), detailsCursor.getInt(3)));

            itemCursor.close();
            detailsCursor.moveToNext();
        }

        return itemList;
    }

    public void addTable(String section){
        mDatabase.execSQL("INSERT INTO tables VALUES (null, ?, 0);",
                new String[]{section});
    }

    public void editTable(int tableNum, String newSection) {
        mDatabase.execSQL("UPDATE tables SET section=? WHERE id==?;",
                new String[]{newSection, String.valueOf(tableNum)});
    }

    public void deleteTable(int tableNum){
        mDatabase.execSQL("DELETE FROM tables WHERE id==?;",
                new String[]{String.valueOf(tableNum)});
    }

    public void setTableStatus(Table table, int status){
        mDatabase.execSQL("UPDATE tables SET status=? WHERE id==?;",
                new String[]{String.valueOf(status), String.valueOf(table.getNumber())});
    }

    public ArrayList<SideDishMenuItem> getOrderQueue(){
        ArrayList<SideDishMenuItem> queue = new ArrayList<>();

        // Get all the orders from the queue table
        // order number is index 6. 0-5 are date fields
        Log.d("LOG:", "Getting all orders from order_queue");
        Cursor orderCursor = mDatabase.rawQuery("SELECT * FROM order_queue ORDER BY " +
                "year ASC, month ASC, day ASC, hour ASC, minute ASC, second ASC" +
                ";", null);

        Log.d("LOG:", "There are *" + orderCursor.getCount() + "* orders in order_queue");

        orderCursor.moveToFirst();
        while(!orderCursor.isAfterLast()){

            // Use the order id and get all the menu items for that order.
            // For every menu item associated with the id, add it to the queue
            Log.d("LOG:", "Getting all items associated with order #" + orderCursor.getInt(6));
            ArrayList<SideDishMenuItem> orderItems = getMenuItemsByOrderID(orderCursor.getInt(6));
            Log.d("LOG:", "There are *" + orderItems.size() + "* items associated with order #" + orderCursor.getInt(6));
            for(int i=0; i<orderItems.size(); i++){
                queue.add(orderItems.get(i));
            }

            orderCursor.moveToNext();
        }
        orderCursor.close();

        Log.d("LOG:", "There are *" + queue.size() + "* items in the queue");
        return queue;
    }

    /* Add an order to the queue */
    public void submitOrderToQueue(Order o){
        Log.d("LOG:", "Adding Order #" + o.getNumber() + " to order_queue");
        String orderNum = String.valueOf(o.getNumber());

        // If the order is already in the queue, it references the order_details, which
        // are updated everytime an item is altered or created in the order screen.
        // There should be no need to resubmit an altered order.
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM order_queue WHERE order_number==?",
                new String[]{orderNum});
        if(cursor.getCount() > 0){
            Log.d("LOG:", "Order #" + o.getNumber() + " Already exists in order_queue");
            return;
        }

        // Get the date
        Calendar c = Calendar.getInstance();
        String year   = String.valueOf(c.get(Calendar.YEAR));
        String month  = String.valueOf(c.get(Calendar.MONTH));
        String day    = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String hour   = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(c.get(Calendar.MINUTE));
        String second = String.valueOf(c.get(Calendar.SECOND));

        mDatabase.execSQL("INSERT INTO order_queue VALUES (?, ?, ?, ?, ?, ?, ?);",
                new String[]{year, month, day, hour, minute, second, orderNum});
        Log.d("LOG:", "Order #" + o.getNumber() + " inserted into order_queue with date " +
                year + " " + month + " " + day + " " + hour + " " + minute + " " + second + " " );
    }

    /* Removing an order from order_queue should NOT remove the order from orders or order_details */
    public void removeOrderFromQueue(Order o){
        mDatabase.execSQL("DELETE FROM order_queue WHERE order_number==?",
                new String[]{String.valueOf(o.getNumber())});
    }

    /* Add a new, empty order to the database */
    public Order addNewOrder(int tableNum){
        // Insert a new order with no items
        mDatabase.execSQL("INSERT INTO orders VALUES (null, 0, ?);",
                new String[]{String.valueOf(tableNum)});

        // Grab the last inserted item's id (the new order)
        // Use that id as the order number.
        Cursor cursor = mDatabase.rawQuery("SELECT last_insert_rowid() FROM orders;", null);

        cursor.moveToFirst();
        Order newOrder = new Order(cursor.getInt(0));
        cursor.close();

        return newOrder;
    }

    public void removeOrderFromTable(int orderNum, int tableNum){
        // Cascading delete should take care of every dependency
        mDatabase.execSQL("DELETE FROM orders WHERE id==? AND table_id==?",
                new String[]{String.valueOf(orderNum), String.valueOf(tableNum)});
    }

    public void removeAllOrdersFromTable(Table table){
        // Cascading delete should take care of every dependency
        String tableID = String.valueOf(table.getNumber());
        mDatabase.execSQL("DELETE FROM orders WHERE table_id==?;",
                new String[]{tableID});
    }

    public void addItemToOrder(int orderNum, SideDishMenuItem newItem){

        String itemID = String.valueOf(newItem.getID());
        String itemComment = newItem.getComment();
        String itemStatus  = String.valueOf(newItem.getStatusInt());
        Log.d("LOG:", "Inserting menu item #" + itemID + " into order #" + orderNum);
        mDatabase.execSQL("INSERT INTO order_details VALUES (?, ?, ?, ?);",
                new String[]{String.valueOf(itemID), itemComment, itemStatus, String.valueOf(orderNum)});
    }

    public void updateOrder(Order o){

        // Taking the nuclear option
        // Remove all old order details
        mDatabase.execSQL("DELETE FROM order_details WHERE order_number==?",
                new String[]{String.valueOf(o.getNumber())});

        // For every item in the order, add it back in
        for(int i=0; i<o.getItems().size(); i++){
            addItemToOrder(o.getNumber(), o.getItem(i));
        }
    }

    /* Remove exactly 1 item from the order */
    /* Android doesn't allow DELETE FROM ... WHERE ... LIMIT ... so I'm using this subquery hack
     * to handle duplicates */
    public void removeItemFromOrder(int orderNum, SideDishMenuItem item){
        mDatabase.execSQL("DELETE FROM order_details WHERE " +
                "_rowid_==(SELECT _rowid_ FROM order_details WHERE order_number==? AND menu_item_id==? LIMIT 1);",
                new String[]{String.valueOf(orderNum), String.valueOf(item.getID())});
    }

    /* Add an order to the history */
    public void recordOrder(Order o){
        // Generate a time stamp for the database
        // Calendars are easy to set for statistics so using Calendar object instead of Date object
        Calendar c = Calendar.getInstance();
        String year   = String.valueOf(c.get(Calendar.YEAR));
        String month  = String.valueOf(c.get(Calendar.MONTH));
        String day    = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String hour   = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(c.get(Calendar.MINUTE));
        String second = String.valueOf(c.get(Calendar.SECOND));

        // For every title, insert a title and date into the history
        for(int i=0; i < o.getItems().size(); i++){
            String title = o.getItems().get(i).getTitle();
            mDatabase.execSQL("INSERT INTO history VALUES (?, ?, ?, ?, ?, ?, " +
                    "(SELECT id FROM menu WHERE title==?)" +
                    ");", new String[]{year, month, day, hour, minute, second, title});
        }
    }

    /* Create all the tables for the database if it doesn't exist */
    @Override
    public void onCreate(SQLiteDatabase db) {
        mDatabase = db;

        // Table of tables
        // status: 0 -> empty, 1 -> has orders
        mDatabase.execSQL("CREATE TABLE tables (" +
                "id INTEGER PRIMARY KEY, "        +
                "section TEXT COLLATE NOCASE, "   +
                "status INTEGER);");

        // Menu items table
        mDatabase.execSQL("CREATE TABLE menu (" +
                "id INTEGER PRIMARY KEY, "      +
                "title TEXT COLLATE NOCASE, "   +
                "price DOUBLE);");

        // Table of active orders
        // order_status: 0 -> new, 1 -> pending, 2 -> in progress, 3 -> complete
        mDatabase.execSQL("CREATE TABLE orders (" +
                "id INTEGER PRIMARY KEY, "        +
                "order_status INTEGER, "          +
                "table_id INTEGER REFERENCES tables(id) ON DELETE CASCADE);");

        // This table lists all of the menu items contained by the active orders
        // It is simply a list of menu items which are associated with an order id
        // Each menu item has a status: 0 -> pending, 1 -> in progress, 2 -> complete
        // When all the statuses are complete, the associated order is complete
        mDatabase.execSQL("CREATE TABLE order_details (" +
                "menu_item_id INTEGER REFERENCES menu(id) ON DELETE CASCADE, "  +
                "menu_item_comment TEXT, "   +
                "menu_item_status INTEGER, " +
                "order_number INTEGER REFERENCES orders(id) ON DELETE CASCADE);" );

        // This is the queue for the kitchen
        // It is a list of orders associated with the date that they were submitted
        // Not using timestamps here because not using timestamps in the history table
        mDatabase.execSQL("CREATE TABLE order_queue (" +
                "year INTEGER, month INTEGER, day INTEGER, hour INTEGER, minute INTEGER, second INTEGER, " +
                "order_number INTEGER REFERENCES orders(id) ON DELETE CASCADE);");

        // Users table
        // Using implicit primary key id. The id column is the user's id used to login
        // type integer distinguishes privileges: 1 -> manager, 2 -> waitstaff, 3 -> kitchen
        mDatabase.execSQL("CREATE TABLE users (" +
                "id TEXT COLLATE NOCASE, " +
                "type INTEGER, "           +
                "password TEXT "          +
                ");");

        // Table of all previous orders taken (for statistics)
        // month column is numbered [1,12] (1 -> Jan, ... 12 -> Dec)
        // Not using timestamps because setting a Calendar object is easier if I can just pull
        // integer values out of the query and not deal with conversions
        mDatabase.execSQL("CREATE TABLE history (" +
                "year INTEGER, month INTEGER, day INTEGER, hour INTEGER, minute INTEGER, second INTEGER, " +
                "menu_item_id INTEGER REFERENCES menu(id) ON DELETE CASCADE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // unused
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        db.execSQL("PRAGMA foreign_keys=ON");
    }
}
