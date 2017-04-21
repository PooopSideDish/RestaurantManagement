package pooop.android.sidedish;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MenuController {

    private static MenuController sMenuController;

    private Context mContext;
    private List<SideDishMenuItem> mMenuItemList;
    private SideDishDataBaseHelper mDBHelper;

    public static MenuController getInstance(Context context) {
        if(sMenuController == null) sMenuController = new MenuController(context);
        return sMenuController;
    }

    private MenuController(Context context) {
        mContext = context.getApplicationContext();
        mDBHelper = new SideDishDataBaseHelper(mContext);
        mMenuItemList = mDBHelper.getMenu();
    }

    public List<SideDishMenuItem> getMenu(){
        return mMenuItemList;
    }

    public void addMenuItem(String title, double price){
        // add to the database
        mDBHelper.addMenuItem(title, price);
        mMenuItemList = mDBHelper.getMenu();
    }

    public String[] getMenuTitles(){
        ArrayList<String> beginArray = new ArrayList<>();
        int j=0;
        for(int i = 0; i < mMenuItemList.size(); i++)
            if(mMenuItemList.get(i).getVisibility() == 1) {
                beginArray.add(mMenuItemList.get(i).getTitle());
                j++;
            }
        String[] retArray = new String[beginArray.size()];
        for(int i=0;i< beginArray.size();i++) retArray[i] = beginArray.get(i);
        return retArray;
    }

    public void hideMenuItem(String name){

    }

    public SideDishMenuItem getMenuItemByName(String name){
        if(name == null || name.length() <= 0) return null;
        return mDBHelper.getMenuItemByName(name);
    }

    public void editMenuItem(String oldTitle, String newTitle, double price){
        mDBHelper.editMenuItem(oldTitle, newTitle, price);
        mMenuItemList = mDBHelper.getMenu();
    }

    public void toggleMenuItemVisibility(String title){
        mDBHelper.toggleMenuItemVisibility(title);
        mMenuItemList = mDBHelper.getMenu();
    }

    public void deleteMenuItem(SideDishMenuItem item){
        mDBHelper.deleteMenuItem(item.getTitle());
        mMenuItemList = mDBHelper.getMenu();
    }
}
