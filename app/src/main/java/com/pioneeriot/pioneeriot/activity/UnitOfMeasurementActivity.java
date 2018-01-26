package com.pioneeriot.pioneeriot.activity;

import android.os.Bundle;
import android.view.View;

import com.pioneeriot.pioneeriot.widget.MyToolbar;
import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.utils.ActivityController;

/**
 * 计量单位说明
 * Created by Li Yuliang on 2018/01/17.
 *
 * @author LiYuliang
 * @version 2018/01/17
 */

public class UnitOfMeasurementActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        setStatusBar(findViewById(R.id.status_bar));
        MyToolbar toolbar = findViewById(R.id.toolbar_feedback);
        toolbar.initToolBar(this, toolbar, getString(R.string.UnitOfMeasurement), R.drawable.back_white, -1, onClickListener);
    }

    private View.OnClickListener onClickListener = (v) -> {
        switch (v.getId()) {
            case R.id.iv_left:
                ActivityController.finishActivity(this);
                break;
            default:
                break;
        }
    };

}
