package com.dengyun.baselibrary.net.callback;

/**
 * @title 简单状态回调(有返回值)
 * @author: zhoubo
 * @CreateDate: 2020-08-21 10:59
 */
public abstract class SimpleValueCallback<T> {
    public abstract void onSuccess(T t);
    public void onError(String code,String message){}
}
