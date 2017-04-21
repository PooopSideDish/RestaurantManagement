package pooop.android.sidedish;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class KitchenFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private QueueAdapter mQueueAdapter;
    private OrderController mOrderController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kitchen, container, false);

        getActivity().setTitle("Kitchen View");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.kitchen_queue_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mOrderController = OrderController.getInstance(getActivity());

        updateKitchenQueue();

        return view;
    }

    /* Updates the order list */
    private void updateKitchenQueue(){

        if(mQueueAdapter == null){
            mQueueAdapter = new QueueAdapter();
            mRecyclerView.setAdapter(mQueueAdapter);
        }
        else {
            mQueueAdapter.updateQueue();
        }

        mQueueAdapter.notifyDataSetChanged();
    }

    private class QueueAdapter extends RecyclerView.Adapter<QueueItemHolder> {

        List<SideDishMenuItem> mQueue;

        public QueueAdapter(){
            mQueue = mOrderController.getQueue();
        }

        public void updateQueue(){
            mQueue = mOrderController.getQueue();
        }

        @Override
        public QueueItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new QueueItemHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(QueueItemHolder holder, int position) {
            holder.bind(mQueue.get(position));
        }

        @Override
        public int getItemCount() {
            return mQueue.size();
        }
    }

    private class QueueItemHolder extends RecyclerView.ViewHolder{

        private SideDishMenuItem mItem;

        private TextView mOrderNumberTextView;
        private TextView mMenuItemTextView;
        private TextView mMenuItemCommentsTextView;
        private Button mItemStatusButton;

        public QueueItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_queue_item, parent, false));

            mOrderNumberTextView = (TextView) itemView.findViewById(R.id.kitchen_queue_order_number_text_view);
            mMenuItemTextView    = (TextView) itemView.findViewById(R.id.kitchen_queue_menu_item_text_view);
            mMenuItemCommentsTextView = (TextView) itemView.findViewById(R.id.kitchen_queue_item_status_text_view);

            mItemStatusButton    = (Button)   itemView.findViewById(R.id.kitchen_queue_update_status_button);
            mItemStatusButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    // Update the order within the database
                    Order order = mOrderController.getOrderByNumber(mItem.getOrderNumber());
                    order.incrementItemStatus(mItem);
                    mOrderController.updateOrder(order);

                    // Progress item status locally for the screen
                    mItem.progressStatus();

                    // Update the text
                    mItemStatusButton.setText(mItem.getStatusString());

                    updateKitchenQueue();
                }
            });
        }

        public void bind(SideDishMenuItem item){

            mItem = item;

            mOrderNumberTextView.setText(String.valueOf(item.getOrderNumber()));
            mMenuItemTextView.setText(item.getTitle());
            mItemStatusButton.setText(item.getStatusString());
            mMenuItemCommentsTextView .setText(item.getComment());
        }
    }
}
