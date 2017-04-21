package pooop.android.sidedish;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private StatisticsAdapter mStatisticsAdapter;
    private Button mStartDateButton;
    private Button mEndDateButton;
    private TextView mTotalMoneyMadeTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        getActivity().setTitle("Statistics");

        final Calendar today = Calendar.getInstance();
        String todayYear  = String.valueOf(today.get(Calendar.YEAR) % 100);
        String todayMonth = String.valueOf(today.get(Calendar.MONTH) + 1);
        String todayDay   = String.valueOf(today.get(Calendar.DAY_OF_MONTH));

        mStartDateButton = (Button) view.findViewById(R.id.statistics_start_date_button);
        mStartDateButton.setText(todayMonth + "/" + todayDay + "/" + todayYear);
        mStartDateButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                DatePickerDialog startDatePicker = new DatePickerDialog(getActivity());
                startDatePicker.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
                startDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String startYear  = String.valueOf(year % 100);
                        String startMonth = String.valueOf(month + 1);
                        String startDay   = String.valueOf(dayOfMonth);

                        mStartDateButton.setText(startMonth + "/" + startDay + "/" + startYear);
                    }
                });
                startDatePicker.show();
            }
        });

        mEndDateButton = (Button) view.findViewById(R.id.statistics_end_date_button);
        mEndDateButton.setText(todayMonth + "/" + todayDay + "/" + todayYear);
        mEndDateButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                DatePickerDialog endDatePicker = new DatePickerDialog(getActivity());
                endDatePicker.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
                endDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String endYear  = String.valueOf(year % 100);
                        String endMonth = String.valueOf(month + 1);
                        String endDay   = String.valueOf(dayOfMonth);

                        mEndDateButton.setText(endMonth + "/" + endDay + "/" + endYear);
                    }
                });
                endDatePicker.show();
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.statistics_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTotalMoneyMadeTextView = (TextView) view.findViewById(R.id.statistics_total_money_text_view);

        updateStatistics();

        return view;
    }

    private void updateStatistics(){

        /* TEST DATA */
        ArrayList<Statistic> testlist = new ArrayList<>();
        for(int i=0; i<10; i++) testlist.add(new Statistic());
        /* TEST DATA */

        // Populate the list of orders
        if(mStatisticsAdapter== null){
            mStatisticsAdapter = new StatisticsAdapter(testlist);
            mRecyclerView.setAdapter(mStatisticsAdapter);
        }
        else mStatisticsAdapter.setStats(testlist);

        double totalMoney = 0.0;

        for(int i=0; i<testlist.size(); i++){
            totalMoney += testlist.get(i).getMoney();
        }
        mTotalMoneyMadeTextView.setText("$ " + String.valueOf(totalMoney));

        mStatisticsAdapter.notifyDataSetChanged();
    }

    private class StatisticsAdapter extends RecyclerView.Adapter<StatisticHolder>{

        List<Statistic> mStats;

        public StatisticsAdapter(ArrayList<Statistic> stats){
            mStats = stats;
        }

        public void setStats(List<Statistic> stats){
            mStats = stats;
        }

        @Override
        public StatisticHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new StatisticHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(StatisticHolder holder, int position) {
            holder.bind(mStats.get(position));
        }

        @Override
        public int getItemCount() {
            return mStats.size();
        }
    }

    private class StatisticHolder extends RecyclerView.ViewHolder{

        Statistic mStat;

        private TextView mItemTitleTextView;
        private TextView mItemNumSoldTextView;
        private TextView mItemMoneyMadeTextView;

        public StatisticHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_statistics_item, parent, false));

            mItemTitleTextView      = (TextView) itemView.findViewById(R.id.statistics_menu_item_title_text_view);
            mItemNumSoldTextView    = (TextView) itemView.findViewById(R.id.statistics_number_sold_text_view);
            mItemMoneyMadeTextView  = (TextView) itemView.findViewById(R.id.statistics_money_made_text_view);
        }

        public void bind(Statistic stat){
            mStat = stat;

            mItemTitleTextView.setText(stat.getTitle());
            mItemNumSoldTextView.setText(String.valueOf(stat.getNumSold()));
            mItemMoneyMadeTextView.setText(String.valueOf(stat.getMoney()));
        }
    }
}
