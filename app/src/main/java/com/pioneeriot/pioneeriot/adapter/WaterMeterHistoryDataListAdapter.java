package com.pioneeriot.pioneeriot.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.bean.WaterMeterCommitInformation;
import com.pioneeriot.pioneeriot.utils.MathUtils;
import com.pioneeriot.pioneeriot.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by LiYuliang on 2018/1/10 0010.
 * 水表历史数据列表模式适配器
 *
 * @author LiYuliang
 * @version 2018/1/10
 */

public class WaterMeterHistoryDataListAdapter extends RecyclerView.Adapter {

    private List<WaterMeterCommitInformation.Data> list;
    private AppCompatActivity appCompatActivity;

    public WaterMeterHistoryDataListAdapter(AppCompatActivity appCompatActivity, List<WaterMeterCommitInformation.Data> lv) {
        list = lv;
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_watermeter_historydata_list, viewGroup, false);
        ListViewHolder listViewHolder = new ListViewHolder(view);
        listViewHolder.tvTimeReadMeter = view.findViewById(R.id.tv_time_readMeter);
        listViewHolder.tvWaterPressure = view.findViewById(R.id.tv_waterPressure);
        listViewHolder.tvFlowPositive = view.findViewById(R.id.tv_flow_positive);
        listViewHolder.tvFlowRate = view.findViewById(R.id.tv_flowRate);
        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ListViewHolder holder = (ListViewHolder) viewHolder;
        WaterMeterCommitInformation.Data data = list.get(position);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(data.getCreateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        calendar.add(Calendar.HOUR_OF_DAY, 2);
        String newCreateTime = DateFormat.format("HH:mm:ss  dd/MM/yyyy", calendar).toString();
        holder.tvTimeReadMeter.setText(newCreateTime);
        holder.tvFlowPositive.setText(String.format(appCompatActivity.getString(R.string.exampleConsumption), StringUtils.removeZero(String.valueOf(MathUtils.formatDouble(data.getTotal() * 1000, 2)))));
        holder.tvFlowRate.setText(String.format(appCompatActivity.getString(R.string.exampleFlowRate), StringUtils.removeZero(String.valueOf(MathUtils.formatDouble(data.getFlowRate() * 1000, 2)))));
        holder.tvWaterPressure.setText(String.format(appCompatActivity.getString(R.string.example_pressure), StringUtils.removeZero(String.valueOf(MathUtils.formatDouble(data.getPressure() * 100, 2)))));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTimeReadMeter, tvFlowPositive, tvFlowRate, tvWaterPressure;

        private ListViewHolder(View itemView) {
            super(itemView);
        }
    }

}
