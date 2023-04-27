package com.dengyun.baselibrary.net.deal;

/**
 * @Title 网络配置
 * @Author: zhoubo
 * @CreateDate: 2019/4/3 10:36 AM
 */
public class NetDealConfig {
    public static NetDealGlobalCode netDealGlobalCode;
    public static NetDealErrorCode netDealErrorCode;
    public static NetDealErrorNetwork netDealErrorNetwork;
    public static NetDealNoNet netDealNoNet;

    public static void setDealGlobalCodeUtil(NetDealGlobalCode netDealGlobalCode){
        NetDealConfig.netDealGlobalCode = netDealGlobalCode;
    }

    public static void setDealErrorCodeUtil(NetDealErrorCode netDealErrorCode){
        NetDealConfig.netDealErrorCode = netDealErrorCode;
    }

    public static void setDealErrorNetworkUtil(NetDealErrorNetwork netDealErrorNetwork){
        NetDealConfig.netDealErrorNetwork = netDealErrorNetwork;
    }

    public static void setDealNoNetUtil(NetDealNoNet netDealNoNet){
        NetDealConfig.netDealNoNet = netDealNoNet;
    }
}
