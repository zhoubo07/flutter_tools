package com.dengyun.baselibrary.net.deal;

import com.dengyun.baselibrary.net.NetOption;
import com.dengyun.baselibrary.utils.ToastUtils;

/**
 * @Title 默认的处理全局处理code的实现
 * @Author: zhoubo
 * @CreateDate: 2019-05-14 17:23
 */
public class DefultDealGlobalCode  {
    public static void dealGlobalCode(NetOption netOption, String code, String message) {
        if(null!=message){
            ToastUtils.showShort(message);
        }
    }
}
