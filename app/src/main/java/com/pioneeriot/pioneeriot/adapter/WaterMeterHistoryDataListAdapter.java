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
 * 水表历史数据列表模式适配器
 *
 * @author LiYuliang
 * @version 2018/1/10
 */

public class WaterMeterHistoryDataListAdapter extends RecyclerView.Adapter {

    private List<WaterMeterCommitInformation.Data> list;

    public WaterMeterHistoryDataListAdapter(List<WaterMeterCommitInformation.Data> lv) {
        list = lv;
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
        holder.tvTimeReadMeter.setText(data.getTimeInP());
        holder.tvWaterPressure.setText(String.valueOf(data.getPressure()));
        holder.tvFlowPositive.setText(String.valueOf(data.getTotal()));
        holder.tvFlowRate.setText(String.valueOf(data.getFlowRate()));
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
