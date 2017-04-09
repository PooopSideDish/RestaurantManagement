package pooop.android.sidedish;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private List<SideDishMenuItem> mItems;
    private double mTotal;

    public Order(){
        mItems = new ArrayList<SideDishMenuItem>();
        mTotal = 0.0;
    }

    public List<SideDishMenuItem> getItems(){
        return mItems;
    }

    public double getTotal(){
        return mTotal;
    }

    public void addItem(SideDishMenuItem item){
        mItems.add(item);
    }
}
