package pooop.android.sidedish;

public class Employee {

    private String mID;
    private String mPassword;
    private int mType;

    public Employee(String id, int type, String password) {
        mID   = id;
        mPassword = password;
        mType = type;
    }

    public String getID(){
        return mID;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getType(){
        String returnString = "UNKNOWN";
        switch(mType){
            case 1:
                returnString = "Manager";
                break;
            case 2:
                returnString = "WaitStaff";
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
