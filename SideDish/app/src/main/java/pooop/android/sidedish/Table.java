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

        // TEST Orders
        Order testOrder = new Order();
        SideDishMenuItem sdmi1 = new SideDishMenuItem("Dr. Pupper", 2.50);
        SideDishMenuItem sdmi2 = new SideDishMenuItem("Coke in Kola", 2.50);
        SideDishMenuItem sdmi3 = new SideDishMenuItem("Sweer Potato", 12.20);
        SideDishMenuItem sdmi4 = new SideDishMenuItem("Tender Loins",10.15);
        testOrder.addItem(sdmi1);
        testOrder.addItem(sdmi2);
        testOrder.addItem(sdmi3);
        testOrder.addItem(sdmi4);
        addOrder(testOrder);

        Order testOrder2 = new Order();
        SideDishMenuItem sdmi5 = new SideDishMenuItem("Ginger's Ale", 2.50);
        SideDishMenuItem sdmi6 = new SideDishMenuItem("Eggs, Ben and Dick (Lunch)", 8.15);
        testOrder2.addItem(sdmi5);
        testOrder2.addItem(sdmi6);
        addOrder(testOrder2);

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
