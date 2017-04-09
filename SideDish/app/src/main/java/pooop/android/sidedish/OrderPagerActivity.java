package pooop.android.sidedish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class OrderPagerActivity extends AppCompatActivity {

    public static final String EXTRA_TABLE_NUM = "pooop.android.sidedish.table_num";

    private ViewPager mViewPager;
    private Table mTable;

    public static Intent newIntent(Context packageContext, Table table){
        Intent intent = new Intent(packageContext, OrderPagerActivity.class);
        intent.putExtra(EXTRA_TABLE_NUM, table.getNumber());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // TODO: Layout the OrderFragment, Set up the OrderViewPager and add the new order FAB
//         setContentView(R.layout.activity_order_pager);
//
//         int tableNum = getIntent().getIntExtra(EXTRA_TABLE_NUM, 0);

//         mTable = TableController.getInstance(getApplicationContext()).getTable(tableNum);

    }
}
