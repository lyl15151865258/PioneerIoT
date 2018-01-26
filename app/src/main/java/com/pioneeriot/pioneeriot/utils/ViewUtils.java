package com.pioneeriot.pioneeriot.utils;

import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;

import com.pioneeriot.pioneeriot.R;

/**
 * 控件辅助工具
 * Created by LiYuliang on 2017/10/20.
 *
 * @author LiYuliang
 * @version 2017/10/19
 */

public class ViewUtils {

    /**
     * 改变密码框可见性
     *
     * @param isInvisible 原先密码框可见性
     * @param editText    密码输入框
     * @param imageView   切换显示/隐藏图标
     */
    public static void changePasswordState(Boolean isInvisible, EditText editText, ImageView imageView) {
        if (isInvisible) {
            imageView.setImageResource(R.drawable.visible_blue);
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            //切换后将EditText光标置于末尾
            CharSequence charSequence = editText.getText();
            if (charSequence != null) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }
        } else {
            imageView.setImageResource(R.drawable.invisible_gray);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            //切换后将EditText光标置于末尾
            setCharSequence(editText.getText());
        }
    }

    /**
     * 将EditText光标置于末尾
     *
     * @param charSequence 有序字符集合
     */
    public static void setCharSequence(CharSequence charSequence) {
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }
}
