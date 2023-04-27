package com.dengyun.baselibrary.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * @Title handle工具类
 * @Author: zhoubo
 * @CreateDate: 2019-11-23 18:16
 */
public class HandleUtil {
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * @return 获取主线程handle
     */
    public static Handler getMainThreadHandler() {
        return mHandler;
    }
}
