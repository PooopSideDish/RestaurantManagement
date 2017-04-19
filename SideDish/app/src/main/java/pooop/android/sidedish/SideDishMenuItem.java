package pooop.android.sidedish;

public class SideDishMenuItem {

    private String mTitle;
    private double mPrice;
    private String mComment;
    private int mStatus;
    private int mID;

    public SideDishMenuItem(String title, double price, int id){
        this(title, price, null, 0, id);
    }

    public SideDishMenuItem(String title, double price, String comment, int status, int id){
        mTitle   = title;
        mPrice   = price;
        mComment = (comment != null ? comment : "");
        mStatus  = status;
        mID = id;
    }

    public int getID(){
        return mID;
    }

    public String getTitle(){
        return mTitle;
    }

    public int getStatusInt(){
        return mStatus;
    }

    public double getPrice(){
        return mPrice;
    }

    public String getComment(){
        return mComment;
    }
}
