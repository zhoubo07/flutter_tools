package com.dengyun.baselibrary.net.rx;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * @title:
 * @author: zhoubo
 * @CreateDate: 5/21/21 10:09 AM
 */
public class AndroidSchedulers implements Executor {

    private static AndroidSchedulers instance;

    private final Scheduler mMainScheduler;
    private final Handler mHandler;

    private AndroidSchedulers() {
        mHandler = new Handler(Looper.myLooper());
        mMainScheduler = Schedulers.from(this);
    }

    public static synchronized Scheduler mainThread() {
        if (instance == null) {
            instance = new AndroidSchedulers();
        }
        return instance.mMainScheduler;
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mHandler.post(command);
    }
    
}
