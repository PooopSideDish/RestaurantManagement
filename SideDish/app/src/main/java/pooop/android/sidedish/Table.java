package pooop.android.sidedish;

/* The Table Model. This will eventually store all of the orders for a given table */
public class Table {

    private int mNum;
    private String mSection = "No Section";
    private String mStatus = "Empty";

    public Table(int tableNum){
        mNum = tableNum;
    }

    public Table(int tableNum, String tableSection){
        mNum = tableNum;
        mSection = tableSection;
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
}
