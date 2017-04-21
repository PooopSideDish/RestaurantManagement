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

    private MenuController mMenuController;

    private int mStartYear;
    private int mStartMonth;
    private int mStartDay;
    private int mEndYear;
    private int mEndMonth;
    private int mEndDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        getActivity().setTitle("Statistics");

        mMenuController = MenuController.getInstance(getActivity());

        final Calendar today = Calendar.getInstance();
        mStartYear  = today.get(Calendar.YEAR);
        mStartMonth = today.get(Calendar.MONTH);
        mStartDay   = today.get(Calendar.DAY_OF_MONTH);
        mEndYear    = mStartYear;
        mEndMonth   = mStartMonth;
        mEndDay     = mStartDay;

        String todayYear  = String.valueOf(mStartYear % 100);
        String todayMonth = String.valueOf(mStartMonth + 1);
        String todayDay   = String.valueOf(mStartDay);

        mStartDateButton = (Button) view.findViewById(R.id.statistics_start_date_button);
        mStartDateButton.setText(todayMonth + "/" + todayDay + "/" + todayYear);
        mStartDateButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                DatePickerDialog startDatePicker = new DatePickerDialog(getActivity());
                startDatePicker.updateDate(mStartYear, mStartMonth, mStartDay);
                startDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mStartYear  = year;
                        mStartMonth = month;
                        mStartDay   = dayOfMonth;

                        String startYear  = String.valueOf(year % 100);
                        String startMonth = String.valueOf(month + 1);
                        String startDay   = String.valueOf(dayOfMonth);

                        mStartDateButton.setText(startMonth + "/" + startDay + "/" + startYear);

                        updateStatistics();
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
                endDatePicker.updateDate(mEndYear, mEndMonth, mEndDay);
                endDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mEndYear  = year;
                        mEndMonth = month;
                        mEndDay   = dayOfMonth;

                        String endYear  = String.valueOf(year % 100);
                        String endMonth = String.valueOf(month + 1);
                        String endDay   = String.valueOf(dayOfMonth);

                        mEndDateButton.setText(endMonth + "/" + endDay + "/" + endYear);

                        updateStatistics();
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

        ArrayList<Statistic> stats = mMenuController.getStatistics(mStartYear, mStartMonth, mStartDay,
                    mEndYear, mEndMonth, mEndDay);

        // Populate the list of orders
        if(mStatisticsAdapter== null){
            mStatisticsAdapter = new StatisticsAdapter(stats);
            mRecyclerView.setAdapter(mStatisticsAdapter);
        }
        else mStatisticsAdapter.setStats(stats);

        double totalMoney = 0.0;

        for(int i=0; i<stats.size(); i++){
            totalMoney += stats.get(i).getMoney();
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

            mItemTitleTextView.setText(stat.getName());
            mItemNumSoldTextView.setText(String.valueOf(stat.getNumSold()));
            mItemMoneyMadeTextView.setText(String.valueOf(stat.getMoney()));
        }
    }
}
