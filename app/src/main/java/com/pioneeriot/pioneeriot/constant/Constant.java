package com.pioneeriot.pioneeriot.constant;

/**
 * 部分常量值，用于解决提示魔法值的提示和接口返回数据解析用
 * Created by LiYuliang on 2017/10/25.
 *
 * @author LiYuliang
 * @version 2017/11/21
 */

public class Constant {

    public static final String EMPTY = "";
    public static final String FAIL = "fail";
    public static final String NEW_LINE = "\n";
    public static final String POINT = ".";
    public static final String HYPHEN = "-";
    public static final String SUCCESS = "success";

    /**
     * EventBus标记
     */
    public static final String CONNECT_SUCCESS = "connectSuccess";
    public static final String CONNECT_FAIL = "connectFail";
    public static final String SHOW_TOAST = "showToast";

    public static final int METER_ID_LENGTH = 8;
    public static final int HYDRANT_ID_LENGTH = 8;
    public static final int IMEI_LENGTH = 11;
    public static final String DEFAULT_METER_ID = "FFFFFFFF";

    public static final int ACTIVITY_REQUEST_CODE_100 = 100;
    public static final int ACTIVITY_RESULT_CODE_100 = 100;
    public static final int ACTIVITY_RESULT_CODE_200 = 200;

    /**
     * 退出程序点击两次返回键的间隔时间
     */
    public static final int EXIT_DOUBLE_CLICK_TIME = 2000;
    /**
     * 距离达到1000m进行单位转换，变为1km
     */
    public static final int KILOMETER = 1000;
    /**
     * 网页加载完成进度
     */
    public static final int PROGRESS_WEBVIEW = 100;


    /**
     * *******************************************************  数值0（int型、float型、String型） *******************************************************
     */
    public static final int ZERO_INT = 0;
    public static final float ZERO_FLOAT = 0.00f;
    public static final String ZERO_FLOAT_STRING = "0.00";
    public static final String ZERO_STRING = "0";
}
