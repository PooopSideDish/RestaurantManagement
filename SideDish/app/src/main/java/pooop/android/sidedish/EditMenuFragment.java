package pooop.android.sidedish;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EditMenuFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private Button mNewMenuItemButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_edit_menu, container, false);

        // mRecyclerView = (RecyclerView) view.findViewById(R.id.edit_menu_recycler_view);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNewMenuItemButton = (Button) view.findViewById(R.id.new_menu_item_button);

        updateEditMenuScreen();

        return view;

    }

    private void updateEditMenuScreen(){
        //  TODO implement recycler view
    }
}
