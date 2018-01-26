package com.pioneeriot.pioneeriot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.pioneeriot.pioneeriot.R;

/**
 * 智慧水务平台集抄管理模块数据查询页面选择筛选条件的弹窗
 * Created by LiYuliang on 2018/1/03 0003.
 *
 * @author LiYuliang
 * @version 2018/01/03
 */

public class ChooseFilterDialog extends Dialog {

    private Context context;
    private ViewGroup viewGroup;
    private OnDialogClickListener dialogClickListener;

    public ChooseFilterDialog(Context context, ViewGroup viewGroup) {
        super(context);
        this.context = context;
        this.viewGroup = viewGroup;
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_choose_filter, viewGroup, false);
        setContentView(view);
        initWindow();
        findViewById(R.id.btn_cancel).setOnClickListener((v) -> dismiss());
        findViewById(R.id.btn_save).setOnClickListener((v) -> {
            if (dialogClickListener != null) {
                dialogClickListener.onSaveClick();
            }
        });
        findViewById(R.id.btn_clear).setOnClickListener((v) -> {
            if (dialogClickListener != null) {
                dialogClickListener.onClearClick();
            }
        });
    }

    /**
     * 添加黑色半透明背景
     */
    private void initWindow() {
        Window dialogWindow = getWindow();
        //设置window背景
        dialogWindow.setBackgroundDrawable(new ColorDrawable(0));
        //设置输入法显示模式
        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.9);
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

    public void setOnDialogClickListener(OnDialogClickListener clickListener) {
        dialogClickListener = clickListener;
    }

    /**
     * 添加按钮点击事件
     */
    public interface OnDialogClickListener {
        /**
         * 取消按钮点击事件
         */
        void onCancelClick();

        /**
         * 清空按钮点击事件
         */
        void onClearClick();

        /**
         * 保存按钮点击事件
         */
        void onSaveClick();
    }

}

