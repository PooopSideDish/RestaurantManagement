package pooop.android.sidedish;

public class SideDishMenuItem {

    private String mTitle;
    private double mPrice;
    private String mComment;
    private int mStatus;
    private int mID;
    private Integer mOrderNumber;

    public SideDishMenuItem(String title, double price, int id){
        this(title, price, null, 0, id, null);
    }

    public SideDishMenuItem(String title, double price, String comment, int status, int id, Integer orderNumber){
        mTitle   = title;
        mPrice   = price;
        mComment = (comment != null ? comment : "");
        mStatus  = status;
        mID = id;
        mOrderNumber = orderNumber;
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

    public void setOrderNumber(int orderNumber){
        mOrderNumber = orderNumber;
    }

    public int getOrderNumber(){
        return mOrderNumber;
    }

    public String getStatusString(){
        if(mStatus == 0) return "pending";
        if(mStatus == 1) return "in progress";
        /* if (mStatus == 2) */ return "complete";
    }

}
