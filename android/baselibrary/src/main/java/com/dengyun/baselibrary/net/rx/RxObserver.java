package com.dengyun.baselibrary.net.rx;

import com.dengyun.baselibrary.base.dialog.BaseDialogFragment;
import com.dengyun.baselibrary.base.dialog.loading.LoadingDialog1;
import com.dengyun.baselibrary.config.AppConfig;
import com.dengyun.baselibrary.net.NetOption;
import com.dengyun.baselibrary.net.deal.DefultDealNoNetUtil;
import com.dengyun.baselibrary.net.deal.NetDealConfig;
import com.dengyun.baselibrary.net.exception.ApiException;
import com.dengyun.baselibrary.net.exception.NoNetException;
import com.dengyun.baselibrary.utils.AppLogUtil;
import com.dengyun.baselibrary.utils.ObjectUtils;
import com.dengyun.baselibrary.utils.phoneapp.AppUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @titile rx的订阅observer
 * @desc Created by seven on 2018/4/4.
 */

public abstract class RxObserver<T> implements Observer<T> {
    private NetOption netOption;
    private String mKey;
    private BaseDialogFragment loadingDialog;
    private RxManager mRxManager;

    public RxObserver(NetOption netOption) {
        this.netOption = netOption;
        if (null != netOption.getFragment()) {
            mKey = ObjectUtils.getClassPath(netOption.getFragment());
        } else if (null != netOption.getActivity()) {
            mKey = ObjectUtils.getClassPath(netOption.getActivity());
        }
        loadingDialog = netOption.getLoadingDialog();

        mRxManager = RxManager.getInstance();
    }

    @Override
    public void onSubscribe(Disposable d) {
        mRxManager.add(mKey, d);

        if (netOption.isShowDialog() && null != loadingDialog) {
            if (null != netOption.getActivity()) {
                if (loadingDialog instanceof LoadingDialog1)
                    ((LoadingDialog1) loadingDialog).show(netOption.getActivity());
                else loadingDialog.show(netOption.getActivity().getSupportFragmentManager());
            } else if (null != netOption.getFragment() && null != netOption.getFragment().getActivity()) {
                if (loadingDialog instanceof LoadingDialog1)
                    ((LoadingDialog1) loadingDialog).show(netOption.getFragment());
                else
                    loadingDialog.show(netOption.getFragment().getActivity().getSupportFragmentManager());
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        dismissUi(false);
        if (e instanceof NoNetException) {
            onNoNet(e);
            return;
        }
        if (AppUtils.isAppDebug() && !(e instanceof ApiException)) {
            AppLogUtil.setNetErrorLog(netOption.getUrl(), e.getMessage());
        }
        handleError(e);
    }

    public void handleError(Throwable e) {
        ApiException.parseErrorRx(this, netOption, e);
    }

    @Override
    public void onComplete() {
        dismissUi(true);
    }

    public void onNoNet(Throwable e) {
        dismissUi(false);
        if (null != NetDealConfig.netDealNoNet) {
            NetDealConfig.netDealNoNet.dealNoNet(netOption);
        } else {
            DefultDealNoNetUtil.dealNoNet(netOption);
        }
    }

    private void dismissUi(boolean isEnableLoadMore) {
        if (loadingDialog != null && loadingDialog.getShowsDialog()) {
            loadingDialog.dismiss();
        }
    }
}
