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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class OrderPagerActivity extends AppCompatActivity {

    public static final String EXTRA_TABLE_NUM = "pooop.android.sidedish.table_num";

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private FloatingActionButton mAddOrderButton;
    private FloatingActionButton mSubmitOrderButton;
    private FloatingActionButton mDeleteOrderButton;
    private Table mTable;
    private TableController mTableController;
    private OrderController mOrderController;

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
        mTableController = TableController.getInstance(OrderPagerActivity.this);
        mTable = mTableController.getTable(tableNum);

        Log.d("LOG:", "Started Order Pager using table #: " + mTable.getNumber());

        mOrderController = OrderController.getInstance(this);

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

        mSubmitOrderButton = (FloatingActionButton) findViewById(R.id.submit_order_floating_button);
        mSubmitOrderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int curIndex = mPagerAdapter.getCurrentItem();
                mOrderController.submitOrderToQueue(mTable.getOrderByIndex(curIndex));

                Toast.makeText(getApplicationContext(),
                        "Order has been submitted to kitchen!",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        mDeleteOrderButton = (FloatingActionButton) findViewById(R.id.delete_order_floating_button);
        mDeleteOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // TODO: add warning dialog here to confirm deletion

                // Get the order's index from the PagerAdapter
                int curIndex = mViewPager.getCurrentItem();
                mTableController.removeOrderFromTable(curIndex, mTable);
                mTable.removeOrder(curIndex);

                // Too tired to slog through stack exchange and figure out how to properly remove
                // a page so here I'm actually just resetting the whole thing. It works great!
                mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
                mViewPager.setAdapter(mPagerAdapter);

                // If divide by zero exception, the table is out of orders
                try {
                    mViewPager.setCurrentItem((curIndex + 1) % mTable.getNumOrders());
                } catch(ArithmeticException e){
                    mTableController.removeAllOrdersFromTable(mTable);
                    finish();
                }
            }
        });
    }

    private class PagerAdapter extends FragmentStatePagerAdapter{

        private int mCurItem = 0;

        public PagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount() {
            int size = mTable.getNumOrders();
            // Log.d("LOG:", "Table # " + mTable.getNumber() + " has *" + mTable.getNumOrders() + "* orders");

            return (size <= 0 ? 1 : size);
        }

        @Override
        public Fragment getItem(int position) {
            mCurItem = position;
            Log.d("LOG:",String.valueOf(mTable.getNumOrders() <= position));
            return OrderFragment.newInstance(mTable.getNumber(), position, mTable.getNumOrders() <= position);
        }

        /* Adds a blank order to the table and adds a new page to the ViewPager to display it */
        public void addPage(){
            mTable.addOrder(mOrderController.addNewOrder(mTable.getNumber()));
            notifyDataSetChanged();
        }

        public int getCurrentItem(){
            return mCurItem;
        }
    }
}
