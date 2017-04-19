package pooop.android.sidedish;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private int mNum;
    private String mSection = "No Section";
    private String mStatus = "Empty";
    private List<Order> mOrders;

    public Table(int tableNum, String tableSection){
        this(tableNum, tableSection, 0);
    }

    public Table(int tableNum, String tableSection, int status){
        mNum = tableNum;
        mSection = tableSection;
        mOrders = new ArrayList<Order>();

        switch(status){
            case 0:
                mStatus = "Empty";
                break;
            case 1:
                mStatus = "Has Orders";
                break;
        }
    }

    /* Add all orders from a list */
    public void addOrders(ArrayList<Order> orderList){
        if(orderList == null) return;
        for(int i=0; i < orderList.size(); i++) mOrders.add(orderList.get(i));
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

    public int getNumOrders(){
        return mOrders.size();
    }

    public List<Order> getOrders(){
        return mOrders;
    }

    /* Maybe should be by order id? */
    public Order getOrder(int orderIndex){
        return mOrders.get(orderIndex);
    }

    public Order getOrderByIndex(int index){
        if(index < 0 || index >= mOrders.size()) return null;
        return mOrders.get(index);
    }

    public void addOrder(Order o){
        mOrders.add(o);
    }

    public void removeOrder(int orderIndex){
        if(orderIndex < 0 || orderIndex >= mOrders.size()) return;
        mOrders.remove(orderIndex);
    }
}
