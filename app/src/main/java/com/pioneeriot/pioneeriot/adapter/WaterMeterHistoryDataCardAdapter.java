package com.pioneeriot.pioneeriot.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.bean.WaterMeterCommitInformation;

import java.util.List;

/**
 * Created by LiYuliang on 2018/1/10 0010.
 * 水表历史数据卡片模式适配器
 *
 * @author LiYuliang
 * @version 2018/1/10
 */

public class WaterMeterHistoryDataCardAdapter extends RecyclerView.Adapter {

    private List<WaterMeterCommitInformation.Data> list;

    public WaterMeterHistoryDataCardAdapter(List<WaterMeterCommitInformation.Data> lv) {
        list = lv;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_watermeter_historydata_card, viewGroup, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        cardViewHolder.tvTimeReadMeter = view.findViewById(R.id.tv_time_readMeter);
        cardViewHolder.tvValveStatus = view.findViewById(R.id.tv_valveStatus);
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
        holder.tvTimeReadMeter.setText(data.getTimeInP());
        holder.tvValveStatus.setText(data.getValveStatus());
        holder.tvWaterPressure.setText(String.valueOf(data.getPressure()));
        holder.tvFlowPositive.setText(String.valueOf(data.getTotal()));
        holder.tvFlowReverse.setText(String.valueOf(data.getElectric()));
        holder.tvFlowRate.setText(String.valueOf(data.getFlowRate()));
        holder.tvMeterStatus.setText(data.getStatus());
        holder.tvTimeCommit.setText(data.getCreateTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTimeReadMeter, tvValveStatus, tvWaterPressure, tvFlowPositive, tvFlowReverse, tvFlowRate, tvMeterStatus, tvTimeCommit;

        private CardViewHolder(View itemView) {
            super(itemView);
        }
    }

}
