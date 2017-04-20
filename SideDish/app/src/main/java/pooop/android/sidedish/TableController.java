package pooop.android.sidedish;

import android.content.Context;

import java.util.List;

public class TableController {

    private static TableController sTableController;

    private Context mContext;
    private List<Table> mTableList;
    private SideDishDataBaseHelper mDBHelper;

    public static TableController getInstance(Context context) {
        if(sTableController == null) sTableController = new TableController(context);
        return sTableController;
    }

    private TableController(Context context) {
        mContext = context.getApplicationContext();
        mDBHelper = new SideDishDataBaseHelper(mContext);
        mTableList = mDBHelper.getTables();
    }

    public List<Table> getTables(){
        mTableList = mDBHelper.getTables();
        return mTableList;
    }

    public Table getTable(int tableNum){
        // if user deletes tables, the numbering will fall out of sync with the index position
        for(int i = 0; i < mTableList.size(); i++){
            if(mTableList.get(i).getNumber() == tableNum) return mTableList.get(i);
        }
        return null;
    }

    public void addTable(String section){
        mDBHelper.addTable(section);
        mTableList = mDBHelper.getTables();
    }

    public void editTable(int tableNum, String newSection){
        // if user deletes tables, the numbering will fall out of sync with the index position
        for(int i = 0; i < mTableList.size(); i++){
            if(mTableList.get(i).getNumber() == tableNum){
                mDBHelper.editTable(mTableList.get(i).getNumber(), newSection);
                mTableList = mDBHelper.getTables();
                return;
            }
        }
    }

    public void removeOrderFromTable(int orderIndex, Table table){
        int orderID = table.getOrder(orderIndex).getNumber();
        int tableNum = table.getNumber();

        mDBHelper.removeOrderFromTable(orderID, tableNum);

        // Update everything because it's 4:00 AM and I can no longer think
        mTableList = mDBHelper.getTables();
    }

    public void removeAllOrdersFromTable(Table table){
        mDBHelper.removeAllOrdersFromTable(table);
    }

    public void deleteTable(int tableNum){
        // if user deletes tables, the numbering will fall out of sync with the index position
        for(int i = 0; i < mTableList.size(); i++){
            if(mTableList.get(i).getNumber() == tableNum){
                mDBHelper.deleteTable(mTableList.get(i).getNumber());
                mTableList = mDBHelper.getTables();
                return;
            }
        }
    }

    public void setTableStatus(Table table, int status){
        mDBHelper.setTableStatus(table, status);
        mTableList = mDBHelper.getTables();
    }
}
