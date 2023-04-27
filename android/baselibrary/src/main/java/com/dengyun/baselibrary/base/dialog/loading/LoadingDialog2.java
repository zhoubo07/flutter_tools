package com.dengyun.baselibrary.base.dialog.loading;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dengyun.baselibrary.R;
import com.dengyun.baselibrary.base.dialog.BaseDialogFragment;
import com.dengyun.baselibrary.base.dialog.listener.DialogViewHolder;
import com.dengyun.baselibrary.net.NetOption;
import com.dengyun.baselibrary.utils.ObjectUtils;
import com.dengyun.baselibrary.widgets.GraduallyTextView;

import java.util.HashMap;

/**
 * @titile  请求网络时的缓冲进度圈
 * @Desc: 加载中文字闪烁
 * @desc Created by seven on 2018/3/24.
 */

public class LoadingDialog2 extends BaseDialogFragment {

    GraduallyTextView mGraduallyTextView;
    private String mMessage;

    @Override
    public int intLayoutId() {
        return R.layout.base_dialog_loading2;
    }

    @Override
    public void convertView(DialogViewHolder holder, BaseDialogFragment dialog) {
        mGraduallyTextView = holder.getView(R.id.graduallyTextView);
        if (!TextUtils.isEmpty(mMessage)) mGraduallyTextView.setText(mMessage);
        setWidthDp(150);
        setHeightDp(150);
    }

    //重写onresume方法，绑定activity的生命周期
    @Override
    public void onResume() {
        super.onResume();
        mGraduallyTextView.startLoading();
    }

    //重写onPause方法，绑定activity的生命周期
    @Override
    public void onPause() {
        super.onPause();
        mGraduallyTextView.stopLoading();
    }

    //设置关闭loadingview的方法
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        System.gc();
    }

    public LoadingDialog2 setText(String message){
        this.mMessage = message;
        return this;
    }






    /*=======================下方是配置整个加载圈dialog在一个页面中单例的===================*/

    private static final String APPEND_TAG = "BaseLoadingDialog";

    //每个页面的单例存在的dialog
    private static HashMap<String, LoadingDialog2> instanceDialogMap = new HashMap<>();

    public synchronized static LoadingDialog2 getInstance(NetOption netOption) {
        if (null != netOption.getActivity()) {
            return getInstance(netOption.getActivity());
        } else if (null != netOption.getFragment()) {
            return getInstance(netOption.getFragment());
        }else {
            return null;
        }
    }

    public synchronized static LoadingDialog2 getInstance(Activity activity) {
        return getInstance(getLoadingDialogTag(activity));
    }

    public synchronized static LoadingDialog2 getInstance(Fragment fragment) {
        return getInstance(getLoadingDialogTag(fragment));
    }

    private synchronized static LoadingDialog2 getInstance(String loadingTag) {
        if (TextUtils.isEmpty(loadingTag)) return null;
        if (instanceDialogMap.containsKey(loadingTag)) {
            return instanceDialogMap.get(loadingTag);
        } else {
            LoadingDialog2 baseLoadingDialog = new LoadingDialog2();
            baseLoadingDialog.setLoadingTag(loadingTag);
            instanceDialogMap.put(loadingTag, baseLoadingDialog);
            return baseLoadingDialog;
        }
    }

    private static String getLoadingDialogTag(Object o) {
        return ObjectUtils.getClassPath(o) + APPEND_TAG;
    }

    private String mTag;

    private void setLoadingTag(String tag){
        this.mTag = tag;
    }

    @Deprecated
    @Override
    public BaseDialogFragment show(FragmentManager manager) {
        return super.show(manager);
    }

    @Deprecated
    @Override
    public int show(FragmentTransaction transaction, String tag) {
        return super.show(transaction, tag);
    }

    /**
     * 设置显示加载圈的方法，挂载在activity，tag是 ：activity名称+LoadingDialog2
     */
    public LoadingDialog2 show(FragmentActivity activity) {
        show(activity.getSupportFragmentManager(), getLoadingDialogTag(activity));
        return this;
    }

    /**
     * 设置显示加载圈的方法，挂载在fragment，tag是 ：fragment名称+LoadingDialog2
     */
    public LoadingDialog2 show(Fragment fragment) {
        FragmentManager fragmentManager = fragment.getFragmentManager();
        if (fragmentManager == null) return this;
        show(fragmentManager, getLoadingDialogTag(fragment));
        return this;
    }

    @Override
    public void onDestroyView() {
        instanceDialogMap.remove(mTag);
        super.onDestroyView();
    }

    /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑配置结束↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/
}
