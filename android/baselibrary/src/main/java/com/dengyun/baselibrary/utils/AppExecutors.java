package com.dengyun.baselibrary.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 *
 * 主线程 AppExecutors.getInstance().mainThread().execute(new Runnable() {});
 * 磁盘IO线程：AppExecutors.getInstance().diskIO().execute(new Runnable() {);
 * 网络IO线程：AppExecutors.getInstance().networkIO().execute(new Runnable() {);
 * // 定时任务：延时3秒后执行
 * AppExecutors.getInstance().scheduledExecutor().schedule(new Runnable() {},3,TimeUnit.SECONDS);
 * // 定时任务：5秒后启动第一次,每3秒执行一次(第一次执行完成和第二次开始之间间隔3秒)
 * AppExecutors.getInstance().scheduledExecutor().scheduleWithFixedDelay(new Runnable() {}, 5, 3, TimeUnit.MILLISECONDS);
 * // 定时任务：5秒后启动第一次,每3秒执行一次(第一次开始执行和第二次开始执行之间间隔3秒)
 * AppExecutors.getInstance().scheduledExecutor().scheduleAtFixedRate(new Runnable() {}, 5, 3, TimeUnit.MILLISECONDS);
 *
 *
 * @title 全局的线程池工具类
 * @author: zhoubo
 * @CreateDate: 2020-09-17 16:26
 */
public class AppExecutors {
    private static AppExecutors instance;

    // 文件读写线程池（单核，防止文件同步问题）
    private Executor mDiskIO;
    // 网络线程池（多核）
    private Executor mNetworkIO;
    // 定时任务线程池
    private ScheduledThreadPoolExecutor schedule;
    // 主线程
    private Executor mMainThread;

    public static AppExecutors getInstance(){
        if (null == instance){
            synchronized (AppExecutors.class){
                if (null == instance){
                    instance = new AppExecutors();
                }
            }
        }
        return instance;
    }

    private AppExecutors() {
        this.mDiskIO = Executors.newSingleThreadExecutor(new MyThreadFactory("single"));
        this.mNetworkIO = Executors.newFixedThreadPool(3, new MyThreadFactory("fixed"));
        this.mMainThread = new MainThreadExecutor();
        this.schedule = new ScheduledThreadPoolExecutor(5, new MyThreadFactory("sc"), new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 磁盘IO线程池（单线程）
     *
     * 和磁盘操作有关的进行使用此线程(如读写数据库,读写文件)
     * 禁止延迟,避免等待
     * 此线程不用考虑同步问题
     */
    public Executor diskIO() {
        return mDiskIO;
    }

    /**
     * 定时(延时)任务线程池
     *
     * 替代Timer,执行定时任务,延时任务
     */
    public ScheduledThreadPoolExecutor schedule() {
        return schedule;
    }

    /**
     * 网络IO线程池
     *
     * 网络请求,异步任务等适用此线程
     * 不建议在这个线程 sleep 或者 wait
     */
    public Executor networkIO() {
        return mNetworkIO;
    }

    /**
     * UI线程
     *
     * Android 的MainThread
     * UI线程不能做的事情这个都不能做
     */
    public Executor mainThread() {
        return mMainThread;
    }


    class MyThreadFactory implements ThreadFactory {

        private final String name;
        private int count = 0;

        MyThreadFactory(String name) {
            this.name = name;
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            count++;
            return new Thread(r, name + "-" + count + "-Thread");
        }
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
