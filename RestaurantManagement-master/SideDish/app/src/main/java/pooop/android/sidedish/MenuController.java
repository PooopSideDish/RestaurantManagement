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

        // add to the list
        SideDishMenuItem newItem = new SideDishMenuItem(title, price);
        mMenuItemList.add(newItem);
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
