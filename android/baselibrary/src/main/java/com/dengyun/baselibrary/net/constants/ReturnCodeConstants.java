package com.dengyun.baselibrary.net.constants;

import com.dengyun.baselibrary.net.NetOption;

/**
 * @titile
 * @desc Created by seven on 2018/4/4.
 */

public class ReturnCodeConstants {

    /*------------------------------------------default code常量--------------------------------------*/

    //默认当前项目全局拦截code，在application中全局配置拦截中操作
    private static String[] DEFAULT_DEAL_GLOBAL_CODES = {"9010001"};
    //默认当前项目需要处理的code，返回到success，例如成功、升级
    private static String[] DEFAULT_DEAL_CODES = {
            "200",  // 请求成功
    };



    /**
     * 是否是需要返回处理的code
     * 这些会返回到onSuccess里，当做正确返回，和正常的success一样
     * @param netOption
     * @param code
     * @return
     */
    public static boolean isContainsDealCode(NetOption netOption, String code){
        if(netOption.getProjectType()==ProjectType.DEFAULT){
            //默认当前项目，判断code是否是需要返回success处理的code
            return isContains(DEFAULT_DEAL_CODES,code);
        }
        //不进行配置code，返回true（全返回到success)
        return true;
    }

    /**
     * 是否是需要全局处理的code （例如token过期）
     * @param code
     * @return
     */
    public static boolean isContainsGlobalDealCode(NetOption netOption,String code) {
        if(netOption.getProjectType()==ProjectType.DEFAULT){
            //默认当前项目，判断code是否是需要返回success处理的code
            return isContains(DEFAULT_DEAL_GLOBAL_CODES,code);
        }
        //不进行配置code，返回false，不进行全局拦截,继续向下判断
        return false;
    }

    private static boolean isContains(String[] strArr,String key){
        for (String s : strArr) {
            if (s.equals(key)){
                return true;
            }
        }
        return false;
    }
}
