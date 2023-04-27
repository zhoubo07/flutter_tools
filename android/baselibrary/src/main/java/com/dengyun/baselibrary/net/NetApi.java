package com.dengyun.baselibrary.net;

import com.dengyun.baselibrary.config.AppConfig;
import com.dengyun.baselibrary.net.callback.JsonCallback;
import com.dengyun.baselibrary.net.constants.RequestMethod;
import com.dengyun.baselibrary.net.deal.DealParamsUtil;
import com.dengyun.baselibrary.net.exception.NoNetException;
import com.dengyun.baselibrary.net.util.JsonConvert2;
import com.dengyun.baselibrary.net.util.NetworkUtils;
import com.dengyun.baselibrary.utils.ObjectUtils;
import com.dengyun.baselibrary.utils.phoneapp.AppUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okrx2.adapter.ObservableBody;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.Response;

/**
 * getData          普通post网络请求，传泛型类
 * getString        普通post网络请求，不传泛型，回调json
 * getStringRX      rx+okgo的post请求，不传泛型，回调json
 * getDataRX        rx+okgo的post请求，传泛型类
 * displayImage     网络图片设置方法
 * upFile           okgo的上传方法
 *
 * @titile 网络工具类
 * @desc Created by seven on 2018/2/27.
 */

public class NetApi {

    /**
     * 网络请求封装，不传泛型，回调返回Json，普通okgo形式请求
     */
    public static void getString(NetOption netOption, JsonCallback<String> jsonCallback) {
        getData(netOption, jsonCallback);
    }

    public static Response getStringSync(NetOption netOption) {
        return getDataSync(netOption);
    }

    /**
     * 网络请求封装，传泛型类，回调返回泛型类普通okgo形式请求
     *
     * @param netOption    配置参数类
     * @param jsonCallback 回调
     * @param <T>
     */
    public static <T> void getData(NetOption netOption, JsonCallback<T> jsonCallback) {
        if (!NetworkUtils.isConnected()) {
            jsonCallback.onNoNet();
            return;
        }
        switch (netOption.getRequestMethod()) {
            case RequestMethod.GET:
                requestForGet(netOption, jsonCallback);
                break;
            case RequestMethod.POST_JSON:
                requestForPostJson(netOption, jsonCallback);
                break;
            case RequestMethod.POST_FORM:
                requestForPostForm(netOption, jsonCallback);
                break;
            case RequestMethod.PUT_JSON:
                requestForPutJson(netOption, jsonCallback);
                break;
            case RequestMethod.PUT_FORM:
                requestForPutForm(netOption, jsonCallback);
                break;
            default:
        }
    }

    /**
     * 网络请求封装 同步请求
     *
     * @param netOption 配置参数类
     * @param <T>
     */
    public static <T> Response getDataSync(NetOption netOption) {
        switch (netOption.getRequestMethod()) {
            case RequestMethod.GET:
                return requestForGetSync(netOption);
            case RequestMethod.POST_JSON:
                return requestForPostJsonSync(netOption);
            case RequestMethod.POST_FORM:
                return requestForPostFormSync(netOption);
            case RequestMethod.PUT_JSON:
                return requestForPutJsonSync(netOption);
            case RequestMethod.PUT_FORM:
                return requestForPutFormSync(netOption);
            default:
                return requestForGetSync(netOption);
        }
    }

    /**
     * 网络请求封装，不传泛型，使用rxjava形式返回observable
     */
    public static Observable<String> getStringRX(NetOption netOption) {
        return getDataRX(netOption);
    }

    /**
     * 网络请求封装，传泛型ApiBean类，使用rxjava形式返回observable
     *
     * @param netOption
     * @param <T>
     * @return
     */
    public static <T> Observable<T> getDataRX(NetOption netOption) {
        if (!NetworkUtils.isConnected()) {
            return Observable.error(new NoNetException());
        }
        switch (netOption.getRequestMethod()) {
            case RequestMethod.GET:
                return requestForGetRX(netOption);
            case RequestMethod.POST_JSON:
                return requestForPostJsonRX(netOption);
            case RequestMethod.POST_FORM:
                return requestForPostFormRX(netOption);
            case RequestMethod.PUT_JSON:
                return requestForPutJsonRX(netOption);
            case RequestMethod.PUT_FORM:
                return requestForPutFormRX(netOption);
            default:
                return requestForGetRX(netOption);
        }
    }

    /**
     *   /////////////////////////////////////////////////////////////////////////////////
     *   //
     *   //                      上传的api方法
     *   //
     *   /////////////////////////////////////////////////////////////////////////////////
     */

    /**
     * 单文件上传
     */
    public static <T> void upFileData(NetOption netOption, File file, JsonCallback<T> jsonCallback) {
        if (!NetworkUtils.isConnected()) {
            jsonCallback.onNoNet();
            return;
        }
        String jsonParams = DealParamsUtil.getDealParams(netOption);
        pringLog(netOption.getUrl(), jsonParams);
        OkGo.<T>post(netOption.getUrl())
                .tag(getRequestTag(netOption))
                .headers(netOption.getHeaders())
                .params("file", file)
                .execute(jsonCallback);
    }

    /**
     * 多文件上传
     */
    public static <T> void upFileListData(NetOption netOption, List<File> files, JsonCallback<T> jsonCallback) {
        if (!NetworkUtils.isConnected()) {
            jsonCallback.onNoNet();
            return;
        }
        String jsonParams = DealParamsUtil.getDealParams(netOption);
        pringLog(netOption.getUrl(), jsonParams);
        OkGo.<T>post(netOption.getUrl())
                .tag(getRequestTag(netOption))
                .isMultipart(true)
                .headers(netOption.getHeaders())
                .addFileParams("files", files)
                .execute(jsonCallback);
    }

    /**
     * @param tag 取消某个tag的请求
     */
    public static void cancelTag(Object tag) {
        OkGo.getInstance().cancelTag(tag);
    }

    /**
     * 取消所有请求
     */
    public static void cancelAll() {
        OkGo.getInstance().cancelAll();
    }

    /**
     * @return 设置请求的tag，用于之后的取消请求，默认为当前请求的activity或Fragment的类名
     */
    public static String getRequestTag(NetOption netOption) {
        //设置请求的key，用于之后的取消，默认为当前activity或fragment的类名
        String mKey = netOption.getUrl();
        if (null != netOption.getTag()) {
            mKey = netOption.getTag();
        } else {
            if (null != netOption.getFragment()) {
                mKey = ObjectUtils.getClassPath(netOption.getFragment());
            } else if (null != netOption.getActivity()) {
                mKey = ObjectUtils.getClassPath(netOption.getActivity());
            }
        }
        return mKey;
    }


    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      不同的请求方式的单个api
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */

    private static <T> void requestForGet(NetOption netOption, JsonCallback<T> jsonCallback) {
        DealParamsUtil.dealUrlForGet(netOption);
        pringLog(netOption.getUrl());
        OkGo.<T>get(netOption.getUrl())
                .tag(getRequestTag(netOption))
                .headers(netOption.getHeaders())
                .execute(jsonCallback);
    }

    private static <T> Response requestForGetSync(NetOption netOption) {
        DealParamsUtil.dealUrlForGet(netOption);
        pringLog(netOption.getUrl());
        try {
            return OkGo.<T>get(netOption.getUrl())
                    .tag(getRequestTag(netOption))
                    .headers(netOption.getHeaders())
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <T> Observable<T> requestForGetRX(NetOption netOption) {
        DealParamsUtil.dealUrlForGet(netOption);
        pringLog(netOption.getUrl());
        return OkGo.<T>get(netOption.getUrl())
                .tag(NetApi.getRequestTag(netOption))
                .headers(netOption.getHeaders())
                .converter(new JsonConvert2<T>(netOption))
                .adapt(new ObservableBody<T>());
    }

    private static <T> void requestForPostJson(NetOption netOption, JsonCallback<T> jsonCallback) {
        String jsonParams = DealParamsUtil.getDealParams(netOption);
        pringLog(netOption.getUrl(), jsonParams);
        OkGo.<T>post(netOption.getUrl())
                .tag(getRequestTag(netOption))
                .headers(netOption.getHeaders())
                .upJson(jsonParams)
                .execute(jsonCallback);
    }

    private static <T> Response requestForPostJsonSync(NetOption netOption) {
        String jsonParams = DealParamsUtil.getDealParams(netOption);
        pringLog(netOption.getUrl(), jsonParams);
        try {
            return OkGo.<T>post(netOption.getUrl())
                    .tag(getRequestTag(netOption))
                    .headers(netOption.getHeaders())
                    .upJson(jsonParams)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <T> Observable<T> requestForPostJsonRX(NetOption netOption) {
        String jsonParams = DealParamsUtil.getDealParams(netOption);
        pringLog(netOption.getUrl(), jsonParams);
        return OkGo.<T>post(netOption.getUrl())
                .tag(NetApi.getRequestTag(netOption))
                .headers(netOption.getHeaders())
                .upJson(jsonParams)
                .converter(new JsonConvert2<T>(netOption))
                .adapt(new ObservableBody<T>());
    }

    private static <T> void requestForPostForm(NetOption netOption, JsonCallback<T> jsonCallback) {
        String jsonParams = DealParamsUtil.getDealParams(netOption);
        pringLog(netOption.getUrl(), jsonParams);
        HttpParams httpParams = new HttpParams();
        httpParams.put(netOption.getParams());
        OkGo.<T>post(netOption.getUrl())
                .tag(getRequestTag(netOption))
                .headers(netOption.getHeaders())
                .params(httpParams)
                .execute(jsonCallback);
    }

    private static <T> Response requestForPostFormSync(NetOption netOption) {
        String jsonParams = DealParamsUtil.getDealParams(netOption);
        pringLog(netOption.getUrl(), jsonParams);

        HttpParams httpParams = new HttpParams();
        httpParams.put(netOption.getParams());
        try {
            return OkGo.<T>post(netOption.getUrl())
                    .tag(getRequestTag(netOption))
                    .headers(netOption.getHeaders())
                    .params(httpParams)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <T> Observable<T> requestForPostFormRX(NetOption netOption) {
        String jsonParams = DealParamsUtil.getDealParams(netOption);
        pringLog(netOption.getUrl(), jsonParams);
        HttpParams httpParams = new HttpParams();
        httpParams.put(netOption.getParams());
        return OkGo.<T>post(netOption.getUrl())
                .tag(NetApi.getRequestTag(netOption))
                .headers(netOption.getHeaders())
                .params(httpParams)
                .converter(new JsonConvert2<T>(netOption))
                .adapt(new ObservableBody<T>());
    }

    private static <T> void requestForPutJson(NetOption netOption, JsonCallback<T> jsonCallback) {
        String jsonParams = DealParamsUtil.getDealParams(netOption);
        pringLog(netOption.getUrl(), jsonParams);
        OkGo.<T>put(netOption.getUrl())
                .tag(getRequestTag(netOption))
                .headers(netOption.getHeaders())
                .upJson(jsonParams)
                .execute(jsonCallback);
    }

    private static <T> Response requestForPutJsonSync(NetOption netOption) {
        String jsonParams = DealParamsUtil.getDealParams(netOption);
        pringLog(netOption.getUrl(), jsonParams);
        try {
            return OkGo.<T>put(netOption.getUrl())
                    .tag(getRequestTag(netOption))
                    .headers(netOption.getHeaders())
                    .upJson(jsonParams)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <T> Observable<T> requestForPutJsonRX(NetOption netOption) {
        String jsonParams = DealParamsUtil.getDealParams(netOption);
        pringLog(netOption.getUrl(), jsonParams);
        return OkGo.<T>put(netOption.getUrl())
                .tag(NetApi.getRequestTag(netOption))
                .headers(netOption.getHeaders())
                .upJson(jsonParams)
                .converter(new JsonConvert2<T>(netOption))
                .adapt(new ObservableBody<T>());
    }

    private static <T> void requestForPutForm(NetOption netOption, JsonCallback<T> jsonCallback) {
        String jsonParams = DealParamsUtil.getDealParams(netOption);
        pringLog(netOption.getUrl(), jsonParams);
        HttpParams httpParams = new HttpParams();
        httpParams.put(netOption.getParams());
        OkGo.<T>put(netOption.getUrl())
                .tag(getRequestTag(netOption))
                .headers(netOption.getHeaders())
                .params(httpParams)
                .execute(jsonCallback);
    }

    private static <T> Response requestForPutFormSync(NetOption netOption) {
        String jsonParams = DealParamsUtil.getDealParams(netOption);
        pringLog(netOption.getUrl(), jsonParams);

        HttpParams httpParams = new HttpParams();
        httpParams.put(netOption.getParams());
        try {
            return OkGo.<T>put(netOption.getUrl())
                    .tag(getRequestTag(netOption))
                    .headers(netOption.getHeaders())
                    .params(httpParams)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <T> Observable<T> requestForPutFormRX(NetOption netOption) {
        String jsonParams = DealParamsUtil.getDealParams(netOption);
        pringLog(netOption.getUrl(), jsonParams);
        HttpParams httpParams = new HttpParams();
        httpParams.put(netOption.getParams());
        return OkGo.<T>put(netOption.getUrl())
                .tag(NetApi.getRequestTag(netOption))
                .headers(netOption.getHeaders())
                .params(httpParams)
                .converter(new JsonConvert2<T>(netOption))
                .adapt(new ObservableBody<T>());
    }
    
    private static void pringLog(String url, String jsonParams) {
        if (!AppUtils.isAppDebug()) return;
        Logger.t("real-request").d(url);
        Logger.t("real-request").d(jsonParams);
    }

    private static void pringLog(String url) {
        if (!AppUtils.isAppDebug()) return;
        Logger.t("real-request").d(url);
    }
}