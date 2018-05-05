package com.pioneeriot.pioneeriot.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.pioneeriot.pioneeriot.dialog.CommonWarningDialog;
import com.pioneeriot.pioneeriot.utils.NotificationsUtils;
import com.pioneeriot.pioneeriot.widget.MyToolbar;
import com.pioneeriot.pioneeriot.widget.NoScrollViewPager;
import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.fragment.SearchDataSettingFragment;
import com.pioneeriot.pioneeriot.fragment.WaterMeterHistoryDataFragment;
import com.pioneeriot.pioneeriot.fragment.WaterMeterLastDataFragment;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页面
 * Created by LiYuliang on 2018/1/26 0026.
 *
 * @author LiYuliang
 * @version 2018/1/26
 */

public class MainActivity extends BaseActivity {

    private Context context;

    @BindViews({R.id.ll_a, R.id.ll_b, R.id.ll_c})
    LinearLayout[] menus;

    private static NoScrollViewPager viewPager;

    private MyToolbar toolbar;

    private final int REQUEST_CODE_NOTIFICATION_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setNoScroll(false);
        initView();

        if (!NotificationsUtils.isNotificationEnabled(context)) {
            CommonWarningDialog commonWarningDialog = new CommonWarningDialog(context, getString(R.string.notification_open_notification));
            commonWarningDialog.setCancelable(false);
            commonWarningDialog.setOnDialogClickListener(new CommonWarningDialog.OnDialogClickListener() {
                @Override
                public void onOKClick() {
                    // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_CODE_NOTIFICATION_SETTINGS);
                }

                @Override
                public void onCancelClick() {
                    showToast("未授予通知权限，部分功能不可使用");
                }
            });
            commonWarningDialog.show();
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        toolbar = findViewById(R.id.set_main_account_toolbar);
        toolbar.initToolBar(this, toolbar, getString(R.string.LatestData), -1, R.drawable.icon_search, onClickListener);
        viewPager.setAdapter(viewPagerAdapter);
        //设置Fragment预加载，非常重要,可以保存每个页面fragment已有的信息,防止切换后原页面信息丢失
        viewPager.setOffscreenPageLimit(menus.length);
        //刚进来默认选择第一个
        menus[0].setSelected(true);
        //viewPager添加滑动监听，用于控制TextView的展示
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (LinearLayout menu : menus) {
                    menu.setSelected(false);
                }
                menus[position].setSelected(true);
                switch (position) {
                    case 0:
                        toolbar.setTitle(getString(R.string.LatestData));
                        toolbar.setRightButtonImage(R.drawable.icon_search);
                        break;
                    case 1:
                        toolbar.setTitle(getString(R.string.HistoricalData));
                        toolbar.setRightButtonImage(-1);
                        break;
                    case 2:
                        toolbar.setTitle(getString(R.string.Settings));
                        toolbar.setRightButtonImage(-1);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private View.OnClickListener onClickListener = (v) -> {
        switch (v.getId()) {
            case R.id.iv_right:
                WaterMeterLastDataFragment waterMeterLastDataFragment = (WaterMeterLastDataFragment) getSupportFragmentManager().getFragments().get(0);
                waterMeterLastDataFragment.searchWaterMeter(waterMeterLastDataFragment.SEARCH_MODE_NORMAL);
                break;
            default:
                break;
        }
    };

    /**
     * ViewPager适配器
     */
    private FragmentStatePagerAdapter viewPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public int getCount() {
            return menus.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new WaterMeterLastDataFragment();
                case 1:
                    return new WaterMeterHistoryDataFragment();
                case 2:
                    return new SearchDataSettingFragment();
                default:
                    break;
            }
            return null;
        }
    };

    /**
     * 切换到历史数据页面
     *
     * @param meterId 携带的表号
     */
    public void moveToSecondPage(String meterId) {
        viewPager.setCurrentItem(1, false);
        WaterMeterHistoryDataFragment waterMeterHistoryDataFragment = (WaterMeterHistoryDataFragment) getSupportFragmentManager().getFragments().get(1);
        waterMeterHistoryDataFragment.setMeterId(meterId);
    }

    /**
     * TextView点击事件
     *
     * @param linearLayout 被点击控件
     */
    @OnClick({R.id.ll_a, R.id.ll_b, R.id.ll_c})
    public void onClick(LinearLayout linearLayout) {
        for (int i = 0; i < menus.length; i++) {
            menus[i].setSelected(false);
            menus[i].setTag(i);
        }
        //设置选择效果
        linearLayout.setSelected(true);
        //参数false代表瞬间切换，true表示平滑过渡
        viewPager.setCurrentItem((Integer) linearLayout.getTag(), false);
    }

}
