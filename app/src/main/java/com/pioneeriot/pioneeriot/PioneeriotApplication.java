package com.pioneeriot.pioneeriot;

import android.support.multidex.MultiDexApplication;

import com.pioneeriot.pioneeriot.utils.CrashHandler;
import com.pioneeriot.pioneeriot.utils.SharedPreferencesUtils;

/**
 * Application类
 * Created by Li Yuliang on 2017/03/01.
 *
 * @author LiYuliang
 * @version 2017/10/27
 */

public class PioneeriotApplication extends MultiDexApplication {

    private static PioneeriotApplication instance;
    public static boolean loginSuccess;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        loginSuccess = false;
        // 捕捉异常
        CrashHandler.getInstance().init(this);
        //初始化SharedPreferences工具
        SharedPreferencesUtils.init(this);
    }

    /**
     * 单例模式中获取唯一的MyApplication实例
     *
     * @return application实例
     */
    public static PioneeriotApplication getInstance() {
        if (instance == null) {
            instance = new PioneeriotApplication();
        }
        return instance;
    }
}
