package pooop.android.sidedish;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class TableController {

    private static TableController sTableController;

    private Context mContext;
    private List<Table> mTableList;

    public static TableController getInstance(Context context) {
        if(sTableController == null) sTableController = new TableController(context);
        return sTableController;
    }

    private TableController(Context context) {
        mContext = context.getApplicationContext();
        mTableList = new ArrayList<>();
    }

    public List<Table> getTables(){
        return mTableList;
    }

    public Table getTable(int tableNum){
        return mTableList.get(tableNum - 1);
    }

    public void addTable(String section){
        Table newTable = new Table(mTableList.size() + 1, section);
        mTableList.add(newTable);
    }
}
