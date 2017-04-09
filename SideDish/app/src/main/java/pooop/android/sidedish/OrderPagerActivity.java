package pooop.android.sidedish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class OrderPagerActivity extends AppCompatActivity {

    public static final String EXTRA_TABLE_NUM = "pooop.android.sidedish.table_num";

    private ViewPager mViewPager;

    public static Intent newIntent(Context packageContext, Table table){
        Intent intent = new Intent(packageContext, OrderPagerActivity.class);
        intent.putExtra(EXTRA_TABLE_NUM, table.getNumber());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pager);

        final int tableNum = getIntent().getIntExtra(EXTRA_TABLE_NUM, 0);

        mViewPager = (ViewPager) findViewById(R.id.order_view_pager);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm){

            @Override
            public int getCount() {
                TableController tc = TableController.getInstance(OrderPagerActivity.this);
                return tc.getTable(tableNum).getOrders().size();
            }

            @Override
            public Fragment getItem(int position) {
                return OrderFragment.newInstance(tableNum, position + 1);
            }
        });

        // default to first order on open
        mViewPager.setCurrentItem(0);
    }
}
