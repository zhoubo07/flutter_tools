package com.dengyun.baselibrary.net.callback;

/**
 * @title 简单状态回调(没有返回值)
 * @author: zhoubo
 * @CreateDate: 2020-09-29 14:53
 */
public abstract class SimpleStatusCallback {
    public abstract void onSuccess();
    public void onError(String code,String message){}
}
