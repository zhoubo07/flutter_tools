package com.dengyun.baselibrary.net.deal;

import com.dengyun.baselibrary.net.NetOption;

/**
 * @titile  处理错误code的工具类，（吐司错误msg）
 * @desc Created by seven on 2018/4/12.
 */

public interface NetDealErrorCode {
    void dealErrorCode(NetOption netOption, String code, String errorMsg);
}
