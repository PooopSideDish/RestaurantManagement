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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class EditUsersFragment extends Fragment {

    private UserController mUserController;
    private RecyclerView mRecyclerView;
    private EditUserAdapter mEditUserAdapter;
    private Button mNewUserButton;

    private int mNewUserType = 0; // 1 -> Manager, 2 -> Waitstaff, 3 -> Kitchen


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_users, container, false);
        mUserController = UserController.getInstance(getActivity());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.edit_user_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNewUserButton = (Button) view.findViewById(R.id.new_user_button);
        mNewUserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Just building the new user dialog here in the Java
                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText userIDInput = new EditText(getActivity());
                userIDInput.setHint("Set User's ID here.");
                final Spinner userTypeInput = new Spinner(getActivity());
                final String[] types = {"Manager", "Waitstaff", "Kitchen"};
                ArrayAdapter<String> typesAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, types);
                userTypeInput.setAdapter(typesAdapter);
                userTypeInput.setSelection(0);
                userTypeInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mNewUserType = position + 1;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        userTypeInput.setSelection(0);
                    }
                });

                layout.addView(userIDInput);
                layout.addView(userTypeInput);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Set ID and Employee Type of New User");
                builder.setView(layout);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = String.valueOf(userIDInput.getText());
                        mUserController.addUser(id, mNewUserType);

                        updateEditUsersScreen();
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

        updateEditUsersScreen();

        return view;
    }

    private void updateEditUsersScreen(){

        // Populate the list of tables
        if(mEditUserAdapter == null){
            mEditUserAdapter = new EditUserAdapter(mUserController.getUsers());
            mRecyclerView.setAdapter(mEditUserAdapter);
        }
        else mEditUserAdapter.setUsers(mUserController.getUsers());

        // Not very pretty but if a menu item is edited or created, just update the dataset
        // (it's probably small anyway... unless this is the cheesecake factory. That menu is huge)
        mEditUserAdapter.notifyDataSetChanged();

    }

    private class EditUserAdapter extends RecyclerView.Adapter<UserHolder> {

        private List<Employee> mUsers;

        public EditUserAdapter(List<Employee> users){
            mUsers = users;
        }

        public void setUsers(List<Employee> users){
            mUsers = users;
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new UserHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(UserHolder holder, int position) {
            holder.bind(mUsers.get(position));
        }
    }

    private class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mUserID;
        private TextView mUserType;

        private Employee mUser;

        private boolean mDeleteUserFlag;

        public UserHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_user, parent, false));

            mUserID   = (TextView) itemView.findViewById(R.id.user_id_text_view);
            mUserType = (TextView) itemView.findViewById(R.id.user_type_text_view);

            mDeleteUserFlag = false;

            itemView.setOnClickListener(this);
        }

        public void bind(Employee user) {
            mUser = user;

            mUserID.setText(mUser.getID());
            mUserType.setText(mUser.getType());
        }

        @Override
        public void onClick(View v) {
            // Just build all the dialogs in Java! Why not? /s
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText userIDInput = new EditText(getActivity());
            userIDInput.setHint("Set User's ID here.");

            final Spinner userTypeInput = new Spinner(getActivity());
            final String[] types = {"Manager", "Waitstaff", "Kitchen"};
            ArrayAdapter<String> typesAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item, types);
            userTypeInput.setAdapter(typesAdapter);
            userTypeInput.setSelection(0);
            userTypeInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mNewUserType = position + 1;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    userTypeInput.setSelection(0);
                }
            });

            final CheckBox deleteUser = new CheckBox(getActivity());
            deleteUser.setText("Remove User");
            deleteUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Toggle the flag
                    mDeleteUserFlag = !mDeleteUserFlag;
                }
            });

            layout.addView(userIDInput);
            layout.addView(userTypeInput);
            layout.addView(deleteUser);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Set Title and Price of New Menu Item");
            builder.setView(layout);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(mDeleteUserFlag){
                        mUserController.deleteUser(mUser);
                    }
                    else {
                        String id = String.valueOf(userIDInput.getText());
                        mUserController.editUser(mUser.getID(), id, mNewUserType);
                    }

                    updateEditUsersScreen();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDeleteUserFlag = false;
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }
}

