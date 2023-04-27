package com.dengyun.baselibrary.net.deal;

import androidx.fragment.app.FragmentManager;

import com.dengyun.baselibrary.base.activity.BaseActivity;
import com.dengyun.baselibrary.base.dialog.BaseDialogFragment;
import com.dengyun.baselibrary.base.dialog.listener.DialogViewHolder;
import com.dengyun.baselibrary.base.dialog.SimpleDialog;
import com.dengyun.baselibrary.base.dialog.listener.OnConfirmListener;
import com.dengyun.baselibrary.base.fragment.BaseFragment;
import com.dengyun.baselibrary.net.NetApi;
import com.dengyun.baselibrary.net.NetOption;
import com.dengyun.baselibrary.net.callback.JsonCallback;

import com.dengyun.baselibrary.net.rx.RxObserver;
import com.dengyun.baselibrary.net.rx.RxSchedulers;
import com.dengyun.baselibrary.utils.ToastUtils;

/**
 * @Title 默认处理网络问题的类
 * @Author: zhoubo
 * @CreateDate: 2019/4/3 2:47 PM
 */
public class DefultDealErrorNetwork {
    public static void dealErrorNetworkRx(final RxObserver rxObserver, final NetOption netOption, Throwable e){
        FragmentManager fragmentManager = null;
        if(netOption.getFragment()!=null&&netOption.getFragment() instanceof BaseFragment){
            fragmentManager = ((BaseFragment) netOption.getFragment()).getFragmentManager();
        }else if(netOption.getActivity()!=null&&netOption.getActivity() instanceof BaseActivity){
            fragmentManager = ((BaseActivity) netOption.getActivity()).getSupportFragmentManager();
        }else {
            ToastUtils.showShort("网络异常，请稍后重试！");
        }
        if(null!=fragmentManager){
            SimpleDialog.newInstance().setTitle("加载失败")
                    .setCancelText("取消")
                    .setConfirmText("加载")
                    .setButtonNum(2)
                    .setMessage("网络异常，是否重新加载？")
                    .setConfirmListener(new OnConfirmListener() {
                        @Override
                        public void onConfirm(DialogViewHolder holder, BaseDialogFragment dialog) {
                            dialog.dismiss();
                            NetApi.getDataRX(netOption)
                                    .compose(RxSchedulers.io_main())
                                    .subscribe(rxObserver);

                        }
                    }).show(fragmentManager);
        }
    }

    public static void dealErrorNetwork(final JsonCallback jsonCallback, final NetOption netOption, Throwable e){
        FragmentManager fragmentManager = null;
        if(netOption.getFragment()!=null&&netOption.getFragment() instanceof BaseFragment){
            fragmentManager = ((BaseFragment) netOption.getFragment()).getFragmentManager();
        }else if(netOption.getActivity()!=null&&netOption.getActivity() instanceof BaseActivity){
            fragmentManager = ((BaseActivity) netOption.getActivity()).getSupportFragmentManager();
        }else {
            ToastUtils.showShort("网络异常，请稍后重试！");
        }
        if(null!=fragmentManager){
            SimpleDialog.newInstance().setTitle("加载失败")
                    .setCancelText("取消")
                    .setConfirmText("加载")
                    .setButtonNum(2)
                    .setMessage("网络异常，是否重新加载？")
                    .setConfirmListener(new OnConfirmListener() {
                        @Override
                        public void onConfirm(DialogViewHolder holder, BaseDialogFragment dialog) {
                            dialog.dismiss();
                            NetApi.getData(netOption,jsonCallback);
                        }
                    }).show(fragmentManager);
        }
    }



    /*if(netOption.getFragment()!=null&&netOption.getFragment() instanceof BaseFragment){
            ((BaseFragment) netOption.getFragment()).showError();
            ((BaseFragment) netOption.getFragment()).setOnErrorRetryClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }else if(netOption.getActivity()!=null&&netOption.getActivity() instanceof BaseActivity){
            ((BaseActivity) netOption.getActivity()).showError();
            ((BaseActivity) netOption.getActivity()).setOnEmptyRetryClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }else {

        }*/
}
