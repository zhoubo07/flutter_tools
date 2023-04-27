package com.dengyun.baselibrary.net.exception;

import com.dengyun.baselibrary.net.NetOption;
import com.dengyun.baselibrary.net.constants.ReturnCodeConstants;
import com.dengyun.baselibrary.net.callback.JsonCallback;
import com.dengyun.baselibrary.net.deal.DefultDealErrorCode;
import com.dengyun.baselibrary.net.deal.DefultDealErrorNetwork;
import com.dengyun.baselibrary.net.deal.DefultDealGlobalCode;
import com.dengyun.baselibrary.net.deal.NetDealConfig;
import com.dengyun.baselibrary.net.rx.RxObserver;
import com.dengyun.baselibrary.utils.ToastUtils;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @titile  自定义错误code类
 * @desc Created by seven on 2018/4/12.
 */

public class ApiException extends Exception {
    private String code;
    public ApiException(String message,String code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public static void parseErrorRx(RxObserver rxObserver, NetOption netOption, Throwable e){
        if (e instanceof EOFException || e instanceof ConnectException || e instanceof SocketException || e instanceof BindException || e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
            //网络错误
            if(null!= NetDealConfig.netDealErrorNetwork){
                NetDealConfig.netDealErrorNetwork.dealErrorNetwork(netOption);
            }else {
                DefultDealErrorNetwork.dealErrorNetworkRx(rxObserver,netOption,e);
            }
        } else if(e instanceof IOException){
            //io错误
            dealIOException();
        }else if(e instanceof JsonIOException || e instanceof JsonSyntaxException){
            //解析错误
            dealJsonIo();
        } else if (e instanceof ApiException) {
            //错误code
            dealErrorCode(netOption,e);
        } else {
            ToastUtils.showShort("未知错误！");
        }
    }

    public static void parseError(JsonCallback jsonCallback, NetOption netOption, Throwable e){
        if (e instanceof EOFException || e instanceof ConnectException || e instanceof SocketException || e instanceof BindException || e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
            //网络错误
            if(null!= NetDealConfig.netDealErrorNetwork){
                NetDealConfig.netDealErrorNetwork.dealErrorNetwork(netOption);
            }else {
                DefultDealErrorNetwork.dealErrorNetwork(jsonCallback,netOption,e);
            }
        } else if(e instanceof IOException){
            //io错误
            dealIOException();
        }else if(e instanceof JsonIOException || e instanceof JsonSyntaxException){
            //解析错误
            dealJsonIo();
        } else if (e instanceof ApiException) {
            //错误code
            dealErrorCode(netOption,e);
        } else {
            ToastUtils.showShort("未知错误！");
        }
    }

    /**
     * 处理io错误
     */
    private static void dealIOException(){
        ToastUtils.showShort("io错误");
    }

    /**
     * 处理解析错误
     */
    private static void dealJsonIo(){
        ToastUtils.showShort("解析错误");
    }

    /**
     * 处理错误code
     */
    private static void dealErrorCode(NetOption netOption, Throwable e) {
        ApiException mApiE = (ApiException) e;
        if(ReturnCodeConstants.isContainsGlobalDealCode(netOption,mApiE.getCode())){
            //全局处理的code
            if(null!= NetDealConfig.netDealGlobalCode){
                NetDealConfig.netDealGlobalCode.dealGlobalCode(netOption,mApiE.getCode(),e.getMessage());
            }else {
                DefultDealGlobalCode.dealGlobalCode(netOption,mApiE.getCode(),e.getMessage());
            }
        }else {
            if(null!= NetDealConfig.netDealErrorCode){
                NetDealConfig.netDealErrorCode.dealErrorCode(netOption,mApiE.getCode(),e.getMessage());
            }else {
                DefultDealErrorCode.dealErrorCode(netOption,mApiE.getCode(),e.getMessage());
            }
        }
    }
}
