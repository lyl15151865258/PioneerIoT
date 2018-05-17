package com.pioneeriot.pioneeriot.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.pioneeriot.pioneeriot.utils.ActivityController;
import com.pioneeriot.pioneeriot.constant.Constant;
import com.pioneeriot.pioneeriot.utils.LogUtils;
import com.pioneeriot.pioneeriot.network.NetClient;
import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.utils.SharedPreferencesUtils;
import com.pioneeriot.pioneeriot.bean.WaterMeterLoginResult;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by LiYuliang on 2017/2/17 0017.
 * Logo页面，过渡页面，跳转到MainActivity
 *
 * @author LiYuliang
 * @version 2017/10/27
 */

public class LogoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logo);
        String ip = (String) SharedPreferencesUtils.getInstance().getData("ip", "");
        String httpPort = (String) SharedPreferencesUtils.getInstance().getData("httpPort", "");
        String serviceName = (String) SharedPreferencesUtils.getInstance().getData("serviceName", "");
        String username = (String) SharedPreferencesUtils.getInstance().getData("userName", "");
        String password = (String) SharedPreferencesUtils.getInstance().getData("passWord", "");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            login(ip, httpPort, serviceName, username, password);
        } else {
            openActivity(LoginActivity.class);
            ActivityController.finishActivity(this);
        }
    }

    /**
     * 登录
     */
    private void login(String ip, String httpPort, String serviceName, String username, String password) {
        Map<String, String> energyManagerParams = new HashMap<>(3);
        energyManagerParams.put("loginName", username);
        energyManagerParams.put("password", password);
        energyManagerParams.put("type", "meter");
        Call<WaterMeterLoginResult> waterMeterLoginResultCall = NetClient.getInstances(NetClient.getBaseUrl(ip, httpPort, serviceName)).getNjMeterApi().loginWaterMeter(energyManagerParams);
        waterMeterLoginResultCall.enqueue(mCallbackWaterMeterLogin);
    }

    private Callback<WaterMeterLoginResult> mCallbackWaterMeterLogin = new Callback<WaterMeterLoginResult>() {
        @Override
        public void onResponse(@NotNull Call<WaterMeterLoginResult> call, @NotNull Response<WaterMeterLoginResult> response) {
            LogUtils.d("retrofit", response.toString());
            if (response.isSuccessful()) {
                WaterMeterLoginResult waterMeterLoginResult = response.body();
                if (waterMeterLoginResult == null) {
                    showToast("Data Error");
                    openActivity(LoginActivity.class);
                    ActivityController.finishActivity(LogoActivity.this);
                } else {
                    String result = waterMeterLoginResult.getResult();
                    if (result.equals(Constant.SUCCESS)) {
                        //登录人信息，用于安装、维修、更换时的操作人信息
                        SharedPreferencesUtils.getInstance().saveData(getString(R.string.Meter_Manager_Name), waterMeterLoginResult.getUserName());
                        switch (waterMeterLoginResult.getPrivilege()) {
                            case "普通":
                                //对象中拿到集合
                                WaterMeterLoginResult.Data data = waterMeterLoginResult.getData().get(0);
                                //判断普通管理员的最高权限
                                int supplierId = data.getSupplierId();
                                int exchangStationId = data.getExchangStationId();
                                int villageId = data.getVillageId();
                                int buildingId = data.getBuildingId();
                                int entranceId = data.getEntranceId();
                                String companyName = data.getDeptName();
                                String fieldName = Constant.EMPTY;
                                int fieldValue = -1;
                                if (entranceId != -1) {
                                    fieldName = "entranceId";
                                    fieldValue = entranceId;
                                } else if (buildingId != -1) {
                                    fieldName = "buildingId";
                                    fieldValue = buildingId;
                                } else if (villageId != -1) {
                                    fieldName = "villageId";
                                    fieldValue = villageId;
                                } else if (exchangStationId != -1) {
                                    fieldName = "exchangStationId";
                                    fieldValue = exchangStationId;
                                } else if (supplierId != -1) {
                                    fieldName = "supplierId";
                                    fieldValue = supplierId;
                                }
                                SharedPreferencesUtils.getInstance().saveData(getString(R.string.waterCompanyName), companyName);
                                SharedPreferencesUtils.getInstance().saveData(getString(R.string.fieldName), fieldName);
                                SharedPreferencesUtils.getInstance().saveData(getString(R.string.fieldValue), fieldValue);
                                SharedPreferencesUtils.getInstance().saveData(getString(R.string.supplierId), supplierId);
                                SharedPreferencesUtils.getInstance().saveData(getString(R.string.exchangStationId), exchangStationId);
                                SharedPreferencesUtils.getInstance().saveData(getString(R.string.villageId), villageId);
                                SharedPreferencesUtils.getInstance().saveData(getString(R.string.buildingId), buildingId);
                                SharedPreferencesUtils.getInstance().saveData(getString(R.string.entranceId), entranceId);
                                openActivity(MainActivity.class);
                                ActivityController.finishActivity(LogoActivity.this);
                                break;
                            default:
                                showToast("Data Error");
                                break;
                        }
                    } else {
                        showToast("The account or password is incorrect");
                        openActivity(LoginActivity.class);
                        ActivityController.finishActivity(LogoActivity.this);
                    }
                }
            } else {
                showToast("Data Error");
                openActivity(LoginActivity.class);
                ActivityController.finishActivity(LogoActivity.this);
            }
        }

        @Override
        public void onFailure(@NotNull Call<WaterMeterLoginResult> call, @NotNull Throwable throwable) {
            showToast("Data Error，" + throwable.getMessage());
            openActivity(LoginActivity.class);
            ActivityController.finishActivity(LogoActivity.this);
        }
    };

    /**
     * Logo页面不允许退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                return true;
            case KeyEvent.KEYCODE_BACK:
                return true;
            case KeyEvent.KEYCODE_CALL:
                return true;
            case KeyEvent.KEYCODE_SYM:
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                return true;
            case KeyEvent.KEYCODE_STAR:
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
