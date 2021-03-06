package com.pioneeriot.pioneeriot.adapter;

import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.bean.WaterMeterLastCommitInformation;
import com.pioneeriot.pioneeriot.utils.MathUtils;
import com.pioneeriot.pioneeriot.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 水表最后一次抄表信息扩展列表的适配器
 * Created by LiYuliang on 2017/12/28 0028.
 *
 * @author LiYuliang
 * @version 2018/01/11
 */

public class WaterMeterLastReportAdapter extends BaseExpandableListAdapter {

    private List<WaterMeterLastCommitInformation.Data> dataList;
    private AppCompatActivity appCompatActivity;
    private String[][] titles, details;
    private int pageSize, pageNumber;

    public WaterMeterLastReportAdapter(AppCompatActivity appCompatActivity, List<WaterMeterLastCommitInformation.Data> dataList, int pageSize, int pageNumber) {
        this.appCompatActivity = appCompatActivity;
        this.dataList = dataList;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        titles = getTitlesArray(dataList);
        details = getDetailsArray(dataList);
    }

    /**
     * 标题数组
     *
     * @param dataList 水表最后一条数据列表
     * @return 二维数组
     */
    private String[][] getTitlesArray(List<WaterMeterLastCommitInformation.Data> dataList) {
        // 转换为二维数组
        String[][] details = new String[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            WaterMeterLastCommitInformation.Data data = dataList.get(i);
            //获取对象成员保存至一维数组
            String[] q = {data.getMeterId(), data.getUserName(),
                    String.valueOf(data.getTotal())};
            for (int j = 0; j < details.length; j++) {
                details[i] = q;
            }
        }
        return details;
    }

    /**
     * 详细内容数组
     *
     * @param dataList 水表最后一条数据列表
     * @return 二维数组
     */
    private String[][] getDetailsArray(List<WaterMeterLastCommitInformation.Data> dataList) {
        // 转换为二维数组
        String[][] details = new String[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            WaterMeterLastCommitInformation.Data data = dataList.get(i);
            //获取对象成员保存至一维数组
            String[] q = {data.getMeterId(), "DN" + data.getMeterSize(),
                    String.valueOf(data.getTotal()) + "m³", String.valueOf(data.getRemainTotal()) + "m³",
                    String.valueOf(data.getFlowRate() + "m³/h"), data.getPressure() + "kg/cm²",
                    data.getValveStatus(), data.getValveStatus(), data.getTimeInP(),
                    data.getCreateTime(), data.getVillage() + data.getBuilding() + data.getEntrance() + data.getDoorPlate()};
            for (int j = 0; j < details.length; j++) {
                details[i] = q;
            }
        }
        return details;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return titles.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        return details[groupPosition].length;
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return titles[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return details[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentViewHolder parentViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(appCompatActivity).inflate(R.layout.item_information_water_meter_last, parent, false);
            parentViewHolder = new ParentViewHolder(convertView);
            convertView.setTag(parentViewHolder);
        } else {
            parentViewHolder = (ParentViewHolder) convertView.getTag();
        }
        WaterMeterLastCommitInformation.Data data = dataList.get(groupPosition);
        parentViewHolder.tvNumber.setText(String.valueOf(pageNumber * pageSize + groupPosition + 1));
        parentViewHolder.tvMeterId.setText(data.getMeterId());
        parentViewHolder.tvUserName.setText(data.getUserName());
        parentViewHolder.tvConsumption.setText(StringUtils.removeZero(String.valueOf(MathUtils.formatDouble(data.getTotal() * 1000, 2))));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(appCompatActivity).inflate(R.layout.item_water_meter_data_last, parent, false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        WaterMeterLastCommitInformation.Data data = dataList.get(groupPosition);
        childViewHolder.tvMeterId.setText(data.getMeterId());
        childViewHolder.tvMeterSize.setText(data.getMeterSize());
        childViewHolder.tvConsumptionPositive.setText(String.format(appCompatActivity.getString(R.string.exampleConsumption), StringUtils.removeZero(String.valueOf(MathUtils.formatDouble(data.getTotal() * 1000, 2)))));
        childViewHolder.tvConsumptionReserve.setText(String.format(appCompatActivity.getString(R.string.exampleConsumption), StringUtils.removeZero(String.valueOf(MathUtils.formatDouble(data.getElectric() * 1000, 2)))));
        childViewHolder.tvFlowRate.setText(String.format(appCompatActivity.getString(R.string.exampleFlowRate), StringUtils.removeZero(String.valueOf(MathUtils.formatDouble(data.getFlowRate() * 1000, 2)))));
        childViewHolder.tvPressure.setText(String.format(appCompatActivity.getString(R.string.example_pressure), StringUtils.removeZero(String.valueOf(MathUtils.formatDouble(data.getPressure() * 100, 2)))));
        childViewHolder.tvTemperature.setText(String.format(appCompatActivity.getString(R.string.example_temperature), StringUtils.removeZero(String.valueOf(MathUtils.formatDouble(data.getT1Inp(), 0)))));
        childViewHolder.tvMeterStatus.setText(data.getStatus().replaceAll("\\D", ""));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(data.getTimeInP()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newTimeInP = DateFormat.format("HH:mm:ss  dd/MM/yyyy", calendar).toString();
        childViewHolder.tvCreateTime.setText(newTimeInP);

        try {
            calendar.setTime(format.parse(data.getCreateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        calendar.add(Calendar.HOUR_OF_DAY, 2);
        String newCreateTime = DateFormat.format("HH:mm:ss  dd/MM/yyyy", calendar).toString();
        childViewHolder.tvCommitTime.setText(newCreateTime);
        String address = data.getDoorPlate() + "," + data.getEntrance() + "," + data.getBuilding() + "," + data.getVillage();
        childViewHolder.tvAddress.setText(address);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    private class ParentViewHolder {
        private TextView tvNumber, tvMeterId, tvUserName, tvConsumption;

        private ParentViewHolder(View view) {
            tvNumber = view.findViewById(R.id.tv_number);
            tvMeterId = view.findViewById(R.id.tv_meterId);
            tvUserName = view.findViewById(R.id.tv_userName);
            tvConsumption = view.findViewById(R.id.tv_consumption);
        }
    }

    private class ChildViewHolder {
        private TextView tvMeterId, tvMeterSize, tvConsumptionPositive, tvConsumptionReserve, tvFlowRate, tvPressure, tvTemperature,
                tvMeterStatus, tvCreateTime, tvCommitTime, tvAddress;

        private ChildViewHolder(View view) {
            tvMeterId = view.findViewById(R.id.tv_meter_id);
            tvMeterSize = view.findViewById(R.id.tv_meterSize);
            tvConsumptionPositive = view.findViewById(R.id.tv_consumption_positive);
            tvConsumptionReserve = view.findViewById(R.id.tv_consumption_reserve);
            tvFlowRate = view.findViewById(R.id.tv_flowRate);
            tvPressure = view.findViewById(R.id.tv_pressure);
            tvTemperature = view.findViewById(R.id.tv_temperature);
            tvMeterStatus = view.findViewById(R.id.tv_meterStatus);
            tvCreateTime = view.findViewById(R.id.tv_createTime);
            tvCommitTime = view.findViewById(R.id.tv_commitTime);
            tvAddress = view.findViewById(R.id.tv_address);
        }
    }
}