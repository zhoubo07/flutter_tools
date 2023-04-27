package com.dengyun.baselibrary.net.deal;

import android.text.TextUtils;

import com.dengyun.baselibrary.net.NetOption;
import com.dengyun.baselibrary.net.constants.ProjectType;
import com.dengyun.baselibrary.utils.GsonConvertUtil;
import com.dengyun.baselibrary.utils.SharedPreferencesUtil;
import com.dengyun.baselibrary.utils.Utils;
import com.dengyun.baselibrary.utils.phoneapp.AppUtils;

import java.util.Map;

/**
 * @titile 处理参数的工具类（参数添加公共参数、加密）
 * @desc Created by seven on 2018/4/10.
 */

public class DealParamsUtil {
    /**
     * 获取处理过的参数（添加公共参数、加密处理）
     *
     * @return 处理之后的参数Json
     */
    public static String getDealParams(NetOption netOption) {
        if (netOption.getProjectType() == ProjectType.DEFAULT) {
            // 默认当前项目配置参数
            return getDealDefaultParams(netOption);
        } else {
            return GsonConvertUtil.toJson(netOption.getParams());
        }
    }

    /**
     * 获取处理过的url（当请求是get请求时，参数在url上,可能需要拼接url）
     *
     * @return 处理之后的url
     */
    public static void dealUrlForGet(NetOption netOption) {
        if (netOption.getProjectType() == ProjectType.DEFAULT) {
            // 1:添加公共参数
            //doNothing;
        } else {
            //其他项目的暂时没有配置get方式的拼接处理
            //doNothing;
        }
    }

    private static String getDealDefaultParams(NetOption netOption) {
        setDefaultPublicParam(netOption);//添加公共参数
        if (netOption.isEncrypt()) {
            return getDefaultEncryptJson(netOption.getParams());//默认当前项目参数加密
        } else {
            return GsonConvertUtil.toJson(netOption.getParams());
        }
    }


    /**
     * 默认当前项目参数加密
     *
     * @param map 加密之前的map
     * @return 加密之后的参数Json
     */
    private static String getDefaultEncryptJson(Map map) {
        // 2020-08-18 加密方式
        return GsonConvertUtil.toJson(map);
    }

    /**
     * 默认当前项目参数添加公共参数
     */
    private static void setDefaultPublicParam(NetOption netOption) {
        netOption.addHeaders("clientHeaderName", "android");
        netOption.addHeaders("clientHeaderVersion", AppUtils.getAppVersionName());
        String token = SharedPreferencesUtil.getData(Utils.getApp(), "user", "access_token", "");
        if (!TextUtils.isEmpty(token)) netOption.addHeaders("access_token",token);
    }

}
