package com.pioneeriot.pioneeriot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pioneeriot.pioneeriot.R;

/**
 * 水表查询设置查询时间
 * Created by LiYuliang on 2018/1/10 0010.
 *
 * @author LiYuliang
 * @version 2018/01/10
 */

public class ChooseTimeRangeDialog extends Dialog {

    private Context context;
    private OnDialogClickListener dialogClickListener;

    public ChooseTimeRangeDialog(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_choose_time_range, null);
        setContentView(view);
        initWindow();
        findViewById(R.id.btn_ok).setOnClickListener((v) -> {
            if (dialogClickListener != null) {
                dialogClickListener.onOKClick();
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
        void onOKClick();
    }
}

