package pooop.android.sidedish;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class OrderFragment extends Fragment {

    private static final String ARG_ORDER_INDEX  = "order_index";
    private static final String ARG_TABLE_NUMBER = "table_number";

    private Table mTable;
    private Order mOrder;
    private OrderController mOrderController;

    private RecyclerView mRecyclerView;
    private MenuItemAdapter mMenuItemAdapter;
    private TextView mOrderTotal;

    // TODO: Remove order somehow (just remove all items?)

    public static OrderFragment newInstance(int tableNum, int orderIndex, boolean orderIsEmpty){
        Bundle args = new Bundle();
        args.putInt(ARG_TABLE_NUMBER, tableNum);

        // flag if the order is empty with -1
        if(orderIsEmpty) args.putInt(ARG_ORDER_INDEX, -1);
        else args.putInt(ARG_ORDER_INDEX, orderIndex);

        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        int tableNum = getArguments().getInt(ARG_TABLE_NUMBER);
        int orderIndex = getArguments().getInt(ARG_ORDER_INDEX);

        mOrderController = OrderController.getInstance(getActivity());
        mTable = TableController.getInstance(getActivity()).getTable(tableNum);

        // If the table already has orders, get the details from the database
        if(orderIndex > -1){
            mOrder = mTable.getOrder(orderIndex);
        }
        // else make a new order
        else{
            mOrder = mOrderController.addNewOrder(tableNum);
            mTable.addOrder(mOrder);
        }

        // setup view
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.menu_items_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mOrderTotal = (TextView) view.findViewById(R.id.order_total_text_view);
        mOrderTotal.setText(String.valueOf(mOrder.getTotal()));

        updateOrder();

        return view;
    }

    private void updateOrder(){

        // Populate the list of orders
        if(mMenuItemAdapter == null){
            mMenuItemAdapter = new MenuItemAdapter();
            mRecyclerView.setAdapter(mMenuItemAdapter);
        }

        // Update data set to reflect changes
        mOrderTotal.setText(String.format("%.2f", mOrder.getTotal()));
        mMenuItemAdapter.notifyDataSetChanged();
    }

    private class MenuItemAdapter extends RecyclerView.Adapter<MenuItemHolder> {

        private MenuController mMenuController;
        private String [] mMenuItemTitles;

        public MenuItemAdapter(){
            mMenuController = MenuController.getInstance(getActivity());
            mMenuItemTitles = mMenuController.getMenuTitles();
        }

        @Override
        public int getItemCount() {
            return mOrder.getItems().size() + 1; // +1 for the AutoCompleteTextView
        }

        @Override
        public MenuItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new MenuItemHolder(inflater, parent, mMenuItemTitles);
        }

        @Override
        public void onBindViewHolder(MenuItemHolder holder, int position) {
            holder.bind(position, mOrder.getItem(position));
        }

        /* Verify the user's input and set a menu item in the order. If bad input -> do nothing */
        protected void setPosition(int position, String userInput){

            SideDishMenuItem newItem = mMenuController.getMenuItemByName(userInput);

            // Check for valid input
            if(newItem == null) return;

            // Case 1: user is entering a new item
            if(position >= mOrder.getItems().size()){
                mOrder.addItem(newItem);
                mOrderController.addItemToOrder(mOrder.getNumber(), newItem);
            }

            // Case 2: user is editing a preexisting item
            else mOrder.setItem(position, newItem);

            updateOrder();
        }
    }

    private class MenuItemHolder extends RecyclerView.ViewHolder {

        private AutoCompleteTextView mAutoCompleteTextView;
        private TextView mMenuItemPrice;
        private Button mDeleteItemButton;

        private SideDishMenuItem mItem;

        private Integer mPosition;

        public MenuItemHolder(LayoutInflater inflater, ViewGroup parent, String[] menuItemTitles) {
            super(inflater.inflate(R.layout.list_item_order, parent, false));

            mAutoCompleteTextView = (AutoCompleteTextView) itemView
                    .findViewById(R.id.menu_item_auto_complete);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line, menuItemTitles);
            mAutoCompleteTextView.setAdapter(adapter);
            mAutoCompleteTextView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener(){
                @Override
                public void onDismiss() {
                    // Only set position if there was a change in the text
                    String input = String.valueOf(mAutoCompleteTextView.getText());
                    if(mItem == null || !mItem.getTitle().equals(input)){
                        mMenuItemAdapter.setPosition(mPosition, input);
                    }
                }
            });
            mAutoCompleteTextView.setValidator(new AutoCompleteTextView.Validator() {
                @Override
                public boolean isValid(CharSequence text) {
                    return false; // not actually used...
                }
                @Override
                public CharSequence fixText(CharSequence invalidText) {
                    // Adding a new item, do nothing
                    if(mItem == null) return String.valueOf(invalidText);

                    // Editing an item to invalid text, reset to original item's text
                    return mItem.getTitle();
                }
            });

            mMenuItemPrice = (TextView) itemView.findViewById(R.id.menu_item_price_text_view);

            mDeleteItemButton = (Button) itemView.findViewById(R.id.menu_item_delete_button);
            mDeleteItemButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mOrderController.removeItemFromOrder(mOrder.getNumber(), mOrder.getItem(mPosition));
                    mOrder.removeItem(mPosition);
                    updateOrder();
                }
            });
        }

        public void bind(Integer position, SideDishMenuItem item){

            mPosition = position;

            if(item == null) {
                mItem = null;
                mMenuItemPrice.setText("");
                mAutoCompleteTextView.setText("");
                mDeleteItemButton.setClickable(false);
            }
            else {
                mItem = item;
                mMenuItemPrice.setText(String.format("%.2f", mItem.getPrice()));
                mAutoCompleteTextView.setText(mItem.getTitle());
                mDeleteItemButton.setClickable(true);
            }
        }
    }
}
