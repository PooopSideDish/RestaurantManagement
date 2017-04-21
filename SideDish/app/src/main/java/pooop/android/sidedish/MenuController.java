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
        mMenuItemList = mDBHelper.getMenu();
        ArrayList<String> temp = new ArrayList<>();
        for(int i=0; i<mMenuItemList.size(); i++){
            if(mMenuItemList.get(i).getVisibility() == 1){
                temp.add(mMenuItemList.get(i).getTitle());
            }
        }

        String[] retArray = new String[temp.size()];

        for(int i=0; i<temp.size(); i++) retArray[i] = temp.get(i);

        return retArray;
    }

    public ArrayList<Statistic> getStatistics(int startYear, int startMonth, int startDay,
                    int endYear, int endMonth, int endDay) {

        // Make sure start is same or before end
        if(endYear < startYear){
            int temp = endYear;
            endYear = startYear;
            startYear = temp;

            temp = endMonth;
            endMonth = startMonth;
            startMonth = temp;

            temp = endDay;
            endDay = startDay;
            startDay = temp;
        }

        if(endYear == startYear && endMonth < startMonth){
            int temp = endMonth;
            endMonth = startMonth;
            startMonth = temp;

            temp = endDay;
            endDay = startDay;
            startDay = temp;
        }

        if(endYear == startYear && endMonth == startMonth && endDay < startDay) {
            int temp = endDay;
            endDay = startDay;
            startDay = temp;
        }

        return mDBHelper.getStatistics(startDay, startMonth, startYear, endDay, endMonth, endYear);
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
