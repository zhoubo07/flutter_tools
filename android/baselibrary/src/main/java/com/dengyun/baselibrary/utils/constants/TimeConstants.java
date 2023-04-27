package com.dengyun.baselibrary.utils.constants;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 时间常量类，注解是指常量类代替枚举类
 */
public final class TimeConstants {

    public static final int MSEC = 1;   //毫秒值
    public static final int SEC  = 1000; //转换成秒值
    public static final int MIN  = 60000;//转换成分钟
    public static final int HOUR = 3600000;//转换成小时
    public static final int DAY  = 86400000;//转换成天

    @IntDef({MSEC, SEC, MIN, HOUR, DAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Unit {
    }
}
