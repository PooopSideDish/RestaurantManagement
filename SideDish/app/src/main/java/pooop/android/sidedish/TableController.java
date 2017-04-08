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

        // Make some test tables for now
        for(int i=1; i<110; i++)
            mTableList.add(new Table(i, i/2 + 1));
    }

    public List<Table> getTables(){
        return mTableList;
    }
}
