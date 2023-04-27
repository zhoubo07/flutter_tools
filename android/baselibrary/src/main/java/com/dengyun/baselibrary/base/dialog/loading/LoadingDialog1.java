package com.dengyun.baselibrary.base.dialog.loading;

import android.app.Activity;
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

import java.util.HashMap;

/**
 * @titile 请求网络时的缓冲进度圈
 * @Desc: 白框圆圈旋转
 * @desc Created by seven on 2018/3/24.
 */

public class LoadingDialog1 extends BaseDialogFragment {
    @Override
    public int intLayoutId() {
        return R.layout.base_dialog_loading3;
    }

    @Override
    public void convertView(DialogViewHolder holder, BaseDialogFragment dialog) {
//        setWidthDp(100);
//        setHeightDp(100);
        setDimAmount(0);
        setOutCancel(false);
        setBackCancel(true);
        setAnimStyle(R.style.base_dialog_loading_animation);
    }





    /*=======================下方是配置整个加载圈dialog在一个页面中单例的===================*/

    private static final String APPEND_TAG = "BaseLoadingDialog";

    //每个页面的单例存在的dialog
    private static HashMap<String, LoadingDialog1> instanceDialogMap = new HashMap<>();

    public synchronized static LoadingDialog1 getInstance(NetOption netOption) {
        if (null != netOption.getActivity()) {
            return getInstance(netOption.getActivity());
        } else if (null != netOption.getFragment()) {
            return getInstance(netOption.getFragment());
        }else {
            return null;
        }
    }

    public synchronized static LoadingDialog1 getInstance(Activity activity) {
        return getInstance(getLoadingDialogTag(activity));
    }

    public synchronized static LoadingDialog1 getInstance(Fragment fragment) {
        return getInstance(getLoadingDialogTag(fragment));
    }

    private synchronized static LoadingDialog1 getInstance(String loadingTag) {
        if (TextUtils.isEmpty(loadingTag)) return null;
        if (instanceDialogMap.containsKey(loadingTag)) {
            return instanceDialogMap.get(loadingTag);
        } else {
            LoadingDialog1 baseLoadingDialog = new LoadingDialog1();
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
     * 设置显示加载圈的方法，挂载在activity，tag是 ：activity名称+LoadingDialog1
     */
    public LoadingDialog1 show(FragmentActivity activity) {
        show(activity.getSupportFragmentManager(), getLoadingDialogTag(activity));
        return this;
    }

    /**
     * 设置显示加载圈的方法，挂载在fragment，tag是 ：fragment名称+LoadingDialog1
     */
    public LoadingDialog1 show(Fragment fragment) {
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
