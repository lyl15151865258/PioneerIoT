package com.pioneeriot.pioneeriot.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pioneeriot.pioneeriot.utils.ActivityController;
import com.pioneeriot.pioneeriot.constant.Constant;
import com.pioneeriot.pioneeriot.adapter.HierarchyAdapter;
import com.pioneeriot.pioneeriot.utils.LogUtils;
import com.pioneeriot.pioneeriot.utils.MySQLiteOpenHelper;
import com.pioneeriot.pioneeriot.network.NetClient;
import com.pioneeriot.pioneeriot.network.NetWork;
import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.utils.SharedPreferencesUtils;
import com.pioneeriot.pioneeriot.adapter.TagAdapter;
import com.pioneeriot.pioneeriot.bean.Hierarchy;
import com.pioneeriot.pioneeriot.bean.WaterCompanyHierarchy;
import com.pioneeriot.pioneeriot.dialog.CommonWarningDialog;
import com.pioneeriot.pioneeriot.flowtaglayout.FlowTagLayout;
import com.pioneeriot.pioneeriot.flowtaglayout.OnTagClickListener;
import com.pioneeriot.pioneeriot.flowtaglayout.OnTagLongClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 选择水司层级
 * Created by LiYuliang on 2017/8/31 0031.
 *
 * @author LiYuliang
 * @version 2017/10/25
 */

public class ChooseHierarchyActivity extends BaseActivity {

    private Context context;
    private List<WaterCompanyHierarchy.Data> companyHierarchyList = new ArrayList<>();
    private List<Hierarchy> hierarchyList = new ArrayList<>();
    private List<String> historyList = new ArrayList<>();
    private ListView lvHierarchy;
    private EditText etSearch;
    private HierarchyAdapter hierarchyAdapter;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private FlowTagLayout ftlHistory;
    private TagAdapter<String> historyAdapter;
    private String fieldName;
    private int fieldValue, exchangStationId, villageId, buildingId, entranceId;
    private TextView tvExchangeStation, tvVillage, tvBuilding, tvEntrance;
    private Hierarchy currentHierarchy;
    private String allBuilding = "", allVillage = "", allExchangeStation = "", allEntrance = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_company_hierarchy);
        LinearLayout llRoot = findViewById(R.id.ll_root);
        setActionBarLayout(llRoot);
        context = this;
        fieldName = (String) SharedPreferencesUtils.getInstance().getData(getString(R.string.fieldName), "");
        fieldValue = (int) SharedPreferencesUtils.getInstance().getData(getString(R.string.fieldValue), 0);
        exchangStationId = (int) SharedPreferencesUtils.getInstance().getData(getString(R.string.exchangStationId), -1);
        villageId = (int) SharedPreferencesUtils.getInstance().getData(getString(R.string.villageId), -1);
        buildingId = (int) SharedPreferencesUtils.getInstance().getData(getString(R.string.buildingId), -1);
        entranceId = (int) SharedPreferencesUtils.getInstance().getData(getString(R.string.entranceId), -1);
        lvHierarchy = findViewById(R.id.lv_hierarchy);
        hierarchyAdapter = new HierarchyAdapter(this, hierarchyList);
        historyAdapter = new TagAdapter<>(this);
        lvHierarchy.setAdapter(hierarchyAdapter);
        lvHierarchy.setOnItemClickListener(onItemClickListener);
        searchAllHierarchy();
        ImageView ivDeleteSearch = findViewById(R.id.iv_deleteSearch);
        ImageView ivDeleteHistory = findViewById(R.id.iv_deleteHistory);
        ivDeleteSearch.setOnClickListener(onClickListener);
        ivDeleteHistory.setOnClickListener(onClickListener);
        etSearch = findViewById(R.id.et_search);
        etSearch.addTextChangedListener(textWatcher);
        ftlHistory = findViewById(R.id.ftl_history);
        ftlHistory.setOnTagClickListener(onTagClickListener);
        ftlHistory.setOnTagLongClickListener(onTagLongClickListener);
        TextView tvClear = findViewById(R.id.tv_clear);
        TextView tvDetermine = findViewById(R.id.tv_determine);
        tvExchangeStation = findViewById(R.id.tv_exchangeStation);
        tvVillage = findViewById(R.id.tv_village);
        tvBuilding = findViewById(R.id.tv_building);
        tvEntrance = findViewById(R.id.tv_entrance);
        tvClear.setOnClickListener(onClickListener);
        tvDetermine.setOnClickListener(onClickListener);
        tvExchangeStation.setOnClickListener(onClickListener);
        tvVillage.setOnClickListener(onClickListener);
        tvBuilding.setOnClickListener(onClickListener);
        tvEntrance.setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //实例化数据库SQLiteOpenHelper子类对象
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        mySQLiteOpenHelper.open();
        //查询显示历史搜索信息
        queryData();
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            tvExchangeStation.setText(allExchangeStation);
            tvVillage.setText(allVillage);
            tvBuilding.setText(allBuilding);
            tvEntrance.setText(allEntrance);
            currentHierarchy = (Hierarchy) parent.getItemAtPosition(position);
            int type = currentHierarchy.getType();
            WaterCompanyHierarchy.Data companyHierarchy = (new WaterCompanyHierarchy()).new Data();
            switch (type) {
                case Hierarchy.EXCHANGE_STATION:
                    //如果点击的是供水站层级
                    for (int i = 0; i < companyHierarchyList.size(); i++) {
                        if (companyHierarchyList.get(i).getExchangStationId() == currentHierarchy.getId()) {
                            companyHierarchy = companyHierarchyList.get(i);
                            break;
                        }
                    }
                    tvExchangeStation.setText(companyHierarchy.getExchangStation());
                    break;
                case Hierarchy.VILLAGE:
                    //如果点击的是小区层级
                    for (int i = 0; i < companyHierarchyList.size(); i++) {
                        if (companyHierarchyList.get(i).getVillageId() == currentHierarchy.getId()) {
                            companyHierarchy = companyHierarchyList.get(i);
                            break;
                        }
                    }
                    tvExchangeStation.setText(companyHierarchy.getExchangStation());
                    tvVillage.setText(companyHierarchy.getVillage());
                    break;
                case Hierarchy.BUILDING:
                    //如果点击的是楼栋层级
                    for (int i = 0; i < companyHierarchyList.size(); i++) {
                        if (companyHierarchyList.get(i).getBuildingId() == currentHierarchy.getId()) {
                            companyHierarchy = companyHierarchyList.get(i);
                            break;
                        }
                    }
                    tvExchangeStation.setText(companyHierarchy.getExchangStation());
                    tvVillage.setText(companyHierarchy.getVillage());
                    tvBuilding.setText(companyHierarchy.getBuilding());
                    break;
                case Hierarchy.ENTRANCE:
                    //如果点击的是单元层级
                    for (int i = 0; i < companyHierarchyList.size(); i++) {
                        if (companyHierarchyList.get(i).getEntranceId() == currentHierarchy.getId()) {
                            companyHierarchy = companyHierarchyList.get(i);
                            break;
                        }
                    }
                    tvExchangeStation.setText(companyHierarchy.getExchangStation());
                    tvVillage.setText(companyHierarchy.getVillage());
                    tvBuilding.setText(companyHierarchy.getBuilding());
                    tvEntrance.setText(companyHierarchy.getEntrance());
                    break;
                default:
                    break;
            }
            //保存到搜索记录
            if (!"".equals(etSearch.getText().toString()) && !hasData(etSearch.getText().toString())) {
                insertData(etSearch.getText().toString());
            }
        }
    };

    /**
     * 查询水司所有层级
     */
    private void searchAllHierarchy() {
        Map<String, String> params = new HashMap<>(2);
        params.put("fieldName", fieldName);
        params.put("fieldValue", String.valueOf(fieldValue));
        showLoadingDialog(context, "Loading...", true);
        String ip = (String) SharedPreferencesUtils.getInstance().getData("ip", "");
        String httpPort = (String) SharedPreferencesUtils.getInstance().getData("httpPort", "");
        String serviceName = (String) SharedPreferencesUtils.getInstance().getData("serviceName", "");
        Call<WaterCompanyHierarchy> waterCompanyHierarchyCall = NetClient.getInstances(NetClient.getBaseUrl(ip, httpPort, serviceName)).getNjMeterApi().searchAllHierarchy(params);
        waterCompanyHierarchyCall.enqueue(mCallback);
    }

    private Callback<WaterCompanyHierarchy> mCallback = new Callback<WaterCompanyHierarchy>() {
        @Override
        public void onResponse(@NotNull Call<WaterCompanyHierarchy> call, @NotNull Response<WaterCompanyHierarchy> response) {
            cancelDialog();
            LogUtils.d("retrofit", response.toString());
            if (response.isSuccessful()) {
                WaterCompanyHierarchy waterCompanyHierarchy = response.body();
                if (waterCompanyHierarchy == null) {
                    showToast("Data Error");
                } else {
                    if (Constant.SUCCESS.equals(waterCompanyHierarchy.getResult())) {
                        companyHierarchyList = waterCompanyHierarchy.getData();
                        if (companyHierarchyList.size() != 0) {
                            showHierarchyListView();
                        } else {
                            showToast("No hierarchy");
                        }
                    } else {
                        showToast("No hierarchy");
                    }
                }
            } else {
                showToast("Query failed");
            }
        }

        @Override
        public void onFailure(@NotNull Call<WaterCompanyHierarchy> call, @NotNull Throwable throwable) {
            cancelDialog();
            showToast("Query failed，" + throwable.getMessage());
        }
    };

    /**
     * 插入数据
     *
     * @param tempName 需要插入的数据
     */
    private void insertData(String tempName) {
        ContentValues values = new ContentValues();
        values.put("name", tempName);
        values.put("fieldName", fieldName);
        values.put("fieldValue", String.valueOf(fieldValue));
        mySQLiteOpenHelper.insert("hierarchy_record", values);
        queryData();
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            showHierarchyListView();
        }
    };

    /**
     * 展示层级结果
     */
    private void showHierarchyListView() {
        String text = etSearch.getText().toString();
        hierarchyList.clear();
        for (int i = 0; i < companyHierarchyList.size(); i++) {
            if (exchangStationId == -1 && companyHierarchyList.get(i).getExchangStation().contains(text)) {
                //供水站层级包含该字段
                Hierarchy hierarchy = new Hierarchy();
                hierarchy.setId(companyHierarchyList.get(i).getExchangStationId());
                hierarchy.setType(Hierarchy.EXCHANGE_STATION);
                hierarchy.setText("（Supply Station）" + companyHierarchyList.get(i).getExchangStation());
                if (!hierarchyList.contains(hierarchy)) {
                    hierarchyList.add(hierarchy);
                }
            }
            if (villageId == -1 && companyHierarchyList.get(i).getVillage().contains(text)) {
                //小区层级包含该字段
                Hierarchy hierarchy = new Hierarchy();
                hierarchy.setId(companyHierarchyList.get(i).getVillageId());
                hierarchy.setType(Hierarchy.VILLAGE);
                hierarchy.setText("（Community）" + companyHierarchyList.get(i).getVillage());
                if (!hierarchyList.contains(hierarchy)) {
                    hierarchyList.add(hierarchy);
                }
            }
            if (buildingId == -1 && companyHierarchyList.get(i).getBuilding().contains(text)) {
                //楼栋层级包含该字段
                Hierarchy hierarchy = new Hierarchy();
                hierarchy.setId(companyHierarchyList.get(i).getBuildingId());
                hierarchy.setType(Hierarchy.BUILDING);
                hierarchy.setText("（Building）" + companyHierarchyList.get(i).getBuilding());
                if (!hierarchyList.contains(hierarchy)) {
                    hierarchyList.add(hierarchy);
                }
            }
            if (entranceId == -1 && companyHierarchyList.get(i).getEntrance().contains(text)) {
                //单元层级包含该字段
                Hierarchy hierarchy = new Hierarchy();
                hierarchy.setId(companyHierarchyList.get(i).getEntranceId());
                hierarchy.setType(Hierarchy.ENTRANCE);
                hierarchy.setText("（Unit）" + companyHierarchyList.get(i).getEntrance());
                if (!hierarchyList.contains(hierarchy)) {
                    hierarchyList.add(hierarchy);
                }
            }
        }
        hierarchyAdapter = new HierarchyAdapter(ChooseHierarchyActivity.this, hierarchyList);
        lvHierarchy.removeAllViewsInLayout();
        lvHierarchy.setAdapter(hierarchyAdapter);
        lvHierarchy.deferNotifyDataSetChanged();
        if (exchangStationId != -1) {
            tvExchangeStation.setText(companyHierarchyList.get(0).getExchangStation());
        }
        if (villageId != -1) {
            tvVillage.setText(companyHierarchyList.get(0).getVillage());
        }
        if (buildingId != -1) {
            tvBuilding.setText(companyHierarchyList.get(0).getBuilding());
        }
        if (entranceId != -1) {
            tvEntrance.setText(companyHierarchyList.get(0).getEntrance());
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_deleteSearch:
                    etSearch.setText("");
                    break;
                case R.id.iv_deleteHistory:
                    deleteData();
                    queryData();
                    break;
                case R.id.tv_clear:
                    etSearch.setText("");
                    tvExchangeStation.setText(allExchangeStation);
                    tvVillage.setText(allVillage);
                    tvBuilding.setText(allBuilding);
                    tvEntrance.setText(allEntrance);
                    currentHierarchy = null;
                    showHierarchyListView();
                    break;
                case R.id.tv_determine:
                    String hierarchy, name;
                    int id;
                    if (currentHierarchy != null) {
                        int type = currentHierarchy.getType();
                        WaterCompanyHierarchy.Data companyHierarchy = (new WaterCompanyHierarchy()).new Data();
                        id = currentHierarchy.getId();
                        switch (type) {
                            case Hierarchy.EXCHANGE_STATION:
                                //如果是供水站层级，则直接显示供水站信息
                                for (int i = 0; i < companyHierarchyList.size(); i++) {
                                    if (companyHierarchyList.get(i).getExchangStationId() == currentHierarchy.getId()) {
                                        companyHierarchy = companyHierarchyList.get(i);
                                        break;
                                    }
                                }
                                hierarchy = companyHierarchy.getExchangStation() + "—All";
                                name = "exchangStationId";
                                break;
                            case Hierarchy.VILLAGE:
                                //如果是楼栋层级，则需要查询显示上一级供水站信息
                                for (int i = 0; i < companyHierarchyList.size(); i++) {
                                    if (companyHierarchyList.get(i).getVillageId() == currentHierarchy.getId()) {
                                        companyHierarchy = companyHierarchyList.get(i);
                                        break;
                                    }
                                }
                                hierarchy = companyHierarchy.getExchangStation() + "—" + companyHierarchy.getVillage() + "—All";
                                name = "villageId";
                                break;
                            case Hierarchy.BUILDING:
                                //如果是楼栋层级，则需要查询显示上一级供水站信息
                                for (int i = 0; i < companyHierarchyList.size(); i++) {
                                    if (companyHierarchyList.get(i).getBuildingId() == currentHierarchy.getId()) {
                                        companyHierarchy = companyHierarchyList.get(i);
                                        break;
                                    }
                                }
                                hierarchy = companyHierarchy.getExchangStation() + "—" + companyHierarchy.getVillage() + "—" + companyHierarchy.getBuilding() + "—All";
                                name = "buildingId";
                                break;
                            case Hierarchy.ENTRANCE:
                                //如果是楼栋层级，则需要查询显示上一级供水站信息
                                for (int i = 0; i < companyHierarchyList.size(); i++) {
                                    if (companyHierarchyList.get(i).getEntranceId() == currentHierarchy.getId()) {
                                        companyHierarchy = companyHierarchyList.get(i);
                                        break;
                                    }
                                }
                                hierarchy = companyHierarchy.getExchangStation() + "—" + companyHierarchy.getVillage() + "—" + companyHierarchy.getBuilding() + "—" + companyHierarchy.getEntrance();
                                name = "entranceId";
                                break;
                            default:
                                hierarchy = "All";
                                name = "";
                                break;
                        }
                    } else {
                        hierarchy = "All";
                        id = fieldValue;
                        name = fieldName;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("hierarchy", hierarchy);
                    intent.putExtra("fieldName", name);
                    intent.putExtra("id", id);
                    setResult(100, intent);
                    ActivityController.finishActivity(ChooseHierarchyActivity.this);
                    break;
                case R.id.tv_exchangeStation:
                    if (exchangStationId == -1) {
                        etSearch.setText("");
                        showSuitedHierarchy(v);
                    } else {
                        showToast("You can not change the hierarchy");
                    }
                    break;
                case R.id.tv_village:
                    if (villageId == -1) {
                        etSearch.setText("");
                        showSuitedHierarchy(v);
                    } else {
                        showToast("You can not change the hierarchy");
                    }
                    break;
                case R.id.tv_building:
                    if (buildingId == -1) {
                        etSearch.setText("");
                        showSuitedHierarchy(v);
                    } else {
                        showToast("You can not change the hierarchy");
                    }
                    break;
                case R.id.tv_entrance:
                    if (entranceId == -1) {
                        etSearch.setText("");
                        showSuitedHierarchy(v);
                    } else {
                        showToast("You can not change the hierarchy");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 点击层级选项显示对应的层级list
     *
     * @param view View控件
     */
    private void showSuitedHierarchy(View view) {
        hierarchyList.clear();
        switch (view.getId()) {
            case R.id.tv_exchangeStation:
                for (int i = 0; i < companyHierarchyList.size(); i++) {
                    WaterCompanyHierarchy.Data companyHierarchy = companyHierarchyList.get(i);
                    Hierarchy hierarchy = new Hierarchy();
                    hierarchy.setId(companyHierarchy.getExchangStationId());
                    hierarchy.setType(Hierarchy.EXCHANGE_STATION);
                    hierarchy.setText("（Supply Station）" + companyHierarchy.getExchangStation());
                    if (!hierarchyList.contains(hierarchy)) {
                        hierarchyList.add(hierarchy);
                    }
                }
                break;
            case R.id.tv_village:
                for (int i = 0; i < companyHierarchyList.size(); i++) {
                    WaterCompanyHierarchy.Data companyHierarchy = companyHierarchyList.get(i);
                    Hierarchy hierarchy = new Hierarchy();
                    hierarchy.setId(companyHierarchy.getVillageId());
                    hierarchy.setType(Hierarchy.VILLAGE);
                    hierarchy.setText("（Community）" + companyHierarchy.getVillage());
                    if (!hierarchyList.contains(hierarchy)) {
                        if (!allExchangeStation.equals(tvExchangeStation.getText().toString())) {
                            if (companyHierarchy.getExchangStation().equals(tvExchangeStation.getText().toString())) {
                                hierarchyList.add(hierarchy);
                            }
                        } else {
                            hierarchyList.add(hierarchy);
                        }
                    }
                }
                break;
            case R.id.tv_building:
                for (int i = 0; i < companyHierarchyList.size(); i++) {
                    WaterCompanyHierarchy.Data companyHierarchy = companyHierarchyList.get(i);
                    Hierarchy hierarchy = new Hierarchy();
                    hierarchy.setId(companyHierarchy.getBuildingId());
                    hierarchy.setType(Hierarchy.BUILDING);
                    hierarchy.setText("（Building）" + companyHierarchy.getBuilding());
                    if (!hierarchyList.contains(hierarchy)) {
                        if (!allVillage.equals(tvVillage.getText().toString())) {
                            if (companyHierarchy.getVillage().equals(tvVillage.getText().toString())) {
                                hierarchyList.add(hierarchy);
                            }
                        } else if (!allExchangeStation.equals(tvExchangeStation.getText().toString())) {
                            if (companyHierarchy.getExchangStation().equals(tvExchangeStation.getText().toString())) {
                                hierarchyList.add(hierarchy);
                            }
                        } else {
                            hierarchyList.add(hierarchy);
                        }
                    }
                }
                break;
            case R.id.tv_entrance:
                for (int i = 0; i < companyHierarchyList.size(); i++) {
                    WaterCompanyHierarchy.Data companyHierarchy = companyHierarchyList.get(i);
                    Hierarchy hierarchy = new Hierarchy();
                    hierarchy.setId(companyHierarchy.getEntranceId());
                    hierarchy.setType(Hierarchy.ENTRANCE);
                    hierarchy.setText("（Unit）" + companyHierarchy.getEntrance());
                    if (!hierarchyList.contains(hierarchy)) {
                        if (!allBuilding.equals(tvBuilding.getText().toString())) {
                            if (companyHierarchy.getBuilding().equals(tvBuilding.getText().toString())) {
                                hierarchyList.add(hierarchy);
                            }
                        } else if (!allVillage.equals(tvVillage.getText().toString())) {
                            if (companyHierarchy.getVillage().equals(tvVillage.getText().toString())) {
                                hierarchyList.add(hierarchy);
                            }
                        } else if (!allExchangeStation.equals(tvExchangeStation.getText().toString())) {
                            if (companyHierarchy.getExchangStation().equals(tvExchangeStation.getText().toString())) {
                                hierarchyList.add(hierarchy);
                            }
                        } else {
                            hierarchyList.add(hierarchy);
                        }
                    }
                }
                break;
            default:
                break;
        }
        hierarchyAdapter = new HierarchyAdapter(ChooseHierarchyActivity.this, hierarchyList);
        lvHierarchy.removeAllViewsInLayout();
        lvHierarchy.setAdapter(hierarchyAdapter);
        lvHierarchy.deferNotifyDataSetChanged();
    }

    /**
     * 模糊查询数据 并显示在GridView列表上
     */
    private void queryData() {
        //模糊搜索
        Cursor cursor = mySQLiteOpenHelper.findList("hierarchy_record", null, "fieldName = ? and fieldValue = ?", new String[]{fieldName, String.valueOf(fieldValue)}, null, null, null);
        // 清空list
        historyList.clear();
        // 查询到的数据添加到list集合
        while (cursor.moveToNext()) {
            String history = cursor.getString(1);
            historyList.add(history);
        }
        ftlHistory.setAdapter(historyAdapter);
        historyAdapter.clearAndAddAll(historyList);
        historyAdapter.notifyDataSetChanged();
        if (historyList.size() == 0) {
            ftlHistory.setVisibility(View.GONE);
        } else {
            ftlHistory.setVisibility(View.VISIBLE);
        }
        cursor.close();
    }

    private OnTagClickListener onTagClickListener = new OnTagClickListener() {
        @Override
        public void onItemClick(FlowTagLayout parent, View view, int position) {
            //获取到用户点击列表里的文字,并自动填充到搜索框内
            TextView textView = view.findViewById(R.id.tv_tag);
            etSearch.setText(textView.getText().toString());
        }
    };

    /**
     * 标签长按监听
     */
    private OnTagLongClickListener onTagLongClickListener = (parent, view, position) -> {
        CommonWarningDialog commonWarningDialog = new CommonWarningDialog(context, getString(R.string.warning_delete_history));
        commonWarningDialog.setCancelable(false);
        commonWarningDialog.setOnDialogClickListener(new CommonWarningDialog.OnDialogClickListener() {
            @Override
            public void onOKClick() {
                deleteOneData(((TextView) view.findViewById(R.id.tv_tag)).getText().toString());
                queryData();
            }

            @Override
            public void onCancelClick() {
            }
        });
        commonWarningDialog.show();
    };

    /**
     * 检查数据库中是否已经有该条记录
     *
     * @param tempName 输入框内容
     * @return 是否包含
     */
    private boolean hasData(String tempName) {
        //从Record这个表里找到name=tempName的id
        Cursor cursor = mySQLiteOpenHelper.findList("hierarchy_record", null, "name = ? and fieldName = ? and fieldValue = ?", new String[]{tempName, fieldName, String.valueOf(fieldValue)}, null, null, null);
        boolean hasData = cursor.moveToNext();
        cursor.close();
        return hasData;
    }

    /**
     * 清空本水司下某一条搜索记录
     */
    private void deleteOneData(String name) {
        mySQLiteOpenHelper.delete("hierarchy_record", "name = ? and fieldName = ? and fieldValue = ?", new String[]{name, fieldName, String.valueOf(fieldValue)});
    }

    /**
     * 清空数据
     */
    private void deleteData() {
        mySQLiteOpenHelper.delete("hierarchy_record", null, null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        queryData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mySQLiteOpenHelper.close();
    }
}
