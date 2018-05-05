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
 * 水表历史数据卡片模式适配器
 *
 * @author LiYuliang
 * @version 2018/1/10
 */

public class WaterMeterHistoryDataCardAdapter extends RecyclerView.Adapter {

    private List<WaterMeterCommitInformation.Data> list;
    private AppCompatActivity appCompatActivity;

    public WaterMeterHistoryDataCardAdapter(AppCompatActivity appCompatActivity, List<WaterMeterCommitInformation.Data> lv) {
        list = lv;
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_watermeter_historydata_card, viewGroup, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        cardViewHolder.tvTimeReadMeter = view.findViewById(R.id.tv_time_readMeter);
        cardViewHolder.tvTemperature = view.findViewById(R.id.tv_temperature);
        cardViewHolder.tvWaterPressure = view.findViewById(R.id.tv_waterPressure);
        cardViewHolder.tvFlowPositive = view.findViewById(R.id.tv_flow_positive);
        cardViewHolder.tvFlowReverse = view.findViewById(R.id.tv_flow_reverse);
        cardViewHolder.tvFlowRate = view.findViewById(R.id.tv_flowRate);
        cardViewHolder.tvMeterStatus = view.findViewById(R.id.tv_meterStatus);
        cardViewHolder.tvTimeCommit = view.findViewById(R.id.tv_time_commit);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CardViewHolder holder = (CardViewHolder) viewHolder;
        WaterMeterCommitInformation.Data data = list.get(position);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(data.getTimeInP()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newTimeInP = DateFormat.format("HH:mm:ss  dd/MM/yyyy", calendar).toString();

        try {
            calendar.setTime(format.parse(data.getCreateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        calendar.add(Calendar.HOUR_OF_DAY, 2);
        String newCreateTime = DateFormat.format("HH:mm:ss  dd/MM/yyyy", calendar).toString();
        holder.tvTimeReadMeter.setText(newTimeInP);
        holder.tvTimeCommit.setText(newCreateTime);
        holder.tvFlowPositive.setText(String.format(appCompatActivity.getString(R.string.exampleConsumption), StringUtils.removeZero(String.valueOf(MathUtils.formatDouble(data.getTotal() * 1000, 2)))));
        holder.tvFlowReverse.setText(String.format(appCompatActivity.getString(R.string.exampleConsumption), StringUtils.removeZero(String.valueOf(MathUtils.formatDouble(data.getElectric() * 1000, 2)))));
        holder.tvFlowRate.setText(String.format(appCompatActivity.getString(R.string.exampleFlowRate), StringUtils.removeZero(String.valueOf(MathUtils.formatDouble(data.getFlowRate() * 1000, 2)))));
        holder.tvWaterPressure.setText(String.format(appCompatActivity.getString(R.string.example_pressure), StringUtils.removeZero(String.valueOf(MathUtils.formatDouble(data.getPressure() * 100, 2)))));
        holder.tvTemperature.setText(String.format(appCompatActivity.getString(R.string.example_temperature), StringUtils.removeZero(String.valueOf(MathUtils.formatDouble(data.getT1Inp(), 1)))));
        holder.tvMeterStatus.setText(data.getStatus().replaceAll("\\D", ""));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTimeReadMeter, tvTemperature, tvWaterPressure, tvFlowPositive, tvFlowReverse, tvFlowRate, tvMeterStatus, tvTimeCommit;

        private CardViewHolder(View itemView) {
            super(itemView);
        }
    }

}
