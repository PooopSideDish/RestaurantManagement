package pooop.android.sidedish;

public class SideDishMenuItem {

    private String mTitle;
    private double mPrice;

    public SideDishMenuItem(String title, double price){
        mTitle = title;
        mPrice = price;
    }

    public String getTitle(){
        return mTitle;
    }

    public double getPrice(){
        return mPrice;
    }
}
