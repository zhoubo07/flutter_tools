package com.dengyun.baselibrary.net.deal;

import androidx.fragment.app.FragmentManager;

import com.dengyun.baselibrary.base.activity.BaseActivity;
import com.dengyun.baselibrary.base.dialog.BaseDialogFragment;
import com.dengyun.baselibrary.base.dialog.listener.DialogViewHolder;
import com.dengyun.baselibrary.base.dialog.SimpleDialog;
import com.dengyun.baselibrary.base.dialog.listener.OnConfirmListener;
import com.dengyun.baselibrary.base.fragment.BaseFragment;
import com.dengyun.baselibrary.net.NetOption;
import com.dengyun.baselibrary.net.util.NetworkUtils;
import com.dengyun.baselibrary.utils.ToastUtils;

/**
 * @titile  没有网络的处理方式
 * @desc Created by seven on 2018/4/24.
 */

public class DefultDealNoNetUtil {
    public static void dealNoNet(NetOption netOption){
        FragmentManager fragmentManager = null;
        if(netOption.getFragment()!=null&&netOption.getFragment() instanceof BaseFragment){
            fragmentManager = ((BaseFragment) netOption.getFragment()).getFragmentManager();
        }else if(netOption.getActivity()!=null&&netOption.getActivity() instanceof BaseActivity){
            fragmentManager = ((BaseActivity) netOption.getActivity()).getSupportFragmentManager();
        }else {
            ToastUtils.showShort("没有网络");
        }
        if(null!=fragmentManager){
            SimpleDialog.newInstance().setTitle("没有网络")
                    .setCancelText("取消")
                    .setConfirmText("去联网")
                    .setButtonNum(2)
                    .setMessage("请联网操作")
                    .setConfirmListener(new OnConfirmListener() {
                        @Override
                        public void onConfirm(DialogViewHolder holder, BaseDialogFragment dialog) {
                            dialog.dismiss();
                            NetworkUtils.openWirelessSettings();
                        }
                    }).show(fragmentManager);
        }

    }
}
