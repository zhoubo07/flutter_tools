package com.zhoubo07.flutter_tools;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import androidx.fragment.app.FragmentActivity;
import com.dengyun.baselibrary.utils.Utils;
import com.dengyun.baselibrary.utils.phoneapp.AppUtils;
import com.idengyun.updatelib.listener.UpdateDataLoader;
import com.idengyun.updatelib.listener.UpdateDataLoaderCallback;
import com.idengyun.updatelib.update.UpdateUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @title 全局启动文件
 * @author: zhoubo
 * @CreateDate: 2021年05月20日17:56:35
 */
public class MyApplication extends io.flutter.app.FlutterApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//         如果需要使用MultiDex，需要在此处调用。
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 暂不判断进程
        // String processName = getCurProcessName(this);
        // if (!TextUtils.isEmpty(processName) && processName.equals("com.saaslander.assisant")) {}
        // 禁止9.0以上手机的反射弹框
        closeAndroidPDialog();
        // 当前应用的初始化
        // 全局的app引用以及初始化common，以及栈管理
        Utils.init(this);
        // 初始化升级模块
        initUpdateModule();
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 升级模块初始化
     */
    private void initUpdateModule() {
        UpdateUtils.init(this, AppUtils.isAppDebug(), R.drawable.push_small, new UpdateDataLoader() {
            @Override
            public void requestUpdateData(FragmentActivity fragmentActivity, String s, boolean b, UpdateDataLoaderCallback updateDataLoaderCallback) {
                // 请求更新信息的逻辑放到Flutter中了，所以UpdateUtils中不能调用请求更新的方法，
                // UpdateUtils中只能调用showDialog的方法
            }

            @Override
            public void exitApp() {
                AppUtils.exitApp();
            }
        });
    }

    /**
     * 适配 android P,禁止弹窗
     */
    private static void closeAndroidPDialog() {
        if (Build.VERSION.SDK_INT < 28) return;
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
