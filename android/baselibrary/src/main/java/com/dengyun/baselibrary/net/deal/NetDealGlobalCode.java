package com.dengyun.baselibrary.net.deal;

import com.dengyun.baselibrary.net.NetOption;

/**
 * @Title 处理需要全局处理的返回code
 * @Author: zhoubo
 * @CreateDate: 2019-05-14 17:23
 */
public interface NetDealGlobalCode {
    void dealGlobalCode(NetOption netOption, String code, String message);
}
