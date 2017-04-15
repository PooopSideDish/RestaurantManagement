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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class EditMenuFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private EditMenuAdapter mEditMenuAdapter;
    private Button mNewMenuItemButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_edit_menu, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.edit_menu_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNewMenuItemButton = (Button) view.findViewById(R.id.new_menu_item_button);
        mNewMenuItemButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Just building the custom dialog here in the Java
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

                        MenuController mc = MenuController.getInstance(getActivity());
                        mc.addMenuItem(title, price);

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

        // Populate the list of tables
        if(mEditMenuAdapter == null){
            MenuController mc = MenuController.getInstance(getActivity());
            mEditMenuAdapter = new EditMenuAdapter(mc.getMenu());
            mRecyclerView.setAdapter(mEditMenuAdapter);
        }

        // Not very pretty but if a menu item is edited or created, just update the dataset
        // (it's probably small anyway... unless this is the cheesecake factory. That menu is huge)
        mEditMenuAdapter.notifyDataSetChanged();

    }

    private class EditMenuAdapter extends RecyclerView.Adapter<MenuItemHolder> {

        private List<SideDishMenuItem> mMenu;

        public EditMenuAdapter(List<SideDishMenuItem> menu){
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
