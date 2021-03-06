package com.pioneeriot.pioneeriot.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.pioneeriot.pioneeriot.activity.AbbreviationActivity;
import com.pioneeriot.pioneeriot.activity.HtmlActivity;
import com.pioneeriot.pioneeriot.activity.MeterStatusCodeActivity;
import com.pioneeriot.pioneeriot.utils.ActivityController;
import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.widget.SegmentControlView;
import com.pioneeriot.pioneeriot.utils.SharedPreferencesUtils;
import com.pioneeriot.pioneeriot.constant.SmartWaterSupply;
import com.pioneeriot.pioneeriot.utils.TimeUtils;
import com.pioneeriot.pioneeriot.activity.UnitOfMeasurementActivity;
import com.pioneeriot.pioneeriot.utils.ViewUtils;
import com.pioneeriot.pioneeriot.activity.LoginActivity;
import com.pioneeriot.pioneeriot.activity.MainActivity;
import com.pioneeriot.pioneeriot.dialog.ChooseMeterSizeDialog;
import com.pioneeriot.pioneeriot.dialog.ChooseRowsPerPageDialog;
import com.pioneeriot.pioneeriot.dialog.ChooseTimeRangeDialog;

/**
 * 智慧水务平台的查询水表数据设置页面
 * Created by Li Yuliang on 2017/12/28.
 *
 * @author LiYuliang
 * @version 2017/12/28
 */

public class SearchDataSettingFragment extends BaseFragment {

    private Context context;
    private MainActivity mainActivity;
    private TextView tvSizeRange, tvTimeRange, tvRows;
    private SegmentControlView scvSearchMode, scvTimeMode, scvShowDataMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_search_data_setting, container, false);
        tvSizeRange = view.findViewById(R.id.tv_sizeRange);
        tvTimeRange = view.findViewById(R.id.tv_timeRange);
        tvRows = view.findViewById(R.id.tv_rows);

        tvSizeRange.setOnClickListener(onClickListener);
        tvTimeRange.setOnClickListener(onClickListener);
        tvRows.setOnClickListener(onClickListener);

        scvSearchMode = view.findViewById(R.id.scv_searchMode);
        scvTimeMode = view.findViewById(R.id.scv_timeMode);
        scvShowDataMode = view.findViewById(R.id.scv_showDataMode);

        (view.findViewById(R.id.ll_settingGuide)).setOnClickListener(onClickListener);
        (view.findViewById(R.id.ll_unit)).setOnClickListener(onClickListener);
        (view.findViewById(R.id.ll_abbreviationIndex)).setOnClickListener(onClickListener);
        (view.findViewById(R.id.ll_meterStatusCode)).setOnClickListener(onClickListener);
        (view.findViewById(R.id.btn_exit)).setOnClickListener(onClickListener);
        initPage();

        tvSizeRange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //发送广播通知查询页面刷新默认值
                Intent intent = new Intent("RefreshDefaultData");
                context.sendBroadcast(intent);
                showToast("Nominal Diameter Ranger:" + "DN" + tvSizeRange.getText());
            }
        });

        tvTimeRange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //发送广播通知查询页面刷新默认值
                Intent intent = new Intent("RefreshDefaultData");
                context.sendBroadcast(intent);
                showToast("Time Range:" + tvTimeRange.getText() + " days");
            }
        });

        tvRows.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //发送广播通知查询页面刷新默认值
                Intent intent = new Intent("RefreshDefaultData");
                context.sendBroadcast(intent);
                showToast("Items Per Page:" + tvRows.getText() + " items");
            }
        });
        scvSearchMode.setOnSegmentChangedListener(onSegmentChangedListenerScvSearchMode);
        scvTimeMode.setOnSegmentChangedListener(onSegmentChangedListenerScvTimeMode);
        scvShowDataMode.setOnSegmentChangedListener(onSegmentChangedListenerScvShowDataMode);
        return view;
    }

    /**
     * 刷新页面
     */
    private void initPage() {
        tvSizeRange.setText(String.format(getString(R.string.range_meterSize_DN),
                SharedPreferencesUtils.getInstance().getData("MeterSize_Min_Default", SmartWaterSupply.DATA_SEARCH_MIN_SIZE),
                SharedPreferencesUtils.getInstance().getData("MeterSize_Max_Default", SmartWaterSupply.DATA_SEARCH_MAX_SIZE)));
        tvTimeRange.setText((String) SharedPreferencesUtils.getInstance().getData("Search_Time_Range", SmartWaterSupply.DATA_SEARCH_TIME_RANGE));
        tvRows.setText((String) SharedPreferencesUtils.getInstance().getData("Rows_Per_Page", SmartWaterSupply.DATA_SEARCH_ROWS_PER_PAGE));
        //查询模式
        boolean searchTypeHasRead = (boolean) SharedPreferencesUtils.getInstance().getData("Default_Search_Type_Has_Read", SmartWaterSupply.DATA_SEARCH_HAS_READ);
        if (searchTypeHasRead) {
            scvSearchMode.setSelectedIndex(0);
        } else {
            scvSearchMode.setSelectedIndex(1);
        }
        //时间模式
        boolean showSingleDate = (boolean) SharedPreferencesUtils.getInstance().getData("Show_Single_Date", SmartWaterSupply.SHOW_SINGLE_DATE);
        if (showSingleDate) {
            scvTimeMode.setSelectedIndex(0);
        } else {
            scvTimeMode.setSelectedIndex(1);
        }
        //数据形式
        boolean showDataList = (boolean) SharedPreferencesUtils.getInstance().getData("Show_Data_List", SmartWaterSupply.SHOW_DATA_LIST);
        if (showDataList) {
            scvShowDataMode.setSelectedIndex(0);
        } else {
            scvShowDataMode.setSelectedIndex(1);
        }
    }

    private SegmentControlView.OnSegmentChangedListener onSegmentChangedListenerScvSearchMode = new SegmentControlView.OnSegmentChangedListener() {
        @Override
        public void onSegmentChanged(int newSelectedIndex) {
            scvSearchMode.setSelectedIndex(newSelectedIndex);
            //是否是查询已抄到
            boolean searchTypeHasRead;
            switch (scvSearchMode.getSelectedIndex()) {
                case 0:
                    searchTypeHasRead = true;
                    break;
                case 1:
                    searchTypeHasRead = false;
                    break;
                default:
                    searchTypeHasRead = SmartWaterSupply.DATA_SEARCH_HAS_READ;
                    break;
            }
            SharedPreferencesUtils.getInstance().saveData("Default_Search_Type_Has_Read", searchTypeHasRead);
            //刷新页面
            initPage();
            //发送广播通知查询页面刷新默认值
            Intent intent = new Intent("RefreshDefaultData");
            context.sendBroadcast(intent);
            if (searchTypeHasRead) {
                showToast("Query Mode:Read");
            } else {
                showToast("Query Mode:Unread");
            }
        }
    };

    private SegmentControlView.OnSegmentChangedListener onSegmentChangedListenerScvTimeMode = new SegmentControlView.OnSegmentChangedListener() {
        @Override
        public void onSegmentChanged(int newSelectedIndex) {
            scvTimeMode.setSelectedIndex(newSelectedIndex);
            //是否是采用单日期模式
            boolean singleDate;
            switch (scvTimeMode.getSelectedIndex()) {
                case 0:
                    singleDate = true;
                    break;
                case 1:
                    singleDate = false;
                    break;
                default:
                    singleDate = SmartWaterSupply.SHOW_SINGLE_DATE;
                    break;
            }
            SharedPreferencesUtils.getInstance().saveData("Show_Single_Date", singleDate);
            //刷新页面
            initPage();
            //发送广播通知查询页面刷新默认值
            Intent intent = new Intent("RefreshDefaultData");
            context.sendBroadcast(intent);
            if (singleDate) {
                showToast("Date Selection Mode:Single Date");
            } else {
                showToast("Date Selection Mode:Double Date");
            }
        }
    };

    private SegmentControlView.OnSegmentChangedListener onSegmentChangedListenerScvShowDataMode = new SegmentControlView.OnSegmentChangedListener() {
        @Override
        public void onSegmentChanged(int newSelectedIndex) {
            scvShowDataMode.setSelectedIndex(newSelectedIndex);
            //数据展示形式
            boolean showDataList;
            switch (scvShowDataMode.getSelectedIndex()) {
                case 0:
                    showDataList = true;
                    break;
                case 1:
                    showDataList = false;
                    break;
                default:
                    showDataList = SmartWaterSupply.SHOW_DATA_LIST;
                    break;
            }
            SharedPreferencesUtils.getInstance().saveData("Show_Data_List", showDataList);
            //刷新页面
            initPage();
            //发送广播通知查询页面刷新默认值
            Intent intent = new Intent("RefreshDefaultData");
            context.sendBroadcast(intent);
            if (showDataList) {
                showToast("Data Display Mode:List");
            } else {
                showToast("Data Display Mode:Card");
            }
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_settingGuide:
                    Intent intent = new Intent(context, HtmlActivity.class);
                    String url = "file:////android_asset/html/searchSettings/index.html";
                    intent.putExtra("title", getString(R.string.SettingInstructions));
                    intent.putExtra("URL", url);
                    startActivity(intent);
                    break;
                case R.id.ll_unit:
                    mainActivity.openActivity(UnitOfMeasurementActivity.class);
                    break;
                case R.id.ll_abbreviationIndex:
                    mainActivity.openActivity(AbbreviationActivity.class);
                    break;
                case R.id.ll_meterStatusCode:
                    mainActivity.openActivity(MeterStatusCodeActivity.class);
                    break;
                case R.id.tv_sizeRange:
                    showSizeRangeDialog();
                    break;
                case R.id.tv_timeRange:
                    showTimeRangeDialog();
                    break;
                case R.id.tv_rows:
                    showRowsPerPageDialog();
                    break;
                case R.id.btn_exit:
                    SharedPreferencesUtils.getInstance().clearData("password");
                    mainActivity.openActivity(LoginActivity.class);
                    ActivityController.finishActivity(mainActivity);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 显示设置查询口径范围的对话框
     */
    private void showSizeRangeDialog() {
        ChooseMeterSizeDialog chooseMeterSizeDialog = new ChooseMeterSizeDialog(getActivity());
        chooseMeterSizeDialog.setCancelable(true);
        EditText etMeterSizeMinDialog = chooseMeterSizeDialog.findViewById(R.id.et_meterSizeMin);
        EditText etMeterSizeMaxDialog = chooseMeterSizeDialog.findViewById(R.id.et_meterSizeMax);
        etMeterSizeMinDialog.setText((String) SharedPreferencesUtils.getInstance().getData("MeterSize_Min_Default", SmartWaterSupply.DATA_SEARCH_MIN_SIZE));
        etMeterSizeMaxDialog.setText((String) SharedPreferencesUtils.getInstance().getData("MeterSize_Max_Default", SmartWaterSupply.DATA_SEARCH_MAX_SIZE));
        ViewUtils.setCharSequence(etMeterSizeMinDialog.getText());
        ViewUtils.setCharSequence(etMeterSizeMaxDialog.getText());
        chooseMeterSizeDialog.setOnDialogClickListener(() -> {
            String minSize = etMeterSizeMinDialog.getText().toString();
            String maxSize = etMeterSizeMaxDialog.getText().toString();
            if (TextUtils.isEmpty(minSize)) {
                showToast("Please enter min Nominal diameter");
                return;
            }
            if (TextUtils.isEmpty(maxSize)) {
                showToast("Please enter max Nominal diameter");
                return;
            }
            //如果最小口径大于最大口径，则自动交换两者的值
            if (Integer.valueOf(minSize) > Integer.valueOf(maxSize)) {
                String temp = minSize;
                minSize = maxSize;
                maxSize = temp;
            }
            SharedPreferencesUtils.getInstance().saveData("MeterSize_Min_Default", minSize);
            SharedPreferencesUtils.getInstance().saveData("MeterSize_Max_Default", maxSize);
            String searchRange = String.format(getString(R.string.range_meterSize_DN), minSize, maxSize);
            if (!searchRange.equals(tvSizeRange.getText().toString())) {
                tvSizeRange.setText(searchRange);
            }
            chooseMeterSizeDialog.dismiss();
        });
        chooseMeterSizeDialog.show();
    }

    /**
     * 显示设置查询时间范围的对话框
     */
    private void showTimeRangeDialog() {
        ChooseTimeRangeDialog chooseTimeRangeDialog = new ChooseTimeRangeDialog(getActivity());
        chooseTimeRangeDialog.setCancelable(true);
        TextView tvCurrentDate = chooseTimeRangeDialog.findViewById(R.id.tv_currentDate);
        EditText etTimeRange = chooseTimeRangeDialog.findViewById(R.id.et_timeRange);
        tvCurrentDate.setText("(" + TimeUtils.getCurrentDate() + ")");
        etTimeRange.setText((String) SharedPreferencesUtils.getInstance().getData("Search_Time_Range", SmartWaterSupply.DATA_SEARCH_TIME_RANGE));
        ViewUtils.setCharSequence(etTimeRange.getText());
        etTimeRange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String timeRange = etTimeRange.getText().toString().trim();
                if (!TextUtils.isEmpty(timeRange) && Integer.valueOf(timeRange) > SmartWaterSupply.DATA_SEARCH_TIME_RANGE_MAX) {
                    charSequence = charSequence.toString().subSequence(0, charSequence.length() - 1);
                    etTimeRange.setText(charSequence);
                    //设置光标在末尾
                    etTimeRange.setSelection(charSequence.length());
                    showToast("The max time range is " + SmartWaterSupply.DATA_SEARCH_TIME_RANGE_MAX + " days");
                }
                if (!TextUtils.isEmpty(timeRange) && Integer.valueOf(timeRange) < SmartWaterSupply.DATA_SEARCH_TIME_RANGE_MIN) {
                    charSequence = charSequence.toString().subSequence(0, charSequence.length() - 1);
                    etTimeRange.setText(charSequence);
                    //设置光标在末尾
                    etTimeRange.setSelection(charSequence.length());
                    showToast("The min time range is " + SmartWaterSupply.DATA_SEARCH_TIME_RANGE_MIN + " days");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        chooseTimeRangeDialog.setOnDialogClickListener(() -> {
            String timeRange = etTimeRange.getText().toString().trim();
            if (TextUtils.isEmpty(timeRange)) {
                showToast("Please enter time range");
                return;
            } else if (Integer.valueOf(timeRange) < SmartWaterSupply.DATA_SEARCH_TIME_RANGE_MIN || Integer.valueOf(timeRange) > SmartWaterSupply.DATA_SEARCH_TIME_RANGE_MAX) {
                showToast("The time range is not valid");
                return;
            }
            SharedPreferencesUtils.getInstance().saveData("Search_Time_Range", timeRange);
            if (!timeRange.equals(tvTimeRange.getText().toString())) {
                tvTimeRange.setText(timeRange);
            }
            chooseTimeRangeDialog.dismiss();
        });
        chooseTimeRangeDialog.show();
    }

    /**
     * 显示设置每页显示条数的对话框
     */
    private void showRowsPerPageDialog() {
        ChooseRowsPerPageDialog chooseRowsPerPageDialog = new ChooseRowsPerPageDialog(getActivity());
        chooseRowsPerPageDialog.setCancelable(true);
        EditText etRows = chooseRowsPerPageDialog.findViewById(R.id.et_rows);
        etRows.setText((String) SharedPreferencesUtils.getInstance().getData("Rows_Per_Page", SmartWaterSupply.DATA_SEARCH_TIME_RANGE));
        ViewUtils.setCharSequence(etRows.getText());
        etRows.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String rows = etRows.getText().toString().trim();
                if (!TextUtils.isEmpty(rows) && Integer.valueOf(rows) > SmartWaterSupply.DATA_SEARCH_ROWS_PER_PAGE_MAX) {
                    charSequence = charSequence.toString().subSequence(0, charSequence.length() - 1);
                    etRows.setText(charSequence);
                    //设置光标在末尾
                    etRows.setSelection(charSequence.length());
                    showToast("The max items in one page is " + SmartWaterSupply.DATA_SEARCH_ROWS_PER_PAGE_MAX);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        chooseRowsPerPageDialog.setOnDialogClickListener(() -> {
            String rows = etRows.getText().toString().trim();
            if (TextUtils.isEmpty(rows)) {
                showToast("Please enter the number of items per page");
                return;
            } else if (Integer.valueOf(rows) < SmartWaterSupply.DATA_SEARCH_ROWS_PER_PAGE_MIN || Integer.valueOf(rows) > SmartWaterSupply.DATA_SEARCH_ROWS_PER_PAGE_MAX) {
                showToast("The number of items per page is " + SmartWaterSupply.DATA_SEARCH_ROWS_PER_PAGE_MIN + "~" + SmartWaterSupply.DATA_SEARCH_ROWS_PER_PAGE_MAX);
                return;
            }
            SharedPreferencesUtils.getInstance().saveData("Rows_Per_Page", rows);
            if (!rows.equals(tvRows.getText().toString())) {
                tvRows.setText(rows);
            }
            chooseRowsPerPageDialog.dismiss();
        });
        chooseRowsPerPageDialog.show();
    }

}
