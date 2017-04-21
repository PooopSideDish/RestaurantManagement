package pooop.android.sidedish;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private List<SideDishMenuItem> mItems;
    private int mNumber;
    private int mStatus;

    public Order(int number){
        this(number, 0);
    }
    public Order(int number, int status){
        mItems = new ArrayList<SideDishMenuItem>();
        mNumber = number;
        mStatus = status;
    }

    /* Add all menu items from a list */
    public void addItems(ArrayList<SideDishMenuItem> itemList){
        if(itemList == null) return;
        for(int i=0; i < itemList.size(); i++) mItems.add(itemList.get(i));
    }

    public List<SideDishMenuItem> getItems(){
        return mItems;
    }

    public SideDishMenuItem getItem(int position){
        if(position < 0 || position >= mItems.size()) return null;
        return mItems.get(position);
    }

    public void incrementItemStatus(SideDishMenuItem item){
        for(int i=0; i<mItems.size(); i++){
            SideDishMenuItem curItem = mItems.get(i);
            if(curItem.getID() == item.getID() && curItem.getStatusInt() == item.getStatusInt()) {
                curItem.progressStatus();
                break;
            }
        }
    }

    /* it's O(n) lol */
    public double getTotal(){
        double total = 0.0;
        for(int i=0; i<mItems.size(); i++) total += mItems.get(i).getPrice();
        return total;
    }

    public void addItem(SideDishMenuItem item){
        mItems.add(item);
    }

    public void setItem(int position, SideDishMenuItem newItem){
        if(position < 0 || position >= mItems.size()) return;
        mItems.set(position, newItem);
    }

    public void removeItem(int position){
        if(position < 0 || position >= mItems.size()) return;
        mItems.remove(position);
    }

    public int getNumber(){
        return mNumber;
    }

    public int getStatus(){
        return mStatus;
    }
}
