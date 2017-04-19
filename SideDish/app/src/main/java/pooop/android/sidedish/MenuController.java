package pooop.android.sidedish;

import android.content.Context;

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
        String[] retArray = new String[mMenuItemList.size()];
        for(int i = 0; i < mMenuItemList.size(); i++) retArray[i] = mMenuItemList.get(i).getTitle();
        return retArray;
    }

    public SideDishMenuItem getMenuItemByName(String name){
        if(name == null || name.length() <= 0) return null;
        return mDBHelper.getMenuItemByName(name);
    }

    public void editMenuItem(String oldTitle, String newTitle, double price){
        mDBHelper.editMenuItem(oldTitle, newTitle, price);
        // I don't know where the menu item that's being updated is at in the list, so just getting
        // the list from the database again: it's O(n) either way you spin it
        mMenuItemList = mDBHelper.getMenu();
    }

    public void deleteMenuItem(SideDishMenuItem item){
        mDBHelper.deleteMenuItem(item.getTitle());
        // There actually is a way to do this in O(1) time using the position arg from the
        // onBind method. If there's time we should update.
        mMenuItemList = mDBHelper.getMenu();
    }
}
