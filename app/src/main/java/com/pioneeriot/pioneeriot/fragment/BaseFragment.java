package com.pioneeriot.pioneeriot.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.utils.ScreenTools;

import rx.Subscription;

/**
 * fragment基类
 * Created by Li Yuliang on 2017/2/13 0013.
 *
 * @author LiYuliang
 * @version 2017/10/30
 */

public class BaseFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private Toast toast;
    private Dialog dialog;
    protected float mDensity;
    protected int mDensityDpi;
    protected int mWidth;
    protected int mHeight;
    protected float mRatio;
    protected int mAvatarSize;
    protected boolean mIsFirstVisible = true;
    protected ViewGroup viewGroup;
    protected Subscription mSubscription;
    protected LayoutInflater mInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
        dialog = new Dialog(mContext, R.style.loading_dialog);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mDensity = dm.density;
        mDensityDpi = dm.densityDpi;
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        mRatio = Math.min((float) mWidth / 720, (float) mHeight / 1280);
        mAvatarSize = (int) (50 * mDensity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mInflater = inflater;
        this.viewGroup = container;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        boolean isVis = isHidden() || getUserVisibleHint();
        if (isVis && mIsFirstVisible) {
            lazyLoad();
            mIsFirstVisible = false;
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
            statusBar.getLayoutParams().height = ScreenTools.getStatusHeight(getActivity());
            statusBar.setLayoutParams(statusBar.getLayoutParams());
        } else {
            statusBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 数据懒加载
     */
    protected void lazyLoad() {

    }

    @Override
    public void onPause() {
        super.onPause();
        //如果toast在显示则取消显示
        cancelDialog();
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        super.onDestroy();
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
        View view = inflater.inflate(R.layout.dialog_loading, viewGroup);
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
        dialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
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

    public void showToast(int resId) {
        showToast(getString(resId));
    }

    /**
     * 自定义的Toast，避免重复出现
     *
     * @param msg 弹出的信息
     */
    public void showToast(String msg) {
        if (toast == null) {
            toast = new Toast(mContext);
            //获取自定义视图
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_toast, viewGroup);
            TextView tvMessage = view.findViewById(R.id.tv_toast_text);
            //设置文本
            tvMessage.setText(msg);
            //设置视图
            toast.setView(view);
            //设置显示时长
            toast.setDuration(Toast.LENGTH_SHORT);
        } else {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_toast, viewGroup);
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
}
