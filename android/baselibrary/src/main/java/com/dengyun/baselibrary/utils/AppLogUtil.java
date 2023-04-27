package com.dengyun.baselibrary.utils;

import com.dengyun.baselibrary.config.AppConfig;
import com.dengyun.baselibrary.utils.phoneapp.AppUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.Map;

/**
 * @titile
 * @desc Created by seven on 2018/5/24.
 */

public class AppLogUtil {
    public static void setActivityLCLog(String activityName, String lcName) {
        if (!AppConfig.isActivityLCLog) return;
        Logger.t("LC_aaaaaa").d(activityName + "-->> %s", lcName);
    }

    public static void setFragmentLCLog(String fragmentName, String lcName) {
        if (!AppConfig.isFragmentLCLog) return;
        Logger.t("LC_ffffff").d(fragmentName + "-->> %s", lcName);
    }

    public static synchronized void setNetResultLog(String url, Map params, String resultJson) {
        if (!AppUtils.isAppDebug()) return;
        Logger.t("wwwwww-u").d(url);
        Logger.t("wwwwww-p").d(GsonConvertUtil.toJson(params));
        Logger.t("wwwwww-r").json(resultJson);
    }

    public static synchronized void setNetErrorLog(String url, String errorMsg) {
        if (!AppUtils.isAppDebug()) return;
        Logger.t("wwwwww-u").e(url);
        Logger.t("wwwwww-e").e(errorMsg);
    }

    public static void initLog() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // （可选）是否显示线程信息。默认值为true
                .methodCount(0)         //（可选）要显示的方法行数。默认值2
//                .methodOffset(1)        //  （可选）隐藏内部方法调用到偏移量。默认5
                .tag("Logger--》")   // （可选）每个日志的全局标签。默认PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return AppUtils.isAppDebug();//如果是Debug  模式 ；打印日志
            }
        });
    }

}
