package com.pioneeriot.pioneeriot.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by LiYuliang on 2017/3/6 0006.
 * SharedPreferences工具类
 *
 * @author LiYuliang
 * @version 2017/11/21
 */

public class SharedPreferencesUtils {
    private static final String FILE_NAME = "app_data";
    private static SharedPreferences mySharedPreference;
    private static SharedPreferencesUtils instance;
    private SharedPreferences.Editor editor;

    private SharedPreferencesUtils(Context context) {
        mySharedPreference = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = mySharedPreference.edit();
    }

    /**
     * 使用同步锁避免多线程的同步问题
     */
    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesUtils(context);
        }
    }

    public static SharedPreferencesUtils getInstance() {
        if (instance == null) {
            throw new RuntimeException("class should init!");
        }
        return instance;
    }

    /**
     * 保存数据
     *
     * @param key  键值
     * @param data 数据
     */
    public void saveData(String key, Object data) {
        String type = data.getClass().getSimpleName();
        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }
        editor.commit();
    }

    /**
     * 获取数据
     *
     * @param key      键值
     * @param defValue 默认值
     * @return 数据
     */
    public Object getData(String key, Object defValue) {
        String type = defValue.getClass().getSimpleName();
        if ("Integer".equals(type)) {
            return mySharedPreference.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return mySharedPreference.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return mySharedPreference.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return mySharedPreference.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return mySharedPreference.getLong(key, (Long) defValue);
        } else {
            return null;
        }
    }

    /**
     * 判断是否包含数据
     *
     * @param key 键值
     * @return 是否包含
     */
    public boolean containsData(String key) {
        return mySharedPreference.getAll().containsKey(key);
    }

    /**
     * 清除所有数据
     */
    public void clearAllData() {
        editor.clear();
        editor.commit();
    }

    /**
     * 清除指定键值的数据
     *
     * @param key 键值
     */
    public void clearData(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 模糊删除指定键值的数据
     *
     * @param key 键值
     */
    public void clearFuzzyData(String key) {
        Map<String, ?> allKeyValue = mySharedPreference.getAll();
        for (Map.Entry<String, ?> entry : allKeyValue.entrySet()) {
            if (entry.getKey().contains(key)) {
                editor.remove(entry.getKey());
            }
        }
        editor.commit();
    }
}
