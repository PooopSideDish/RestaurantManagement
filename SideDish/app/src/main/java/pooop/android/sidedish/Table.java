package pooop.android.sidedish;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private int mNum;
    private String mSection = "No Section";
    private String mStatus = "Empty";
    private List<Order> mOrders;

    public Table(int tableNum){
        mNum = tableNum;
    }

    public Table(int tableNum, String tableSection){
        mNum = tableNum;
        mSection = tableSection;
        mOrders = new ArrayList<Order>();
    }

    public int getNumber(){
        return mNum;
    }

    public String getStatus(){
        return mStatus;
    }

    public String getSection(){
        return mSection;
    }

    public List<Order> getOrders(){
        return mOrders;
    }

    public Order getOrder(int orderNumber){
        return mOrders.get(orderNumber - 1);
    }

    public void addOrder(Order o){
        mOrders.add(o);
    }

    public void clearOrders(){
        mOrders = new ArrayList<Order>();
    }
}
