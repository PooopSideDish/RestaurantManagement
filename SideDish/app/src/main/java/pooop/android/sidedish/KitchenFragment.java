package pooop.android.sidedish;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KitchenFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // View view = inflater.inflate(R.layout.fragment_view_kitchen, container, false);

        // Need at least order controller

        return null;
        // return view;
    }

    /* Updates the order list */
    private void updateKitchenQueue(){

    }

    // TODO: Implement RecyclerView List as order list

    private class OrderAdapter extends RecyclerView.Adapter<OrderHolder> {

        @Override
        public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(OrderHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private class OrderHolder extends RecyclerView.ViewHolder{

        public OrderHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_user, parent, false));
        }
    }
}
