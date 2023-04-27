package com.dengyun.baselibrary.base;

/**
 * @Title 公共的回调
 * @Desc: 泛型可以传回调回来的结果
 * @Author: zhoubo
 * @CreateDate: 2020-05-29 16:56
 */
public abstract class OnCommonValueListener<T> {
    public void onError(int code, String msg){}

    public abstract void onSuccess(T var1);
}
