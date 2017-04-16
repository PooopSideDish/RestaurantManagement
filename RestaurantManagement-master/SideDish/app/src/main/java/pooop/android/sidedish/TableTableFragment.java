package pooop.android.sidedish;

import android.content.DialogInterface;
import android.content.Intent;
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

public class TableTableFragment extends Fragment{

    private TableController mTableController;

    private RecyclerView mTableRecyclerView;
    private TableAdapter mTableAdapter;
    private Button mNewTableButton;
    private Button mEditTableButton;

    private boolean mDeleteTableFlag;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = layoutInflater.inflate(R.layout.fragment_table_table, container, false);

        mTableController = TableController.getInstance(getActivity());
        mDeleteTableFlag = false;

        mTableRecyclerView = (RecyclerView) view.findViewById(R.id.table_recycler_view);
        mTableRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNewTableButton = (Button) view.findViewById(R.id.new_table_button);
        mNewTableButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(getActivity());
                input.setHint("Enter Table Section");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Set Table's Section");
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTableController.addTable(input.getText().toString());
                        updateTableTable();
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

        mEditTableButton = (Button) view.findViewById(R.id.edit_table_button);
        mEditTableButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Building the custom dialog in the Java to save time...
                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText tableNumInput = new EditText(getActivity());
                tableNumInput.setHint("Enter Table Number to Edit");
                final EditText sectionInput = new EditText(getActivity());
                sectionInput.setHint("Enter New Table Section");

                final CheckBox deleteTable = new CheckBox(getActivity());
                deleteTable.setText("Delete Menu Item");
                deleteTable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // Toggle the flag
                        mDeleteTableFlag= !mDeleteTableFlag;
                    }
                });

                layout.addView(tableNumInput);
                layout.addView(sectionInput);
                layout.addView(deleteTable);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Edit Table");
                builder.setView(layout);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int tableNum = Integer.parseInt(String.valueOf(tableNumInput.getText()));

                        if(mDeleteTableFlag){
                            mTableController.deleteTable(tableNum);
                            mDeleteTableFlag = false;
                        }
                        else {
                            String newSection = String.valueOf(sectionInput.getText());
                            mTableController.editTable(tableNum, newSection);
                        }

                        updateTableTable();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDeleteTableFlag = false;
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        updateTableTable();

        return view;
    }

    private void updateTableTable(){
        List<Table> tables = TableController.getInstance(getActivity()).getTables();

        // Populate the list of tables
        if(mTableAdapter == null){
            mTableAdapter = new TableAdapter(tables);
            mTableRecyclerView.setAdapter(mTableAdapter);
        }
        else mTableAdapter.setTables(tables);

        // When returning to this fragment from some other screen, the list of tables will most
        // likely need to be updated.
        // Keeping it simple right now and just updating the entire RecyclerView set.
        mTableAdapter.notifyDataSetChanged();

    }

    private class TableAdapter extends RecyclerView.Adapter<TableHolder> {

        private List<Table> mTables;

        public TableAdapter(List<Table> tables){
            mTables = tables;
        }

        public void setTables(List<Table> tables){
            mTables = tables;
        }

        @Override
        public int getItemCount() {
            return mTables.size();
        }

        @Override
        public TableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new TableHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(TableHolder holder, int position) {
            Table table = mTables.get(position);
            holder.bind(table);
        }
    }

    private class TableHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTableNumberText;
        private TextView mTableSectionText;
        private TextView mTableStatusText;

        private Table mTable;

        public TableHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_table, parent, false));

            mTableNumberText  = (TextView) itemView.findViewById(R.id.table_number_text_view);
            mTableSectionText = (TextView) itemView.findViewById(R.id.table_section_text_view);
            mTableStatusText  = (TextView) itemView.findViewById(R.id.table_status_text_view);

            itemView.setOnClickListener(this);
        }

        public void bind(Table table){
            mTable = table;

            mTableNumberText.setText(String.valueOf(table.getNumber()));
            mTableSectionText.setText(String.valueOf(table.getSection()));
            mTableStatusText.setText(table.getStatus());
        }

        @Override
        public void onClick(View v) {
            Intent intent = OrderPagerActivity.newIntent(getActivity(), mTable);
            startActivity(intent);
        }
    }
}
