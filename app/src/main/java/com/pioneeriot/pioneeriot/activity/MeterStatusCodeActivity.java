package com.pioneeriot.pioneeriot.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.utils.ActivityController;
import com.pioneeriot.pioneeriot.widget.MyToolbar;

/**
 * 水表状态码
 * Created by Li Yuliang on 2018/01/31.
 *
 * @author LiYuliang
 * @version 2018/01/31
 */

public class MeterStatusCodeActivity extends BaseActivity {

    private CheckBox cbError1, cbError2, cbError3, cbError4, cbError5, cbError6, cbError7, cbError8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_status_code);
        setStatusBar(findViewById(R.id.status_bar));
        MyToolbar toolbar = findViewById(R.id.toolbar_feedback);
        toolbar.initToolBar(this, toolbar, getString(R.string.MeterStatusCode), R.drawable.back_white, -1, onClickListener);
        EditText etStatusCode = findViewById(R.id.et_statusCode);
        cbError1 = findViewById(R.id.cb_error1);
        cbError2 = findViewById(R.id.cb_error2);
        cbError3 = findViewById(R.id.cb_error3);
        cbError4 = findViewById(R.id.cb_error4);
        cbError5 = findViewById(R.id.cb_error5);
        cbError6 = findViewById(R.id.cb_error6);
        cbError7 = findViewById(R.id.cb_error7);
        cbError8 = findViewById(R.id.cb_error8);
        etStatusCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 2) {
                    int m = Integer.valueOf(charSequence.toString(), 16);
                    if ((m & 0x01) == 0x01) {
                        cbError1.setChecked(true);
                    } else if ((m & 0x01) == 0x00) {
                        cbError1.setChecked(false);
                    }
                    if ((m & 0x02) == 0x02) {
                        cbError2.setChecked(true);
                    } else if ((m & 0x02) == 0x00) {
                        cbError2.setChecked(false);
                    }
                    if ((m & 0x04) == 0x04) {
                        cbError3.setChecked(true);
                    } else if ((m & 0x04) == 0x00) {
                        cbError3.setChecked(false);
                    }
                    if ((m & 0x08) == 0x08) {
                        cbError4.setChecked(true);
                    } else if ((m & 0x08) == 0x00) {
                        cbError4.setChecked(false);
                    }
                    if ((m & 0x10) == 0x10) {
                        cbError5.setChecked(true);
                    } else if ((m & 0x10) == 0x00) {
                        cbError5.setChecked(false);
                    }
                    if ((m & 0x20) == 0x20) {
                        cbError6.setChecked(true);
                    } else if ((m & 0x20) == 0x00) {
                        cbError6.setChecked(false);
                    }
                    if ((m & 0x40) == 0x40) {
                        cbError7.setChecked(true);
                    } else if ((m & 0x40) == 0x00) {
                        cbError7.setChecked(false);
                    }
                    if ((m & 0x80) == 0x80) {
                        cbError8.setChecked(true);
                    } else if ((m & 0x80) == 0x00) {
                        cbError8.setChecked(false);
                    }
                } else {
                    cbError1.setChecked(false);
                    cbError2.setChecked(false);
                    cbError3.setChecked(false);
                    cbError4.setChecked(false);
                    cbError5.setChecked(false);
                    cbError6.setChecked(false);
                    cbError7.setChecked(false);
                    cbError8.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
