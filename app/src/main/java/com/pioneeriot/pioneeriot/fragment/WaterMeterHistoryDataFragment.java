package com.pioneeriot.pioneeriot.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pioneeriot.pioneeriot.constant.Constant;
import com.pioneeriot.pioneeriot.utils.LogUtils;
import com.pioneeriot.pioneeriot.network.NetClient;
import com.pioneeriot.pioneeriot.network.NetWork;
import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.widget.RecyclerViewDivider;
import com.pioneeriot.pioneeriot.utils.SharedPreferencesUtils;
import com.pioneeriot.pioneeriot.constant.SmartWaterSupply;
import com.pioneeriot.pioneeriot.utils.TimeUtils;
import com.pioneeriot.pioneeriot.utils.ViewUtils;
import com.pioneeriot.pioneeriot.adapter.WaterMeterHistoryDataCardAdapter;
import com.pioneeriot.pioneeriot.adapter.WaterMeterHistoryDataListAdapter;
import com.pioneeriot.pioneeriot.bean.WaterMeterCommitInformation;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 智慧水务平台的查询水表历史数据页面
 * Created by Li Yuliang on 2017/12/29.
 *
 * @author LiYuliang
 * @version 2017/12/29
 */

public class WaterMeterHistoryDataFragment extends BaseFragment {

    private Context context;
    private Activity activity;
    private EditText etMeterId;
    private TextView tvDate, tvBeginTime, tvEndTime;
    private RecyclerView recyclerViewHistoryInformation;
    private WaterMeterHistoryDataListAdapter waterMeterHistoryDataListAdapter;
    private WaterMeterHistoryDataCardAdapter waterMeterHistoryDataCardAdapter;
    private LinearLayout llSingleDate, llDateInterval, llListTitle;
    private List<WaterMeterCommitInformation.Data> dataList;
    private String fieldName, fieldValue;
    private RefreshDefaultData refreshDefaultData;
    private RecyclerViewDivider listDivider, cardDivider;
    private View viewSpace;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_watermeter_history_data, container, false);
        context = getContext();
        activity = getActivity();
        etMeterId = view.findViewById(R.id.et_deviceId);
        etMeterId.addTextChangedListener(textWatcher);
        ImageView ivDeleteMeterId = view.findViewById(R.id.iv_deleteDeviceId);
        ivDeleteMeterId.setOnClickListener(onClickListener);
        dataList = new ArrayList<>();

        recyclerViewHistoryInformation = view.findViewById(R.id.recyclerView_historyInformation);
        //垂直线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewHistoryInformation.setLayoutManager(linearLayoutManager);
        waterMeterHistoryDataListAdapter = new WaterMeterHistoryDataListAdapter(dataList);
        waterMeterHistoryDataCardAdapter = new WaterMeterHistoryDataCardAdapter(dataList);

        listDivider = new RecyclerViewDivider(context, LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(context, R.color.gray_slight));
        cardDivider = new RecyclerViewDivider(context, LinearLayoutManager.HORIZONTAL, 10, ContextCompat.getColor(context, R.color.white));

        llSingleDate = view.findViewById(R.id.ll_singleDate);
        llDateInterval = view.findViewById(R.id.ll_date_interval);
        llListTitle = view.findViewById(R.id.ll_listTitle);

        viewSpace = view.findViewById(R.id.view_space);

        fieldName = (String) SharedPreferencesUtils.getInstance().getData(getString(R.string.fieldName), "");
        fieldValue = String.valueOf((int) SharedPreferencesUtils.getInstance().getData(getString(R.string.fieldValue), -1));

        if ((boolean) SharedPreferencesUtils.getInstance().getData("Show_Single_Date", true)) {
            llSingleDate.setVisibility(View.VISIBLE);
            llDateInterval.setVisibility(View.GONE);
        } else {
            llSingleDate.setVisibility(View.GONE);
            llDateInterval.setVisibility(View.VISIBLE);
        }

        refreshDefaultData = new RefreshDefaultData();
        view.findViewById(R.id.btn_search_meter).setOnClickListener(onClickListener);
        view.findViewById(R.id.iv_lastMonth).setOnClickListener(onClickListener);
        view.findViewById(R.id.iv_nextMonth).setOnClickListener(onClickListener);
        view.findViewById(R.id.iv_lastDay).setOnClickListener(onClickListener);
        view.findViewById(R.id.iv_nextDay).setOnClickListener(onClickListener);
        tvDate = view.findViewById(R.id.tv_date);
        tvBeginTime = view.findViewById(R.id.tv_beginTime);
        tvEndTime = view.findViewById(R.id.tv_endTime);
        tvDate.setOnClickListener(onClickListener);
        tvDate.addTextChangedListener(textWatcher);
        tvBeginTime.setOnClickListener(onClickListener);
        tvBeginTime.addTextChangedListener(textWatcher);
        tvEndTime.setOnClickListener(onClickListener);
        tvEndTime.addTextChangedListener(textWatcher);
        tvDate.setText(String.format(getString(R.string.date), TimeUtils.getCurrentDate()));
        tvBeginTime.setText(String.format(getString(R.string.date), TimeUtils.getCurrentDate()));
        tvEndTime.setText(String.format(getString(R.string.date), TimeUtils.getCurrentDate()));
        //注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction("RefreshDefaultData");
        context.registerReceiver(refreshDefaultData, filter);
        return view;
    }

    /**
     * 注册广播用于通知查询页面刷新默认值
     */
    public class RefreshDefaultData extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("RefreshDefaultData".equals(intent.getAction())) {
                initDefaultData();
            }
        }
    }

    private void initDefaultData() {
        //时间模式
        boolean showSingleDate = (boolean) SharedPreferencesUtils.getInstance().getData("Show_Single_Date", SmartWaterSupply.SHOW_SINGLE_DATE);
        if (showSingleDate) {
            llSingleDate.setVisibility(View.VISIBLE);
            llDateInterval.setVisibility(View.GONE);
        } else {
            llSingleDate.setVisibility(View.GONE);
            llDateInterval.setVisibility(View.VISIBLE);
        }
        showListOrCard();
    }

    public void setMeterId(String meterId) {
        etMeterId.setText(meterId);
    }

    @Override
    public void onDestroyView() {
        if (refreshDefaultData != null) {
            try {
                context.unregisterReceiver(refreshDefaultData);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        super.onDestroyView();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            refreshData();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 清空视图
     */
    private void clearView() {
        //清空列表视图
        if (dataList != null) {
            dataList.clear();
            showListOrCard();
        }
    }

    /**
     * 注册广播用于请求数据
     */
    public class GetMeterIdChartReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getString(R.string.Put_MeterId_Chart).equals(intent.getAction()) && intent.getExtras() != null) {
                String meterId = intent.getExtras().getString("meter_id");
                etMeterId.setText(meterId);
                ViewUtils.setCharSequence(etMeterId.getText());
            }
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_deleteDeviceId:
                    etMeterId.setText("");
                    break;
                case R.id.tv_date:
                    selectData((TextView) v);
                    break;
                case R.id.tv_beginTime:
                    selectData((TextView) v);
                    break;
                case R.id.tv_endTime:
                    selectData((TextView) v);
                    break;
                case R.id.btn_search_meter:
                    refreshData();
                    break;
                case R.id.iv_lastMonth:
                    changeDate(1, -1);
                    break;
                case R.id.iv_nextMonth:
                    changeDate(1, 1);
                    break;
                case R.id.iv_lastDay:
                    changeDate(3, -1);
                    break;
                case R.id.iv_nextDay:
                    changeDate(3, 1);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 改变日期（加减一天或一个月），修改日期显示并刷新数据
     *
     * @param mode  模式（年、月、日）
     * @param value 负数向前/正数向后
     */
    private void changeDate(int mode, int value) {
        String date = tvDate.getText().toString().substring(0, 10);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (mode) {
            case 0:
                //当前时间加上value年，负数为向前value年
                calendar.add(Calendar.YEAR, value);
                break;
            case 1:
                //当前时间加上value月，负数为向前value月
                calendar.add(Calendar.MONTH, value);
                break;
            case 2:
                //当前时间加上value星期，负数为向前value星期
                calendar.add(Calendar.WEEK_OF_YEAR, value);
                break;
            case 3:
                //当前时间加上value天，负数为向前value天
                calendar.add(Calendar.DAY_OF_YEAR, value);
                break;
            default:
                break;
        }
        calendar.getTime();
        //如果获得的时间大于当前时间则保持日期不变（主要针对向后的日期）
        Date currentDate = new Date();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(currentDate);
        //利用compareTo比较两者，大于返回1，小于返回-1，等于返回0
        if (calendar.compareTo(calendar1) != 1) {
            String newDate = DateFormat.format("yyyy-MM-dd", calendar).toString();
            tvDate.setText(String.format(getString(R.string.date), newDate));
        }
    }

    /**
     * 当表号、图表标题、日期发生改变或者点击刷新按钮时执行的方法
     */
    private void refreshData() {
        clearView();
        String meterId = etMeterId.getText().toString();
        if (meterId.length() == Constant.METER_ID_LENGTH) {
            etMeterId.clearFocus();
            hideSoftInputFromWindow();
            searchMeterAllInformation(meterId);
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInputFromWindow() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 查询一只表指定时间的所有提交记录
     *
     * @param meterId 水表编号
     */
    private void searchMeterAllInformation(final String meterId) {
        String beginTime, endTime;
        if ((boolean) SharedPreferencesUtils.getInstance().getData("Show_Single_Date", true)) {
            beginTime = tvDate.getText().toString();
            endTime = tvDate.getText().toString();
        } else {
            beginTime = tvBeginTime.getText().toString();
            endTime = tvEndTime.getText().toString();
        }
        showLoadingDialog(context, "数据查询中，请稍后", true);
        HashMap<String, String> params = new HashMap<>(5);
        params.put("fieldName", fieldName);
        params.put("fieldValue", fieldValue);
        params.put("meterId", meterId);
        params.put("beginTime", beginTime);
        params.put("endTime", endTime);
        Call<WaterMeterCommitInformation> waterMeterCommitInformationCall = NetClient.getInstances(NetClient.getBaseUrl(NetWork.SERVER_HOST_MAIN, NetWork.SERVER_PORT_MAIN, NetWork.PROJECT_MAIN)).getNjMeterApi().searchMeterCommitDataInformation(params);
        waterMeterCommitInformationCall.enqueue(mCallbackWaterCommitInformation);
    }

    private Callback<WaterMeterCommitInformation> mCallbackWaterCommitInformation = new Callback<WaterMeterCommitInformation>() {
        @Override
        public void onResponse(@NotNull Call<WaterMeterCommitInformation> call, @NotNull Response<WaterMeterCommitInformation> response) {
            cancelDialog();
            LogUtils.d("retrofit", response.toString());
            if (response.isSuccessful()) {
                WaterMeterCommitInformation waterMeterCommitInformation = response.body();
                if (waterMeterCommitInformation == null) {
                    showToast("查询失败，返回值异常");
                } else {
                    if (Constant.SUCCESS.equals(waterMeterCommitInformation.getResult())) {
                        //对象中拿到集合
                        dataList.clear();
                        dataList.addAll(waterMeterCommitInformation.getData());
                        if (dataList != null && dataList.size() == 0) {
                            showToast("暂无符合要求的数据");
                        }
                        showListOrCard();
                    } else {
                        showToast("查询失败");
                    }
                }
            } else {
                showToast("查询失败，返回值异常");
            }
        }

        @Override
        public void onFailure(@NotNull Call<WaterMeterCommitInformation> call, @NotNull Throwable throwable) {
            cancelDialog();
            showToast("查询失败，" + throwable.getMessage());
        }
    };

    private void showListOrCard() {
        recyclerViewHistoryInformation.removeItemDecoration(listDivider);
        recyclerViewHistoryInformation.removeItemDecoration(cardDivider);
        if ((boolean) SharedPreferencesUtils.getInstance().getData("Show_Data_List", SmartWaterSupply.SHOW_DATA_LIST)) {
            recyclerViewHistoryInformation.addItemDecoration(listDivider);
            recyclerViewHistoryInformation.setAdapter(waterMeterHistoryDataListAdapter);
            waterMeterHistoryDataListAdapter.notifyDataSetChanged();
            llListTitle.setVisibility(View.VISIBLE);
            viewSpace.setVisibility(View.GONE);
        } else {
            recyclerViewHistoryInformation.addItemDecoration(cardDivider);
            recyclerViewHistoryInformation.setAdapter(waterMeterHistoryDataCardAdapter);
            waterMeterHistoryDataCardAdapter.notifyDataSetChanged();
            llListTitle.setVisibility(View.GONE);
            viewSpace.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 弹出日期选择框Dialog并设置日期和星期
     *
     * @param tv 文本控件
     */
    private void selectData(final TextView tv) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dataPickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(year, monthOfYear, dayOfMonth);
            String date = DateFormat.format("yyyy-MM-dd", calendar).toString();
            tv.setText(String.format(getString(R.string.date), date));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        DatePicker datePicker = dataPickerDialog.getDatePicker();
        datePicker.setMaxDate(new Date(System.currentTimeMillis()).getTime());
        dataPickerDialog.show();
    }
}
