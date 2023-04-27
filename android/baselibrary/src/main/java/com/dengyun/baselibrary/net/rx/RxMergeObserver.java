package com.dengyun.baselibrary.net.rx;

import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.dengyun.baselibrary.base.dialog.loading.LoadingDialog1;
import com.dengyun.baselibrary.utils.phoneapp.AppUtils;

import java.io.EOFException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @titile
 * @desc Created by seven on 2018/4/8.
 */

public abstract class RxMergeObserver<T> implements Observer<T> {
    private FragmentActivity activity;  //参数传进来的activity
    private String mKey;
    private boolean isShowDialog;   //是否显示缓冲菊花
    private int mWhichRequest;
    private LoadingDialog1 loadingDialog;
    private RxManager mRxManager;
    public RxMergeObserver(Fragment fragment, int whichRequest, boolean isShowDialog){
        this(fragment.getActivity(),
                AppUtils.getAppPackageName() + "." + fragment.getClass().getSimpleName(),
                whichRequest,
                isShowDialog);
    }
    public RxMergeObserver(Fragment fragment, int whichRequest){
        this(fragment.getActivity(),
                AppUtils.getAppPackageName() + "." + fragment.getClass().getSimpleName(),
                whichRequest,
                false);
    }

    public RxMergeObserver(FragmentActivity activity, int whichRequest, boolean isShowDialog){
        this(activity,
                AppUtils.getAppPackageName() + "." + activity.getClass().getSimpleName(),
                whichRequest,
                isShowDialog);
    }

    public RxMergeObserver(FragmentActivity activity, int whichRequest){
        this(activity,
                AppUtils.getAppPackageName() + "." + activity.getClass().getSimpleName(),
                whichRequest,
                false);
    }

    public RxMergeObserver(FragmentActivity activity, String key, int whichRequest, boolean isShowDialog) {
        this.activity = activity;
        this.mKey = key;
        this.isShowDialog = isShowDialog;
        this.mWhichRequest = whichRequest;
        if(isShowDialog){
            if(null==loadingDialog){
                loadingDialog = new LoadingDialog1();
            }
        }
        mRxManager = RxManager.getInstance();
    }

    public String getKey(Class c){
        return AppUtils.getAppPackageName() + "." + c.getClass().getSimpleName();
    }

    @Override
    public void onSubscribe(Disposable d) {
        mRxManager.add(mKey, d);
        if (isShowDialog) {
            if(null==loadingDialog){
                loadingDialog = new LoadingDialog1();
            }
            loadingDialog.show(activity);
        }
        onStart(mWhichRequest);
    }

    @Override
    public final void onNext(T value) {
        onSuccess(mWhichRequest, value);
    }

    @Override
    public void onError(Throwable e) {
        if (loadingDialog != null && loadingDialog.getShowsDialog()) {
            loadingDialog.dismiss();
        }
        if (e instanceof EOFException || e instanceof ConnectException || e instanceof SocketException || e instanceof BindException || e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
            Toast.makeText(activity, "网络异常，请稍后重试！", Toast.LENGTH_SHORT).show();
        } /*else if (e instanceof ApiException) {
            onError(mWhichRequest, e);
        }*/ else {
            Toast.makeText(activity, "未知错误！", Toast.LENGTH_SHORT).show();
        }
        onError(mWhichRequest,e);
    }

    @Override
    public void onComplete() {
        if (loadingDialog != null && loadingDialog.getShowsDialog()) {
            loadingDialog.dismiss();
        }
    }

    public abstract void onSuccess(int whichRequest, T t);

    public void onError(int whichRequest, Throwable e){

    }

    public void onStart(int whichRequest) {

    }
}
