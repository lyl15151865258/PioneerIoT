package com.pioneeriot.pioneeriot.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.pioneeriot.pioneeriot.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Li Yuliang on 2017/7/15.
 * 自定义的Toolbar，放弃使用自定义的ActionBar
 *
 * @author LiYuliang
 * @version 2017/11/17
 */

public class MyToolbar extends Toolbar {

    private String titleText;

    @BindView(R.id.iv_left)
    public ImageView leftButton;
    @BindView(R.id.tv_title)
    public MarqueeTextView titleTextView;
    @BindView(R.id.iv_right)
    public ImageView rightButton;

    public MyToolbar(Context context) {
        super(context);
        init(context, null);
    }

    public MyToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.toolbar, this, true);
        ButterKnife.bind(this, view);
        //删除左右默认的padding
        setContentInsetsRelative(0, 0);
        // Load attributes
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ToolbarControl, 0, 0);
        titleText = a.getString(R.styleable.ToolbarControl_titleText);
        titleTextView.setText(titleText);
        a.recycle();
    }

    /**
     * 设置标题
     *
     * @param titleStr 标题
     */
    public void setTitle(String titleStr) {
        if (titleTextView != null) {
            titleTextView.setText(titleStr);
        }
    }

    public void setTitleByResourceId(int rid) {
        if (titleTextView != null) {
            titleTextView.setText(rid);
        }
    }

    public void setLeftButtonImage(int resourceId) {
        if (leftButton != null) {
            if (resourceId == -1) {
                leftButton.setVisibility(INVISIBLE);
            } else {
                leftButton.setImageResource(resourceId);
                leftButton.setVisibility(VISIBLE);
            }
        }
    }

    public void setRightButtonImage(int resourceId) {
        if (rightButton != null) {
            if (resourceId == -1) {
                rightButton.setVisibility(INVISIBLE);
            } else {
                rightButton.setImageResource(resourceId);
                rightButton.setVisibility(VISIBLE);
            }
        }
    }

    public void setLeftButtonOnClickListener(OnClickListener listener) {
        if (leftButton != null && listener != null) {
            leftButton.setOnClickListener(listener);
        }
    }

    public void setRightButtonOnClickListener(OnClickListener listener) {
        if (rightButton != null && listener != null) {
            rightButton.setOnClickListener(listener);
        }
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public void initToolBar(AppCompatActivity appCompatActivity, MyToolbar toolbar, String title, int leftImage, int rightImage, OnClickListener onClickListener) {
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setLeftButtonImage(leftImage);
        toolbar.setLeftButtonOnClickListener(onClickListener);
        toolbar.setRightButtonImage(rightImage);
        toolbar.setRightButtonOnClickListener(onClickListener);
        toolbar.setTitle(title);
    }
}