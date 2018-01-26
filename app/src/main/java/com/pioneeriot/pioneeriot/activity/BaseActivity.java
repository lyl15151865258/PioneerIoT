package com.pioneeriot.pioneeriot.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pioneeriot.pioneeriot.utils.ActivityController;
import com.pioneeriot.pioneeriot.constant.Constant;
import com.pioneeriot.pioneeriot.utils.LogUtils;
import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.utils.ScreenTools;

/**
 * 父类activity
 * Created by Li Yuliang on 2017/2/13 0013.
 *
 * @author LiYuliang
 * @version 2017/10/27
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Toast toast;
    private Dialog dialog;
    protected int mWidth;
    protected int mHeight;
    protected float mDensity;
    protected int mDensityDpi;
    protected int mAvatarSize;
    protected float mRatio;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
        LogUtils.d(LogUtils.TAG, getClass().getSimpleName() + "onCreate() ");
        //保持屏幕常亮（禁止休眠）
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mDensity = dm.density;
        mDensityDpi = dm.densityDpi;
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        mRatio = Math.min((float) mWidth / 720, (float) mHeight / 1280);
        mAvatarSize = (int) (50 * mDensity);
        dialog = new Dialog(this, R.style.loading_dialog);
    }

    /**
     * 沉浸模式View
     *
     * @param views 需要偏移的View控件
     */
    protected void setActionBarLayout(View... views) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for (View view : views) {
                view.setPadding(0, ScreenTools.getStatusBarHeight(this), 0, 0);
            }
        }
    }

    /**
     * 沉浸模式View
     *
     * @param statusBar 状态栏
     */
    protected void setStatusBar(View statusBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            statusBar.setVisibility(View.VISIBLE);
            statusBar.getLayoutParams().height = ScreenTools.getStatusHeight(this);
            statusBar.setLayoutParams(statusBar.getLayoutParams());
        } else {
            statusBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (mInputMethodManager != null) {
                return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }
        }
        return super.onTouchEvent(event);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d(LogUtils.TAG, getClass().getSimpleName() + "onStart() ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.d(LogUtils.TAG, getClass().getSimpleName() + "onRestart() ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(LogUtils.TAG, getClass().getSimpleName() + "onResume() ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //如果toast在显示则取消显示
        cancelDialog();
        LogUtils.d(LogUtils.TAG, getClass().getSimpleName() + "onPause() ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(LogUtils.TAG, getClass().getSimpleName() + "onStop() ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d(LogUtils.TAG, getClass().getSimpleName() + "onDestroy() ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.d(LogUtils.TAG, getClass().getSimpleName() + "onSaveInstanceState() ");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.d(LogUtils.TAG, getClass().getSimpleName() + "onRestoreInstanceState() ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.d(LogUtils.TAG, getClass().getSimpleName() + "onConfigurationChanged() ");
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }

    /**
     * 自定义的Toast，避免重复出现
     *
     * @param msg 需要显示的文本
     */
    public void showToast(String msg) {
        if (toast == null) {
            toast = new Toast(this);
            //获取自定义视图
            View view = LayoutInflater.from(this).inflate(R.layout.view_toast, findViewById(android.R.id.content), false);
            TextView tvMessage = view.findViewById(R.id.tv_toast_text);
            //设置文本
            tvMessage.setText(msg);
            //设置视图
            toast.setView(view);
            //设置显示时长
            toast.setDuration(Toast.LENGTH_SHORT);
        } else {
            View view = LayoutInflater.from(this).inflate(R.layout.view_toast, findViewById(android.R.id.content), false);
            TextView tvMessage = view.findViewById(R.id.tv_toast_text);
            //设置文本
            tvMessage.setText(msg);
            //设置视图
            toast.setView(view);
            //设置显示时长
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null, null);
    }

    public void openActivity(Class<?> pClass, Bundle bundle) {
        openActivity(pClass, bundle, null);
    }

    public void openActivity(Class<?> pClass, Bundle bundle, Uri uri) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (uri != null) {
            intent.setData(uri);
        }
        startActivity(intent);
    }

    public void openActivity(String action) {
        openActivity(action, null, null);
    }

    public void openActivity(String action, Bundle bundle) {
        openActivity(action, bundle, null);
    }

    public void openActivity(String action, Bundle bundle, Uri uri) {
        Intent intent = new Intent(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (uri != null) {
            intent.setData(uri);
        }
        startActivity(intent);
    }

    /**
     * 点击除了EditText以外的所有地方都可以隐藏软键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                AppCompatActivity currentActivity = (AppCompatActivity) ActivityController.getInstance().getCurrentActivity();
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != im) {
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示加载的dialogs
     *
     * @param context    Context对象
     * @param msg        显示的信息
     * @param cancelable 是否可取消
     */
    public void showLoadingDialog(Context context, String msg, boolean cancelable) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_loading, findViewById(android.R.id.content), false);
        LinearLayout layout = view.findViewById(R.id.dialog_view);
        //自定义图片
        ImageView ivImg = view.findViewById(R.id.iv_dialogLoading_img);
        // 提示文字
        TextView tvMsg = view.findViewById(R.id.tv_dialogLoading_msg);
        // 加载动画
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        // 使用ImageView显示动画
        ivImg.startAnimation(animation);
        if (null != msg) {
            // 设置加载信息
            tvMsg.setText(msg);
        }
        // 不可以用“返回键”取消
        dialog.setCancelable(cancelable);
        // 设置布局
        dialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        //显示dialog
        dialog.show();
    }

    /**
     * 取消dialog显示
     */
    public void cancelDialog() {
        if (null != dialog) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ActivityController.activities.size() == 1) {
                //如果activity队列只有本Activity，则提示双击退出
                exit();
            } else {
                //如果activity队列还包含本activity以外的activity，则删除本activity，回退到前一个activity
                ActivityController.finishActivity(this);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出程序的方法（两秒内点击两次返回键退出程序）
     */
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > Constant.EXIT_DOUBLE_CLICK_TIME) {
            showToast(getString(R.string.click_again_exit));
            exitTime = System.currentTimeMillis();
        } else {
            ActivityController.exit();
        }
    }

}
