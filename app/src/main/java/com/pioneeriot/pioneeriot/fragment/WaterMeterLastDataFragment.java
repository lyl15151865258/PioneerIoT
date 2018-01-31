package com.pioneeriot.pioneeriot.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.pioneeriot.pioneeriot.constant.Constant;
import com.pioneeriot.pioneeriot.adapter.FilterConditionAdapter;
import com.pioneeriot.pioneeriot.utils.LogUtils;
import com.pioneeriot.pioneeriot.widget.MarqueeTextView;
import com.pioneeriot.pioneeriot.network.NetClient;
import com.pioneeriot.pioneeriot.network.NetWork;
import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.widget.SegmentControlView;
import com.pioneeriot.pioneeriot.utils.SharedPreferencesUtils;
import com.pioneeriot.pioneeriot.constant.SmartWaterSupply;
import com.pioneeriot.pioneeriot.adapter.TagAdapter;
import com.pioneeriot.pioneeriot.utils.ViewUtils;
import com.pioneeriot.pioneeriot.adapter.WaterMeterLastReportAdapter;
import com.pioneeriot.pioneeriot.activity.ChooseHierarchyActivity;
import com.pioneeriot.pioneeriot.activity.MainActivity;
import com.pioneeriot.pioneeriot.bean.FilterCondition;
import com.pioneeriot.pioneeriot.bean.WaterMeterLastCommitInformation;
import com.pioneeriot.pioneeriot.dialog.ChooseFilterDialog;
import com.pioneeriot.pioneeriot.dialog.ChooseMeterSizeDialog;
import com.pioneeriot.pioneeriot.flowtaglayout.FlowTagLayout;
import com.pioneeriot.pioneeriot.flowtaglayout.OnTagSelectListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 智慧水务平台的查询水表末次数据页面
 * Created by Li Yuliang on 2017/12/26.
 *
 * @author LiYuliang
 * @version 2017/12/26
 */

public class WaterMeterLastDataFragment extends BaseFragment {

    private Context context;
    private MainActivity mainActivity;
    private SegmentControlView scvSearchMode;
    private TextView tvShowAdvancedFilter, tvSizeRange, tvCurrentHierarchy, tvMountPage;
    private Button tvExpandAllFilter;
    private MarqueeTextView tvConditions;
    private EditText etTimeRange, etCurrentPage, etFilterValue;
    private ListView lvFilter;
    private LinearLayout llAdvancedFilter;
    private List<FilterCondition> filterConditionList;
    private List<WaterMeterLastCommitInformation.Data> waterMeterLastCommitInformationDataList;
    private String fieldName, fieldValue;
    private ExpandableListView lvWaterMeterLast;
    private FilterConditionAdapter filterConditionAdapter;
    private WaterMeterLastReportAdapter waterMeterLastReportAdapter;
    private int pageSize, totalPage, currentPage;
    private int itemContentFiltering, itemComparisonOperators, itemValueFiltering;
    private RefreshDefaultData refreshDefaultData;
    private List<String> spinnerContentFiltering, spinnerComparisonOperators, spinnerMeterStatus, spinnerValveStatus;
    private FlowTagLayout fltComparisonOperators, fltValueFiltering;
    private TagAdapter<String> contentFilteringAdapter, comparisonOperatorsAdapter, valueFilteringAdapter;
    private final int MODIFY = 1, ADD = 2, maxDecimalDigits = 2;
    public final int SEARCH_MODE_NORMAL = 0, SEARCH_MODE_CHANGE_SEGMENT = 1, SEARCH_MODE_LAST = 2, SEARCH_MODE_NEXT = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watermeter_last_data, container, false);
        super.onCreate(savedInstanceState);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        scvSearchMode = view.findViewById(R.id.scv_searchMode);
        tvSizeRange = view.findViewById(R.id.tv_size_range);
        tvCurrentHierarchy = view.findViewById(R.id.tv_current_hierarchy);
        tvMountPage = view.findViewById(R.id.tv_mount_page);
        etTimeRange = view.findViewById(R.id.et_time_range);
        etCurrentPage = view.findViewById(R.id.et_currentPage);
        llAdvancedFilter = view.findViewById(R.id.ll_advancedFilter);
        lvFilter = view.findViewById(R.id.lv_filter);
        refreshDefaultData = new RefreshDefaultData();
        filterConditionList = new ArrayList<>();
        filterConditionAdapter = new FilterConditionAdapter(context, filterConditionList);
        lvFilter.setAdapter(filterConditionAdapter);
        lvFilter.setOnItemClickListener(onItemClickListener);
        lvFilter.setOnItemLongClickListener(onItemLongClickListener);
        itemContentFiltering = -1;
        itemComparisonOperators = -1;
        itemValueFiltering = -1;

        spinnerContentFiltering = Arrays.asList(getResources().getStringArray(R.array.spinner_content_filtering));
        spinnerComparisonOperators = Arrays.asList(getResources().getStringArray(R.array.spinner_comparison_operators));
        spinnerMeterStatus = Arrays.asList(getResources().getStringArray(R.array.spinner_meter_status));
        spinnerValveStatus = Arrays.asList(getResources().getStringArray(R.array.spinner_valve_status));

        tvConditions = (view.findViewById(R.id.tv_conditions));
        tvShowAdvancedFilter = (view.findViewById(R.id.tv_showAdvancedFilter));
        tvShowAdvancedFilter.setOnClickListener(onClickListener);

        (view.findViewById(R.id.btn_addFilter)).setOnClickListener(onClickListener);
        (view.findViewById(R.id.btn_deleteAllFilter)).setOnClickListener(onClickListener);

        tvExpandAllFilter = (view.findViewById(R.id.btn_expandAllFilter));
        tvExpandAllFilter.setOnClickListener(onClickListener);

        //显示默认查询设置
        initDefaultData();
        tvCurrentHierarchy.setOnClickListener(onClickListener);
        tvSizeRange.setOnClickListener(onClickListener);
        scvSearchMode.setOnSegmentChangedListener(onSegmentChangedListener);
        etTimeRange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String timeRange = etTimeRange.getText().toString().trim();
                etCurrentPage.setText("1");
                if (!TextUtils.isEmpty(timeRange) && Integer.valueOf(timeRange) > SmartWaterSupply.DATA_SEARCH_TIME_RANGE_MAX) {
                    charSequence = charSequence.toString().subSequence(0, charSequence.length() - 1);
                    etTimeRange.setText(charSequence);
                    //设置光标在末尾
                    etTimeRange.setSelection(charSequence.length());
                    showToast("The time range is 1~31");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etCurrentPage.setText("1");
        (view.findViewById(R.id.iv_lastPage)).setOnClickListener(onClickListener);
        (view.findViewById(R.id.iv_nextPage)).setOnClickListener(onClickListener);
        lvWaterMeterLast = view.findViewById(R.id.lv_water_meter_last);
        //Group点击展开
        lvWaterMeterLast.setOnGroupClickListener(onGroupClickListener);
        lvWaterMeterLast.setOnChildClickListener(onChildClickListener);
        lvWaterMeterLast.setOnItemLongClickListener(onItemLongClickListener);
        //只展开一个Group
        lvWaterMeterLast.setOnGroupExpandListener((groupPosition) -> {
            for (int i = 0; i < waterMeterLastReportAdapter.getGroupCount(); i++) {
                if (groupPosition != i) {
                    lvWaterMeterLast.collapseGroup(i);
                }
            }
        });

        fieldName = (String) SharedPreferencesUtils.getInstance().getData(getString(R.string.fieldName), "");
        fieldValue = String.valueOf((int) SharedPreferencesUtils.getInstance().getData(getString(R.string.fieldValue), -1));

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

    private SegmentControlView.OnSegmentChangedListener onSegmentChangedListener = new SegmentControlView.OnSegmentChangedListener() {
        @Override
        public void onSegmentChanged(int newSelectedIndex) {
            scvSearchMode.setSelectedIndex(newSelectedIndex);
            etCurrentPage.setText("1");
        }
    };

    private ExpandableListView.OnGroupClickListener onGroupClickListener = (expandableListView, view, i, l) -> false;

    private ExpandableListView.OnChildClickListener onChildClickListener = (expandableListView, view, i, i1, l) -> false;

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (adapterView.getId()) {
                case R.id.lv_filter:
                    FilterCondition filterCondition = filterConditionList.get(i);
                    itemContentFiltering = filterCondition.getContentFilteringPosition();
                    itemComparisonOperators = filterCondition.getComparisonOperatorsPosition();
                    itemValueFiltering = filterCondition.getValueFilteringPosition();
                    showChooseItemDialog(MODIFY, i);
                    break;
                default:
                    break;
            }
        }
    };

    private AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (adapterView.getId()) {
                case R.id.lv_water_meter_last:
                    //切换Fragment到历史数据页面
                    mainActivity.moveToSecondPage(waterMeterLastCommitInformationDataList.get(i).getMeterId());
                    break;
                case R.id.lv_filter:
                    filterConditionList.remove(i);
                    filterConditionAdapter.notifyDataSetChanged();
                    if (filterConditionList.size() != 0) {
                        tvConditions.setText(String.format(getString(R.string.conditions), filterConditionList.get(0).getContentFiltering(),
                                filterConditionList.get(0).getComparisonOperators(), filterConditionList.get(0).getValueFiltering(), filterConditionList.size()));
                    } else {
                        filterConditionList.clear();
                        filterConditionAdapter.notifyDataSetChanged();
                        tvConditions.setText("");
                        etCurrentPage.setText("1");
                        changeListVisibility(false, false);
                    }
                    break;
                default:
                    break;
            }
            //避免触发点击事件
            return true;
        }
    };

    private View.OnClickListener onClickListener = (v) -> {
        switch (v.getId()) {
            case R.id.tv_current_hierarchy:
                //层级选择
                Intent intent = new Intent(context, ChooseHierarchyActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_size_range:
                //修改口径范围
                showSizeRangeDialog();
                break;
            case R.id.tv_showAdvancedFilter:
                if (llAdvancedFilter.getVisibility() == View.VISIBLE) {
                    llAdvancedFilter.setVisibility(View.GONE);
                    tvShowAdvancedFilter.setText(getString(R.string.ShowAdvancedFilter));
                } else {
                    llAdvancedFilter.setVisibility(View.VISIBLE);
                    tvShowAdvancedFilter.setText(getString(R.string.HideAdvancedFilter));
                }
                break;
            case R.id.btn_addFilter:
                if (filterConditionList.size() == 5) {
                    showToast(getString(R.string.Only5FiltersCanBeAdded));
                } else {
                    showChooseItemDialog(ADD, 0);
                }
                break;
            case R.id.btn_deleteAllFilter:
                filterConditionList.clear();
                filterConditionAdapter.notifyDataSetChanged();
                tvConditions.setText("");
                etCurrentPage.setText("1");
                changeListVisibility(false, false);
                break;
            case R.id.btn_expandAllFilter:
                if (lvFilter.getVisibility() == View.VISIBLE) {
                    changeListVisibility(false, true);
                } else if (lvFilter.getVisibility() == View.GONE) {
                    changeListVisibility(true, false);
                }
                break;
            case R.id.iv_lastPage:
                //上一页
                if (currentPage > 1) {
                    currentPage--;
                    searchWaterMeter(SEARCH_MODE_LAST);
                } else {
                    showToast("Already to the first page");
                }
                break;
            case R.id.iv_nextPage:
                //下一页
                if (currentPage < totalPage) {
                    currentPage++;
                    searchWaterMeter(SEARCH_MODE_NEXT);
                } else {
                    showToast("Already to the last page");
                }
                break;
            default:
                break;
        }
    };

    /**
     * 展开或收起高级筛选条件
     */
    private void changeListVisibility(boolean showList, boolean showText) {
        if (showList) {
            if (filterConditionList.size() != 0) {
                lvFilter.setVisibility(View.VISIBLE);
            } else {
                lvFilter.setVisibility(View.GONE);
            }
            tvExpandAllFilter.setText(R.string.CollapseAll);
        } else {
            lvFilter.setVisibility(View.GONE);
            tvExpandAllFilter.setText(R.string.ExpandAll);
        }
        if (showText) {
            if (filterConditionList.size() != 0) {
                tvConditions.setVisibility(View.VISIBLE);
            } else {
                tvConditions.setVisibility(View.GONE);
            }
        } else {
            tvConditions.setVisibility(View.GONE);
        }
    }

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
                showToast("Please enter the minimum nominal diameter");
                return;
            }
            if (TextUtils.isEmpty(maxSize)) {
                showToast("Please enter the maximum nominal diameter");
                return;
            }
            //如果最小口径大于最大口径，则自动交换两者的值
            if (Integer.valueOf(minSize) > Integer.valueOf(maxSize)) {
                String temp = minSize;
                minSize = maxSize;
                maxSize = temp;
            }
            SharedPreferencesUtils.getInstance().saveData("MeterSize_Min", minSize);
            SharedPreferencesUtils.getInstance().saveData("MeterSize_Max", maxSize);
            String searchRange = String.format(getString(R.string.range_meterSize_DN), minSize, maxSize);
            //当查询范围发生改变时，标记
            if (!searchRange.equals(tvSizeRange.getText().toString())) {
                tvSizeRange.setText(searchRange);
                etCurrentPage.setText("1");
            }
            chooseMeterSizeDialog.dismiss();
        });
        chooseMeterSizeDialog.show();
    }

    /**
     * 显示筛选条件对话框
     */
    private void showChooseItemDialog(int mode, int position) {
        switch (mode) {
            case ADD:
                initItem();
                break;
            case MODIFY:
                itemContentFiltering = filterConditionList.get(position).getContentFilteringPosition();
                itemComparisonOperators = filterConditionList.get(position).getComparisonOperatorsPosition();
                break;
            default:
                break;
        }

        ChooseFilterDialog chooseFilterDialog = new ChooseFilterDialog(getActivity(), viewGroup);
        chooseFilterDialog.setCancelable(false);
        FlowTagLayout fltContentFiltering = chooseFilterDialog.findViewById(R.id.flt_content_filtering);
        fltComparisonOperators = chooseFilterDialog.findViewById(R.id.flt_comparison_operators);
        fltValueFiltering = chooseFilterDialog.findViewById(R.id.flt_value_filtering);
        etFilterValue = chooseFilterDialog.findViewById(R.id.et_filterValue);

        contentFilteringAdapter = new TagAdapter<>(context);
        comparisonOperatorsAdapter = new TagAdapter<>(context);
        valueFilteringAdapter = new TagAdapter<>(context);

        //比较项
        fltContentFiltering.setAdapter(contentFilteringAdapter);
        fltContentFiltering.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        contentFilteringAdapter.onlyAddAll(spinnerContentFiltering);
        if (itemContentFiltering != -1) {
            fltContentFiltering.setSelectedOption(itemContentFiltering);
        } else {
            fltContentFiltering.clearAllOption();
        }
        //比较符
        fltComparisonOperators.setAdapter(comparisonOperatorsAdapter);
        fltComparisonOperators.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        comparisonOperatorsAdapter.onlyAddAll(spinnerComparisonOperators);
        if (itemComparisonOperators != -1) {
            fltComparisonOperators.setSelectedOption(itemComparisonOperators);
        } else {
            fltComparisonOperators.clearAllOption();
        }
        //比较值
        fltValueFiltering.setAdapter(valueFilteringAdapter);
        fltValueFiltering.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        switch (mode) {
            case ADD:
                etFilterValue.setVisibility(View.VISIBLE);
                fltValueFiltering.setVisibility(View.GONE);
                valueFilteringAdapter.onlyAddAll(spinnerMeterStatus);
                if (itemValueFiltering != -1) {
                    fltValueFiltering.setSelectedOption(itemValueFiltering);
                } else {
                    fltValueFiltering.clearAllOption();
                }
                break;
            case MODIFY:
                switch (filterConditionList.get(position).getContentFilteringPosition()) {
                    case 0:
                    case 1:
                        fltComparisonOperators.getChildAt(0).setVisibility(View.GONE);
                        fltComparisonOperators.getChildAt(2).setVisibility(View.GONE);
                        break;
                    case 2:
                    case 3:
                    case 4:
                        fltComparisonOperators.getChildAt(3).setVisibility(View.GONE);
                        break;
                    case 5:
                        valueFilteringAdapter.clearAndAddAll(spinnerMeterStatus);
                        fltComparisonOperators.getChildAt(0).setVisibility(View.GONE);
                        fltComparisonOperators.getChildAt(2).setVisibility(View.GONE);
                        fltComparisonOperators.getChildAt(3).setVisibility(View.GONE);
                        break;
                    case 6:
                        valueFilteringAdapter.clearAndAddAll(spinnerValveStatus);
                        fltComparisonOperators.getChildAt(0).setVisibility(View.GONE);
                        fltComparisonOperators.getChildAt(2).setVisibility(View.GONE);
                        fltComparisonOperators.getChildAt(3).setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                if (filterConditionList.get(position).isHasCustomValue()) {
                    etFilterValue.setVisibility(View.VISIBLE);
                    fltValueFiltering.setVisibility(View.GONE);
                    etFilterValue.setText(filterConditionList.get(position).getValueFiltering());
                    ViewUtils.setCharSequence(etFilterValue.getText());
                } else {
                    etFilterValue.setVisibility(View.GONE);
                    fltValueFiltering.setVisibility(View.VISIBLE);
                    itemValueFiltering = filterConditionList.get(position).getValueFilteringPosition();
                    if (itemValueFiltering != -1) {
                        fltValueFiltering.setSelectedOption(itemValueFiltering);
                    } else {
                        fltValueFiltering.clearAllOption();
                    }
                }
                break;
            default:
                break;
        }

        fltContentFiltering.setOnTagSelectListener(onTagSelectListener);
        fltComparisonOperators.setOnTagSelectListener(onTagSelectListener);
        fltValueFiltering.setOnTagSelectListener(onTagSelectListener);

        chooseFilterDialog.setOnDialogClickListener(new ChooseFilterDialog.OnDialogClickListener() {
            @Override
            public void onCancelClick() {
                initItem();
                chooseFilterDialog.dismiss();
            }

            @Override
            public void onClearClick() {
                contentFilteringAdapter.notifyDataSetChanged();
                comparisonOperatorsAdapter.notifyDataSetChanged();
                valueFilteringAdapter.notifyDataSetChanged();
                initItem();
            }

            @Override
            public void onSaveClick() {
                FilterCondition filterCondition = new FilterCondition();
                switch (itemContentFiltering) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        if (itemContentFiltering == -1 || itemComparisonOperators == -1 || TextUtils.isEmpty(etFilterValue.getText().toString().trim())) {
                            showToast("Please refine the filter criteria");
                            return;
                        }
                        filterCondition.setContentFiltering((String) contentFilteringAdapter.getItem(itemContentFiltering));
                        filterCondition.setComparisonOperators((String) comparisonOperatorsAdapter.getItem(itemComparisonOperators));
                        filterCondition.setValueFiltering(etFilterValue.getText().toString().trim());
                        filterCondition.setHasCustomValue(true);
                        break;
                    case 5:
                    case 6:
                        if (itemContentFiltering == -1 || itemComparisonOperators == -1 || itemValueFiltering == -1) {
                            showToast("Please refine the filter criteria");
                            return;
                        }
                        filterCondition.setContentFiltering((String) contentFilteringAdapter.getItem(itemContentFiltering));
                        filterCondition.setComparisonOperators((String) comparisonOperatorsAdapter.getItem(itemComparisonOperators));
                        filterCondition.setValueFiltering((String) valueFilteringAdapter.getItem(itemValueFiltering));
                        filterCondition.setHasCustomValue(false);
                        break;
                    default:
                        break;
                }
                filterCondition.setContentFilteringPosition(itemContentFiltering);
                filterCondition.setComparisonOperatorsPosition(itemComparisonOperators);
                filterCondition.setValueFilteringPosition(itemValueFiltering);
                switch (mode) {
                    case ADD:
                        filterConditionList.add(filterCondition);
                        filterConditionAdapter.notifyDataSetChanged();
                        initItem();
                        break;
                    case MODIFY:
                        filterConditionList.remove(position);
                        filterConditionList.add(position, filterCondition);
                        filterConditionAdapter.notifyDataSetChanged();
                        initItem();
                        break;
                    default:
                        break;
                }
                etCurrentPage.setText("1");
                tvConditions.setText(String.format(getString(R.string.conditions), filterConditionList.get(0).getContentFiltering(),
                        filterConditionList.get(0).getComparisonOperators(), filterConditionList.get(0).getValueFiltering(), filterConditionList.size()));
                changeListVisibility(true, false);
                chooseFilterDialog.dismiss();
            }
        });
        chooseFilterDialog.show();
    }

    /**
     * 初始化标签选择的位置
     */
    private void initItem() {
        itemContentFiltering = -1;
        itemComparisonOperators = -1;
        itemValueFiltering = -1;
    }

    /**
     * 标签选中监听
     */
    private OnTagSelectListener onTagSelectListener = new OnTagSelectListener() {
        @Override
        public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
            switch (parent.getId()) {
                case R.id.flt_content_filtering:
                    comparisonOperatorsAdapter.notifyDataSetChanged();
                    if (selectedList != null && selectedList.size() != 0) {
                        itemContentFiltering = selectedList.get(0);
                        etFilterValue.removeTextChangedListener(textWatcherConsumption);
                        etFilterValue.removeTextChangedListener(textWatcherPressure);
                        etFilterValue.removeTextChangedListener(textWatcherFlowRate);
                        switch (itemContentFiltering) {
                            case 0:
                                etFilterValue.setVisibility(View.VISIBLE);
                                etFilterValue.setHint(R.string.EnterTheMeterSNOrAPartOfIt);
                                etFilterValue.setText("");
                                etFilterValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                                etFilterValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                fltValueFiltering.setVisibility(View.GONE);
                                fltComparisonOperators.getChildAt(0).setVisibility(View.GONE);
                                fltComparisonOperators.getChildAt(2).setVisibility(View.GONE);
                                break;
                            case 1:
                                etFilterValue.setVisibility(View.VISIBLE);
                                etFilterValue.setHint(R.string.EnterAUsernameOrAPartOfIt);
                                etFilterValue.setText("");
                                etFilterValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                                etFilterValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                fltValueFiltering.setVisibility(View.GONE);
                                fltComparisonOperators.getChildAt(0).setVisibility(View.GONE);
                                fltComparisonOperators.getChildAt(2).setVisibility(View.GONE);
                                break;
                            case 2:
                                etFilterValue.setVisibility(View.VISIBLE);
                                etFilterValue.setHint(R.string.EnterTotalFlowValue);
                                etFilterValue.setText("");
                                etFilterValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
                                etFilterValue.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
                                etFilterValue.addTextChangedListener(textWatcherConsumption);
                                fltValueFiltering.setVisibility(View.GONE);
                                fltComparisonOperators.getChildAt(3).setVisibility(View.GONE);
                                break;
                            case 3:
                                etFilterValue.setVisibility(View.VISIBLE);
                                etFilterValue.setHint(R.string.EnterPressureValue);
                                etFilterValue.setText("");
                                etFilterValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                                etFilterValue.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
                                etFilterValue.addTextChangedListener(textWatcherPressure);
                                fltValueFiltering.setVisibility(View.GONE);
                                fltComparisonOperators.getChildAt(3).setVisibility(View.GONE);
                                break;
                            case 4:
                                etFilterValue.setVisibility(View.VISIBLE);
                                etFilterValue.setHint(R.string.EnterFlowRateValue);
                                etFilterValue.setText("");
                                etFilterValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                                etFilterValue.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
                                etFilterValue.addTextChangedListener(textWatcherFlowRate);
                                fltValueFiltering.setVisibility(View.GONE);
                                fltComparisonOperators.getChildAt(3).setVisibility(View.GONE);
                                break;
                            case 5:
                                valueFilteringAdapter.clearAndAddAll(spinnerMeterStatus);
                                etFilterValue.setVisibility(View.GONE);
                                fltValueFiltering.setVisibility(View.VISIBLE);
                                fltComparisonOperators.setSelectedOption(1);
                                itemComparisonOperators = 1;
                                fltComparisonOperators.getChildAt(0).setVisibility(View.GONE);
                                fltComparisonOperators.getChildAt(2).setVisibility(View.GONE);
                                fltComparisonOperators.getChildAt(3).setVisibility(View.GONE);
                                break;
                            case 6:
                                valueFilteringAdapter.clearAndAddAll(spinnerValveStatus);
                                etFilterValue.setVisibility(View.GONE);
                                fltValueFiltering.setVisibility(View.VISIBLE);
                                fltComparisonOperators.setSelectedOption(1);
                                itemComparisonOperators = 1;
                                fltComparisonOperators.getChildAt(0).setVisibility(View.GONE);
                                fltComparisonOperators.getChildAt(2).setVisibility(View.GONE);
                                fltComparisonOperators.getChildAt(3).setVisibility(View.GONE);
                                break;
                            default:
                                break;
                        }
                    } else {
                        itemContentFiltering = -1;
                    }
                    break;
                case R.id.flt_comparison_operators:
                    if (selectedList != null && selectedList.size() != 0) {
                        itemComparisonOperators = selectedList.get(0);
                    } else {
                        itemComparisonOperators = -1;
                    }
                    break;
                case R.id.flt_value_filtering:
                    if (selectedList != null && selectedList.size() != 0) {
                        itemValueFiltering = selectedList.get(0);
                    } else {
                        itemValueFiltering = -1;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 压力输入监听
     */
    private TextWatcher textWatcherPressure = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int maxPressure = 20;
            if (s.toString().contains(Constant.POINT)) {
                //如果有小数点
                //限制小数位数不大于2个
                if (s.length() - 1 - s.toString().indexOf(Constant.POINT) > maxDecimalDigits) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    etFilterValue.setText(s);
                    //设置光标在末尾
                    etFilterValue.setSelection(s.length());
                    showToast("You can enter up to two decimal places");
                }
                if (s.toString().indexOf(Constant.POINT) > 0 && Double.valueOf(s.toString().substring(0, s.toString().indexOf(Constant.POINT))) >= maxPressure) {
                    s = s.toString().subSequence(0, s.length() - 1);
                    etFilterValue.setText(s);
                    //设置光标在末尾
                    etFilterValue.setSelection(s.length());
                    showToast("Pressure can not be greater than " + maxPressure + "bar");
                }
            } else {
                //如果没有小数点
                if (!TextUtils.isEmpty(s) && Double.valueOf(s.toString()) > maxPressure) {
                    s = s.toString().subSequence(0, s.length() - 1);
                    etFilterValue.setText(s);
                    //设置光标在末尾
                    etFilterValue.setSelection(s.length());
                    showToast("Pressure can not be greater than " + maxPressure + "bar");
                }
            }
            //如果直接输入小数点，前面自动补0
            if (s.toString().trim().equals(Constant.POINT)) {
                s = "0" + s;
                etFilterValue.setText(s);
                etFilterValue.setSelection(2);
            }
            //除了小数开头不能为0，而且开头不允许连续出现0
            if (s.toString().startsWith(Constant.ZERO_STRING) && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(Constant.POINT)) {
                    etFilterValue.setText(s.subSequence(0, 1));
                    etFilterValue.setSelection(1);
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 累计流量输入监听
     */
    private TextWatcher textWatcherConsumption = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int maxConsumption = 99999999;
            if (s.toString().contains(Constant.POINT)) {
                //如果有小数点
                //限制小数位数不大于2个
                if (s.length() - 1 - s.toString().indexOf(Constant.POINT) > maxDecimalDigits) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    etFilterValue.setText(s);
                    //设置光标在末尾
                    etFilterValue.setSelection(s.length());
                    showToast("You can enter up to two decimal places");
                }
                if (s.toString().indexOf(Constant.POINT) > 0 && Double.valueOf(s.toString().substring(0, s.toString().indexOf(Constant.POINT))) >= maxConsumption) {
                    s = s.toString().subSequence(0, s.length() - 1);
                    etFilterValue.setText(s);
                    //设置光标在末尾
                    etFilterValue.setSelection(s.length());
                    showToast("Total flow can not be greater than " + maxConsumption + "m³");
                }
            } else {
                //如果没有小数点
                if (!TextUtils.isEmpty(s) && Double.valueOf(s.toString()) > maxConsumption) {
                    s = s.toString().subSequence(0, s.length() - 1);
                    etFilterValue.setText(s);
                    //设置光标在末尾
                    etFilterValue.setSelection(s.length());
                    showToast("Total flow can not be greater than " + maxConsumption + "m³");
                }
            }
            //如果直接输入小数点，前面自动补0
            if (s.toString().trim().equals(Constant.POINT)) {
                s = "0" + s;
                etFilterValue.setText(s);
                etFilterValue.setSelection(2);
            }
            //除了小数开头不能为0，而且开头不允许连续出现0
            if (s.toString().startsWith(Constant.ZERO_STRING) && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(Constant.POINT)) {
                    etFilterValue.setText(s.subSequence(0, 1));
                    etFilterValue.setSelection(1);
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 瞬时流速输入监听
     */
    private TextWatcher textWatcherFlowRate = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int maxFlowRate = 100000;
            if (s.toString().contains(Constant.POINT)) {
                //如果有小数点
                //限制小数位数不大于2个
                if (s.length() - 1 - s.toString().indexOf(Constant.POINT) > maxDecimalDigits) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    etFilterValue.setText(s);
                    //设置光标在末尾
                    etFilterValue.setSelection(s.length());
                    showToast("You can enter up to two decimal places");
                }
                if (s.toString().indexOf(Constant.POINT) > 0 && Double.valueOf(s.toString().substring(0, s.toString().indexOf(Constant.POINT))) >= maxFlowRate) {
                    s = s.toString().subSequence(0, s.length() - 1);
                    etFilterValue.setText(s);
                    //设置光标在末尾
                    etFilterValue.setSelection(s.length());
                    showToast("Flow rate can not be greater than " + maxFlowRate + "m³/h");
                }
            } else {
                //如果没有小数点
                if (!TextUtils.isEmpty(s) && Double.valueOf(s.toString()) > maxFlowRate) {
                    s = s.toString().subSequence(0, s.length() - 1);
                    etFilterValue.setText(s);
                    //设置光标在末尾
                    etFilterValue.setSelection(s.length());
                    showToast("Flow rate can not be greater than " + maxFlowRate + "m³/h");
                }
            }
            //如果直接输入小数点，前面自动补0
            if (s.toString().trim().equals(Constant.POINT)) {
                s = "0" + s;
                etFilterValue.setText(s);
                etFilterValue.setSelection(2);
            }
            //除了小数开头不能为0，而且开头不允许连续出现0
            if (s.toString().startsWith(Constant.ZERO_STRING) && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(Constant.POINT)) {
                    etFilterValue.setText(s.subSequence(0, 1));
                    etFilterValue.setSelection(1);
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 查询水表的方法
     */
    public void searchWaterMeter(int searchMode) {
        //先清空页面
        initPage();
        String timeRange = etTimeRange.getText().toString().trim();
        if (TextUtils.isEmpty(timeRange)) {
            timeRange = (String) SharedPreferencesUtils.getInstance().getData("timeRange", SmartWaterSupply.DATA_SEARCH_TIME_RANGE);
        }
        switch (searchMode) {
            case SEARCH_MODE_NORMAL:
                //普通查询要比较页码输入框的值
                String pageNumber = etCurrentPage.getText().toString().trim();
                if (TextUtils.isEmpty(pageNumber)) {
                    currentPage = 1;
                } else if (Integer.valueOf(pageNumber) > totalPage) {
                    //查询页码大于总页码
                    if (totalPage == 0) {
                        //如果没有查询过或者查询到的是0条数据，则总页码为0，需要将查询页码设为1
                        currentPage = 1;
                    } else {
                        //如果总页码不为0，需要将查询页码设为总页码的值
                        currentPage = totalPage;
                    }
                } else {
                    currentPage = Integer.valueOf(pageNumber);
                }
                break;
            case SEARCH_MODE_CHANGE_SEGMENT:
                //切换“已抄到”和“未抄到”按钮查询时将页码设为1
                currentPage = 1;
                break;
            case SEARCH_MODE_LAST:
            case SEARCH_MODE_NEXT:
                //上下翻页查询时忽略页码输入框的值

                break;
            default:
                break;
        }
        searchMeterLastInformation(fieldName, fieldValue, timeRange, String.valueOf(currentPage - 1));
    }

    /**
     * 查询表提交信息的最后一条记录
     *
     * @param fieldName  层级名称
     * @param fieldValue 层级ID
     */
    private void searchMeterLastInformation(String fieldName, String fieldValue, String timeRange, String pageNumber) {
        showLoadingDialog(context, "Loading...", true);
        String meterSizeMin = (String) SharedPreferencesUtils.getInstance().getData("MeterSize_Min", Constant.EMPTY);
        String meterSizeMax = (String) SharedPreferencesUtils.getInstance().getData("MeterSize_Max", Constant.EMPTY);
        if (Constant.EMPTY.equals(meterSizeMin) || Constant.EMPTY.equals(meterSizeMax)) {
            meterSizeMin = (String) SharedPreferencesUtils.getInstance().getData("MeterSize_Min_Default", SmartWaterSupply.DATA_SEARCH_MIN_SIZE);
            meterSizeMax = (String) SharedPreferencesUtils.getInstance().getData("MeterSize_Max_Default", SmartWaterSupply.DATA_SEARCH_MAX_SIZE);
        }
        Map<String, String> params = new HashMap<>(8);
        params.put("fieldName", fieldName);
        params.put("fieldValue", fieldValue);
        params.put("meterSizeMin", meterSizeMin);
        params.put("meterSizeMax", meterSizeMax);

        params.put("timeRange", timeRange);
        params.put("rows", String.valueOf(pageSize));
        params.put("page", pageNumber);

        params.put("param", JSON.toJSONString(filterConditionList));

        showLoadingDialog(context, "Loading...", true);
        if (scvSearchMode.getSelectedIndex() == 0) {
            //查询已抄到的水表
            Call<WaterMeterLastCommitInformation> waterMeterLastReportYiChaoCall = NetClient.getInstances(NetClient.getBaseUrl(NetWork.SERVER_HOST_MAIN, NetWork.SERVER_PORT_MAIN, NetWork.PROJECT_MAIN)).getNjMeterApi().searchMeterLastReportYiChao(params);
            waterMeterLastReportYiChaoCall.enqueue(mCallbackWaterMeterLast);
        } else if (scvSearchMode.getSelectedIndex() == 1) {
            //查询未抄到的水表
            Call<WaterMeterLastCommitInformation> waterMeterLastReportWeiChaoCall = NetClient.getInstances(NetClient.getBaseUrl(NetWork.SERVER_HOST_MAIN, NetWork.SERVER_PORT_MAIN, NetWork.PROJECT_MAIN)).getNjMeterApi().searchMeterLastReportWeiChao(params);
            waterMeterLastReportWeiChaoCall.enqueue(mCallbackWaterMeterLast);
        }
    }

    private Callback<WaterMeterLastCommitInformation> mCallbackWaterMeterLast = new Callback<WaterMeterLastCommitInformation>() {
        @Override
        public void onResponse(@NotNull Call<WaterMeterLastCommitInformation> call, @NotNull Response<WaterMeterLastCommitInformation> response) {
            cancelDialog();
            LogUtils.d("retrofit", response.toString());
            if (response.isSuccessful()) {
                WaterMeterLastCommitInformation waterMeterLastCommitInformation = response.body();
                if (waterMeterLastCommitInformation == null) {
                    showToast("Data Error");
                } else {
                    if (waterMeterLastCommitInformation.getResult().equals(Constant.SUCCESS)) {
                        int totalCount = waterMeterLastCommitInformation.getCount();
                        if (totalCount % pageSize == 0) {
                            totalPage = totalCount / pageSize;
                        } else {
                            totalPage = totalCount / pageSize + 1;
                        }
                        tvMountPage.setText(String.format(getString(R.string.total_page), totalCount, totalPage));
                        etCurrentPage.setText(String.valueOf(currentPage));
                        if (totalCount == 0) {
                            //如果列表长度为0弹出提示
                            showToast("No data");
                        } else {
                            //否则显示列表内容
                            waterMeterLastCommitInformationDataList = waterMeterLastCommitInformation.getData();
                            waterMeterLastReportAdapter = new WaterMeterLastReportAdapter(mainActivity, waterMeterLastCommitInformationDataList, pageSize, currentPage - 1);
                            lvWaterMeterLast.setAdapter(waterMeterLastReportAdapter);
                            changeListVisibility(true, false);
                        }
                    } else if (waterMeterLastCommitInformation.getResult().equals(Constant.FAIL)) {
                        showToast("Query failed");
                    }
                }
            } else {
                showToast("Query failed");
            }
        }

        @Override
        public void onFailure(@NotNull Call<WaterMeterLastCommitInformation> call, @NotNull Throwable throwable) {
            cancelDialog();
            showToast("Query failed，" + throwable.getMessage());
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.ACTIVITY_REQUEST_CODE_100 && resultCode == Constant.ACTIVITY_RESULT_CODE_100) {
            String hierarchy = data.getStringExtra("hierarchy");
            //如果选择的层级和当前层级不一样，则修改显示的层级、清空原查询列表并重新请求数据
            if (!tvCurrentHierarchy.getText().toString().equals(hierarchy)) {
                tvCurrentHierarchy.setText(data.getStringExtra("hierarchy"));
                fieldValue = String.valueOf(data.getIntExtra("id", 0));
                fieldName = data.getStringExtra("fieldName");
                etCurrentPage.setText("1");
                searchWaterMeter(SEARCH_MODE_NORMAL);
            }
        }
    }

    /**
     * 初始化默认查询配置
     */
    private void initDefaultData() {
        //每页显示条数
        pageSize = Integer.valueOf(((String) SharedPreferencesUtils.getInstance().getData("Rows_Per_Page", SmartWaterSupply.DATA_SEARCH_ROWS_PER_PAGE)));
        //查询模式
        boolean searchTypeHasRead = (boolean) SharedPreferencesUtils.getInstance().getData("Default_Search_Type_Has_Read", SmartWaterSupply.DATA_SEARCH_HAS_READ);
        if (searchTypeHasRead) {
            scvSearchMode.setSelectedIndex(0);
        } else {
            scvSearchMode.setSelectedIndex(1);
        }
        String minSize = (String) SharedPreferencesUtils.getInstance().getData("MeterSize_Min_Default", SmartWaterSupply.DATA_SEARCH_MIN_SIZE);
        String maxSize = (String) SharedPreferencesUtils.getInstance().getData("MeterSize_Max_Default", SmartWaterSupply.DATA_SEARCH_MAX_SIZE);
        tvSizeRange.setText(String.format(getString(R.string.range_meterSize_DN), minSize, maxSize));
        etTimeRange.setText((String) SharedPreferencesUtils.getInstance().getData("Search_Time_Range", SmartWaterSupply.DATA_SEARCH_TIME_RANGE));
    }

    /**
     * 初始化页面信息（清空页面信息）
     */
    private void initPage() {
        if (waterMeterLastCommitInformationDataList != null) {
            waterMeterLastCommitInformationDataList.clear();
            waterMeterLastReportAdapter = new WaterMeterLastReportAdapter(mainActivity, waterMeterLastCommitInformationDataList, pageSize, currentPage - 1);
            lvWaterMeterLast.setAdapter(waterMeterLastReportAdapter);
        }
        etTimeRange.clearFocus();
        etCurrentPage.clearFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SharedPreferencesUtils.getInstance().clearData("MeterSize_Min");
        SharedPreferencesUtils.getInstance().clearData("MeterSize_Max");
        if (refreshDefaultData != null) {
            try {
                context.unregisterReceiver(refreshDefaultData);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
