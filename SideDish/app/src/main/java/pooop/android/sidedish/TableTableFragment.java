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
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class TableTableFragment extends Fragment{

    private RecyclerView mTableRecyclerView;
    private TableAdapter mTableAdapter;
    private Button mNewTableButton;
    private Button mEditTableButton;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = layoutInflater.inflate(R.layout.fragment_table_table, container, false);

        mTableRecyclerView = (RecyclerView) view.findViewById(R.id.table_recycler_view);
        mTableRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNewTableButton = (Button) view.findViewById(R.id.new_table_button);
        mNewTableButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(getActivity());

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Set Table's Section");
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TableController tc = TableController.getInstance(getActivity());
                        tc.addTable(input.getText().toString());
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

            mTableNumberText.setText( "Table Number: " + String.valueOf(table.getNumber()));
            mTableSectionText.setText("Section: "      + String.valueOf(table.getSection()));
            mTableStatusText.setText( "Status : "      + table.getStatus());
        }

        @Override
        public void onClick(View v) {
            Intent intent = OrderPagerActivity.newIntent(getActivity(), mTable);
            startActivity(intent);
        }
    }
}
