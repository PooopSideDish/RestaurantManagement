package pooop.android.sidedish;

public class Employee {

    private String mID;
    private int mType;

    public Employee(String id, int type) {
        mID   = id;
        mType = type;
    }

    public String getID(){
        return mID;
    }

    public String getType(){
        String returnString = "UNKNOWN";
        switch(mType){
            case 1:
                returnString = "Manager";
                break;
            case 2:
                returnString = "Waitstaff";
                break;
            case 3:
                returnString = "Kitchen";
                break;
        }

        return returnString;
    }

    public int getTypeInt(){
        return mType;
    }

    public void setID(String id){
        mID = id;
    }

    public void setType(int type){
        mType = type;
    }
}
