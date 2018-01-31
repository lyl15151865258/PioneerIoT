package com.pioneeriot.pioneeriot.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pioneeriot.pioneeriot.utils.ActivityController;
import com.pioneeriot.pioneeriot.constant.Constant;
import com.pioneeriot.pioneeriot.utils.LogUtils;
import com.pioneeriot.pioneeriot.network.NetClient;
import com.pioneeriot.pioneeriot.network.NetWork;
import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.utils.SharedPreferencesUtils;
import com.pioneeriot.pioneeriot.utils.ViewUtils;
import com.pioneeriot.pioneeriot.bean.WaterMeterLoginResult;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登录
 * Created by LiYuliang on 2017/07/10 0010.
 *
 * @author LiYuliang
 * @version 2017/10/20
 */

public class LoginActivity extends BaseActivity {

    private EditText etUsername, etPassword;
    private ImageView  ivInvisible;
    private boolean isInvisibleEtPassWord = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //沉浸式通知栏,透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etPassword.setOnEditorActionListener(onEditorActionListener);
        ivInvisible = findViewById(R.id.iv_invisible);
        String username = (String) SharedPreferencesUtils.getInstance().getData("username", "");
        String password = (String) SharedPreferencesUtils.getInstance().getData("password", "");
        etUsername.setText(username);
        etPassword.setText(password);
        ViewUtils.setCharSequence(etUsername.getText());
        ViewUtils.setCharSequence(etPassword.getText());
        (findViewById(R.id.iv_deleteName)).setOnClickListener(onClickListener);
        ivInvisible.setOnClickListener(onClickListener);
        (findViewById(R.id.btn_login)).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = (view) -> {
        switch (view.getId()) {
            case R.id.iv_deleteName:
                etUsername.setText("");
                break;
            case R.id.iv_invisible:
                ViewUtils.changePasswordState(isInvisibleEtPassWord, etPassword, ivInvisible);
                isInvisibleEtPassWord = !isInvisibleEtPassWord;
                break;
            case R.id.btn_login:
                login();
                break;
            default:
                break;
        }
    };

    private TextView.OnEditorActionListener onEditorActionListener = (textView, actionId, keyEvent) -> {
        boolean isGo = actionId == EditorInfo.IME_ACTION_GO || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER);
        if (isGo) {
            login();
        }
        return false;
    };

    /**
     * 登录
     */
    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            showToast("Please enter your account");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showToast("Please enter the password");
            return;
        }
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            Map<String, String> energyManagerParams = new HashMap<>(2);
            energyManagerParams.put("loginName", username);
            energyManagerParams.put("password", password);
            Call<WaterMeterLoginResult> waterMeterLoginResultCall = NetClient.getInstances(NetClient.getBaseUrl(NetWork.SERVER_HOST_MAIN, NetWork.SERVER_PORT_MAIN, NetWork.PROJECT_MAIN)).getNjMeterApi().loginWaterMeter(energyManagerParams);
            waterMeterLoginResultCall.enqueue(mCallbackWaterMeterLogin);
        }
    }

    private Callback<WaterMeterLoginResult> mCallbackWaterMeterLogin = new Callback<WaterMeterLoginResult>() {
        @Override
        public void onResponse(@NotNull Call<WaterMeterLoginResult> call, @NotNull Response<WaterMeterLoginResult> response) {
            LogUtils.d("retrofit", response.toString());
            if (response.isSuccessful()) {
                WaterMeterLoginResult waterMeterLoginResult = response.body();
                if (waterMeterLoginResult == null) {
                    showToast("Data Error");
                } else {
                    String result = waterMeterLoginResult.getResult();
                    if (result.equals(Constant.SUCCESS)) {
                        //保存账号信息
                        SharedPreferencesUtils.getInstance().saveData("username", etUsername.getText().toString().trim());
                        SharedPreferencesUtils.getInstance().saveData("password", etPassword.getText().toString().trim());
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
                                ActivityController.finishActivity(LoginActivity.this);
                                break;
                            default:
                                showToast("Data Error");
                                break;
                        }
                    } else {
                        showToast("The account or password is incorrect");
                    }
                }
            } else {
                showToast("Data Error");
            }
        }

        @Override
        public void onFailure(@NotNull Call<WaterMeterLoginResult> call, @NotNull Throwable throwable) {
            showToast("Data Error，" + throwable.getMessage());
        }
    };
}
