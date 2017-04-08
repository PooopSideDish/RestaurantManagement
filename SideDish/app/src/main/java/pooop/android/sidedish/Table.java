package pooop.android.sidedish;

/* The Table Model. This will eventually store all of the orders for a given table */
public class Table {

    private int mNum;
    private int mSection;
    private String mStatus = "Empty";

    public Table(int tableNum, int tableSection){
        mNum = tableNum;
        mSection = tableSection;
    }

    public int getNumber(){
        return mNum;
    }

    public String getStatus(){
        return mStatus;
    }

    public int getSection(){
        return mSection;
    }
}
