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

    public SideDishMenuItem getItem(int position){
        if(position < 0 || position >= mItems.size()) return null;
        return mItems.get(position);
    }

    public double getTotal(){
        return mTotal;
    }

    public void addItem(SideDishMenuItem item){
        mItems.add(item);
        mTotal += item.getPrice();
    }

    public void setItem(int position, SideDishMenuItem newItem){
        if(position < 0 || position >= mItems.size()) return;
        mTotal -= mItems.get(position).getPrice();
        mTotal += newItem.getPrice();
        mItems.set(position, newItem);
    }

    public void removeItem(int position){
        if(position < 0 || position >= mItems.size()) return;
        mTotal -= mItems.get(position).getPrice();
        mItems.remove(position);
    }
}
