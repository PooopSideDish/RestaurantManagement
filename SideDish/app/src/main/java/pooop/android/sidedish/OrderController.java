package pooop.android.sidedish;

import android.content.Context;

import java.util.List;

public class OrderController {

    // TODO: everytime the kitchen screen is interacted with, just fetch the orderqueue again

    private static OrderController sOrderController;

    private Context mContext;
    private List<Order> mQueue;
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

    public void addOrder(Order o){

    }

    /* Add the new item to the order details, under the order number */
    public void addItemToOrder(int orderNum, SideDishMenuItem newItem){
        mDBHelper.addItemToOrder(orderNum, newItem);
    }

    /* Submit an order to the queue and record it in the history */
    public void submitOrder(Order o) {
        mDBHelper.submitOrder(o);
        mDBHelper.recordOrder(o);
    }

    /* Remove an item from the Database */
    public void removeItemFromOrder(int orderNum, SideDishMenuItem item){
        mDBHelper.removeItemFromOrder(orderNum, item);
    }

    public List<Order> getQueue(){
        return mQueue;
    }
}
