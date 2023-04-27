package com.dengyun.baselibrary.net.deal;

import com.dengyun.baselibrary.net.NetOption;
import com.dengyun.baselibrary.utils.ToastUtils;

/**
 * @Title 默认处理错误code的方式
 * @Author: zhoubo
 * @CreateDate: 2019/4/3 2:40 PM
 */
public class DefultDealErrorCode {
    public static void dealErrorCode(NetOption netOption, String code, String errorMsg) {
        ToastUtils.showShort(errorMsg);
    }
}
