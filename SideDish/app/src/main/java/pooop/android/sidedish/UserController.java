package pooop.android.sidedish;

import android.content.Context;

import java.util.List;

public class UserController {

    private static UserController sUserController;

    private Context mContext;
    private List<Employee> mUsers;
    private SideDishDataBaseHelper mDBHelper;

    public static UserController getInstance(Context context) {
        if(sUserController == null) sUserController = new UserController(context);
        return sUserController;
    }

    private UserController(Context context) {
        mContext = context.getApplicationContext();
        mDBHelper = new SideDishDataBaseHelper(mContext);
        mUsers = mDBHelper.getUsers();
    }

    public List<Employee> getUsers(){
        return mUsers;
    }

    public void addUser(String id, int type){
        mDBHelper.addUser(id, type);
        mUsers = mDBHelper.getUsers();
    }

    public void editUser(String oldID, String newID, int type){
        mDBHelper.editUser(oldID, newID, type);
        mUsers = mDBHelper.getUsers();
    }

    public void deleteUser(Employee user){
        mDBHelper.deleteUser(user.getID());
        mUsers = mDBHelper.getUsers();
    }

    public boolean isValidUser(String id){
        for(int i = 0; i < mUsers.size(); i++){
            if(mUsers.get(i).getID().equals(id)) return true;
        }
        return false;
    }
}
