package pooop.android.sidedish;

public class SideDishMenuItem {

    private String mTitle;
    private double mPrice;
    private String mComment;
    private int mStatus;
    private int mID;
    private Integer mOrderNumber;
    private int mVisibility;

    public SideDishMenuItem(String title, double price, int id, int visibility){
        this(title, price, null, 0, id, null, visibility);
    }

    public SideDishMenuItem(String title, double price, String comment, int status, int id, Integer orderNumber, Integer visibility){
        mTitle   = title;
        mPrice   = price;
        mComment = (comment != null ? comment : "");
        mStatus  = status;
        mID = id;
        mOrderNumber = orderNumber;
        mVisibility = visibility;
    }

    public int getID(){
        return mID;
    }

    public int getVisibility(){ return mVisibility; }

    public String getTitle(){
        return mTitle;
    }

    public int getStatusInt(){
        return mStatus;
    }

    public void progressStatus(){
        // if status > 2 -> remove order from queue
        mStatus++;
    }

    public double getPrice(){
        return mPrice;
    }

    public String getComment(){
        return mComment;
    }

    public void setComment(String comment){
        mComment = comment;
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
