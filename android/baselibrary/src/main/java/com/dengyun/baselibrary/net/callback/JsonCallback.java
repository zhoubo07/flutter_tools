/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dengyun.baselibrary.net.callback;

import com.dengyun.baselibrary.base.dialog.BaseDialogFragment;
import com.dengyun.baselibrary.base.dialog.loading.LoadingDialog1;
import com.dengyun.baselibrary.config.AppConfig;
import com.dengyun.baselibrary.net.NetOption;
import com.dengyun.baselibrary.net.deal.DefultDealNoNetUtil;
import com.dengyun.baselibrary.net.deal.NetDealConfig;
import com.dengyun.baselibrary.net.exception.ApiException;
import com.dengyun.baselibrary.net.util.JsonConvert2;
import com.dengyun.baselibrary.utils.AppLogUtil;
import com.dengyun.baselibrary.utils.phoneapp.AppUtils;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.base.Request;

import okhttp3.Response;

/**
 * @titile 网络请求返回的回调，可以是bean,
 * @desc Created by seven on 2018/2/27.
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    private NetOption netOption;//参数菜单
    private BaseDialogFragment loadingDialog;

    public JsonCallback(NetOption netOption) {
        this.netOption = netOption;
        loadingDialog = netOption.getLoadingDialog();
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现
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
    public void onError(com.lzy.okgo.model.Response<T> response) {
        super.onError(response);
        if (AppUtils.isAppDebug() && !(response.getException() instanceof ApiException)) {
            AppLogUtil.setNetErrorLog(netOption.getUrl(),response.getException().getMessage());
        }
        dismissUi(false);
        handleError(response);

    }

    public void handleError(com.lzy.okgo.model.Response<T> response) {
        ApiException.parseError(this, netOption, response.getException());
    }

    public void onNoNet() {
        dismissUi(false);
        if (null != NetDealConfig.netDealNoNet) {
            NetDealConfig.netDealNoNet.dealNoNet(netOption);
        } else {
            DefultDealNoNetUtil.dealNoNet(netOption);
        }

    }

    @Override
    public T convertResponse(Response response) throws Throwable {
        JsonConvert2<T> convert = new JsonConvert2<T>(netOption);
        return convert.convertResponse(response);
    }

    @Override
    public void onFinish() {
        super.onFinish();
        dismissUi(true);
    }

    private void dismissUi(boolean isEnableLoadMore) {
        if (loadingDialog != null && loadingDialog.getShowsDialog()) {
            loadingDialog.dismiss();
        }
    }
}
