package pooop.android.sidedish;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class OrderFragment extends Fragment {

    private static final String ARG_ORDER_NUMBER = "order_number";
    private static final String ARG_TABLE_NUMBER = "table_number";

    private Order mOrder;

    private RecyclerView mRecyclerView;
    private MenuItemAdapter mMenuItemAdapter;
    private TextView mOrderTotal;


    public static OrderFragment newInstance(int tableNum, int orderNum){
        Bundle args = new Bundle();
        args.putInt(ARG_TABLE_NUMBER, tableNum);
        args.putInt(ARG_ORDER_NUMBER, orderNum);

        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        int tableNum = getArguments().getInt(ARG_TABLE_NUMBER);
        int orderNum = getArguments().getInt(ARG_ORDER_NUMBER);

        mOrder = TableController.getInstance(getActivity()).getTable(tableNum).getOrder(orderNum);

        View view = inflater.inflate(R.layout.fragment_order, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.menu_items_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderTotal = (TextView) view.findViewById(R.id.order_total_text_view);

        mOrderTotal.setText(String.valueOf(mOrder.getTotal()));

        updateOrder();

        return view;
    }

    private void updateOrder(){

        // Populate the list of tables
        if(mMenuItemAdapter == null){
            mMenuItemAdapter = new MenuItemAdapter(mOrder.getItems());
            mRecyclerView.setAdapter(mMenuItemAdapter);
        }
        else mMenuItemAdapter.setMenuItems(mOrder.getItems());

        // When returning to this fragment from some other screen, the list of tables will most
        // likely need to be updated.
        // Keeping it simple right now and just updating the entire RecyclerView set.
        mMenuItemAdapter.notifyDataSetChanged();

    }

    private class MenuItemAdapter extends RecyclerView.Adapter<MenuItemHolder> {

        private List<SideDishMenuItem> mItems;

        public MenuItemAdapter(List<SideDishMenuItem> items){
            mItems = items;
        }

        public void setMenuItems(List<SideDishMenuItem> items){
            mItems = items;
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        @Override
        public MenuItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new MenuItemHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(MenuItemHolder holder, int position) {
            holder.bind(mItems.get(position));
        }
    }

    private class MenuItemHolder extends RecyclerView.ViewHolder {

        private TextView mMenuItemTitle;
        private TextView mMenuItemPrice;

        private SideDishMenuItem mItem;

        public MenuItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_order, parent, false));

            mMenuItemTitle = (TextView) itemView.findViewById(R.id.menu_item_title_text_view);
            mMenuItemPrice = (TextView) itemView.findViewById(R.id.menu_item_price_text_view);
        }

        public void bind(SideDishMenuItem item){
            mItem = item;

            mMenuItemTitle.setText(mItem.getTitle());
            mMenuItemPrice.setText(String.valueOf(mItem.getPrice()));
        }
    }
}
