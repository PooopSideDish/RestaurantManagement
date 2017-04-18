package pooop.android.sidedish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class OrderPagerActivity extends AppCompatActivity {

    public static final String EXTRA_TABLE_NUM = "pooop.android.sidedish.table_num";

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private FloatingActionButton mAddOrderButton;
    private FloatingActionButton mSubmitAllOrdersButton;
    private Table mTable;

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
        TableController tc = TableController.getInstance(OrderPagerActivity.this);
        mTable = tc.getTable(tableNum);

        mViewPager = (ViewPager) findViewById(R.id.order_view_pager);

        FragmentManager fm = getSupportFragmentManager();
        mPagerAdapter = new PagerAdapter(fm);
        mViewPager.setAdapter(mPagerAdapter);

        // default to first order on open
        mViewPager.setCurrentItem(0);

        // FABs handle new orders and submitting orders
        mAddOrderButton = (FloatingActionButton) findViewById(R.id.add_order_floating_button);
        mAddOrderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Need to add a blank order to the table, and page over to the blank order
                mPagerAdapter.addPage();
                mViewPager.setCurrentItem(mTable.getNumOrders() - 1);
            }
        });

        mSubmitAllOrdersButton = (FloatingActionButton) findViewById(R.id.submit_order_floating_button);
        mSubmitAllOrdersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });
    }

    private class PagerAdapter extends FragmentStatePagerAdapter{

        public PagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount() {
            int size = mTable.getNumOrders();

            if(size <= 0){
                mTable.addOrder(new Order());
                return 1;
            }
            return size;
        }

        @Override
        public Fragment getItem(int position) {
            return OrderFragment.newInstance(mTable.getNumber(), position + 1);
        }

        /* Adds a blank order to the table and adds a new page to the ViewPager to display it */
        public void addPage(){
            mTable.addOrder(new Order());
            notifyDataSetChanged();
        }
    }
}
