package pooop.android.sidedish;

import android.content.Context;

import java.util.List;

public class OrderController {

    private static OrderController sOrderController;

    private Context mContext;
    private List<SideDishMenuItem> mQueue;
    private SideDishDataBaseHelper mDBHelper;

    public static OrderController getInstance(Context context) {
        if(sOrderController == null) sOrderController = new OrderController(context);
        return sOrderController;
    }

    private OrderController(Context context){
        mContext  = context.getApplicationContext();
        mDBHelper = new SideDishDataBaseHelper(mContext);
        mQueue    = mDBHelper.getOrderQueue();
    }

    public Order addNewOrder(int tableNum){
        return mDBHelper.addNewOrder(tableNum);
    }

    /* Add the new item to the order details, under the order number */
    public void addItemToOrder(int orderNum, SideDishMenuItem newItem){
        mDBHelper.addItemToOrder(orderNum, newItem);
    }

    /* Sync the order in memory with the order in the database */
    public void updateOrder(Order o){
        mDBHelper.updateOrder(o);
    }

    /* Submit an order to the queue and record it in the history */
    public void submitOrderToQueue(Order o) {
        mDBHelper.submitOrderToQueue(o);
        mDBHelper.recordOrder(o);
    }

    public void removeOrderFromQueue(Order o){
        mDBHelper.removeOrderFromQueue(o);
        mQueue = mDBHelper.getOrderQueue();
    }

    /* Remove an item from the Database */
    public void removeItemFromOrder(int orderNum, SideDishMenuItem item){
        mDBHelper.removeItemFromOrder(orderNum, item);
    }

    public List<SideDishMenuItem> getQueue(){
        mQueue = mDBHelper.getOrderQueue();
        return mQueue;
    }
}
