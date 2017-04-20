package pooop.android.sidedish;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class EditMenuFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private EditMenuAdapter mEditMenuAdapter;
    private Button mNewMenuItemButton;

    private MenuController mMenuController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        getActivity().setTitle("Manage Menu");
        View view = inflater.inflate(R.layout.fragment_edit_menu, container, false);
        mMenuController = MenuController.getInstance(getActivity());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.edit_menu_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNewMenuItemButton = (Button) view.findViewById(R.id.new_menu_item_button);
        mNewMenuItemButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Just building the new menu item dialog here in the Java
                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText titleInput = new EditText(getActivity());
                titleInput.setHint("Set Title here.");
                final EditText priceInput = new EditText(getActivity());
                priceInput.setHint("Set Price here.");

                layout.addView(titleInput);
                layout.addView(priceInput);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Set Title and Price of New Menu Item");
                builder.setView(layout);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double price = Double.valueOf(String.valueOf(priceInput.getText()));
                        String title = String.valueOf(titleInput.getText());

                        mMenuController.addMenuItem(title, price);

                        updateEditMenuScreen();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        updateEditMenuScreen();

        return view;
    }

    private void updateEditMenuScreen(){

        // Populate the list of orders
        if(mEditMenuAdapter == null){
            mEditMenuAdapter = new EditMenuAdapter(mMenuController.getMenu());
            mRecyclerView.setAdapter(mEditMenuAdapter);
        }
        else mEditMenuAdapter.setMenuItems(mMenuController.getMenu());

        // Not very pretty but if a menu item is edited or created, just update the dataset
        // (it's probably small anyway... unless this is the cheesecake factory. That menu is huge)
        mEditMenuAdapter.notifyDataSetChanged();

    }

    private class EditMenuAdapter extends RecyclerView.Adapter<MenuItemHolder> {

        private List<SideDishMenuItem> mMenu;

        public EditMenuAdapter(List<SideDishMenuItem> menu){
            mMenu = menu;
        }

        public void setMenuItems(List<SideDishMenuItem> menu){
            mMenu = menu;
        }

        @Override
        public int getItemCount() {
            return mMenu.size();
        }

        @Override
        public MenuItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new MenuItemHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(MenuItemHolder holder, int position) {
            holder.bind(mMenu.get(position));
        }
    }

    private class MenuItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mMenuItemTitle;
        private TextView mMenuItemPrice;

        private SideDishMenuItem mItem;

        private boolean mDeleteItemFlag;

        public MenuItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_edit_menu_item, parent, false));

            mMenuItemTitle = (TextView) itemView.findViewById(R.id.edit_menu_item_title_text_view);
            mMenuItemPrice = (TextView) itemView.findViewById(R.id.edit_menu_item_price_text_view);

            mDeleteItemFlag = false;

            itemView.setOnClickListener(this);
        }

        public void bind(SideDishMenuItem item) {
            mItem = item;

            mMenuItemTitle.setText(mItem.getTitle());
            mMenuItemPrice.setText(String.format("%.2f", mItem.getPrice()));
        }

        @Override
        public void onClick(View v) {
            // Just building the edit menu item dialog here in the Java
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText titleInput = new EditText(getActivity());
            titleInput.setHint("Set New Title here.");
            final EditText priceInput = new EditText(getActivity());
            priceInput.setHint("Set New Price here.");

            final CheckBox deleteItem = new CheckBox(getActivity());
            deleteItem.setText("Delete Menu Item");
            deleteItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Toggle the flag
                    mDeleteItemFlag = !mDeleteItemFlag;
                }
            });

            layout.addView(titleInput);
            layout.addView(priceInput);
            layout.addView(deleteItem);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Set Title and Price of Menu Item");
            builder.setView(layout);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(mDeleteItemFlag){
                        mMenuController.deleteMenuItem(mItem);
                        mDeleteItemFlag = false;
                    }
                    else {
                        double newPrice = Double.valueOf(String.valueOf(priceInput.getText()));
                        String newTitle = String.valueOf(titleInput.getText());
                        String oldTitle = mItem.getTitle();

                        mMenuController.editMenuItem(oldTitle, newTitle, newPrice);
                    }

                    updateEditMenuScreen();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDeleteItemFlag = false; // just in case
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }
}
