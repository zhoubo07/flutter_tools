package com.dengyun.baselibrary.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * *   pi=12.34567;
 *         //取整数
 *         System.out.println(new DecimalFormat("0").format(pi));//12
 *
 *         //取整数和两位小数
 *         System.out.println(new DecimalFormat("0.00").format(pi));//12.35
 *
 *         //取两位整数(大于等于两位整数)和三位小数，整数不足部分以0填补。
 *         System.out.println(new DecimalFormat("00.000").format(pi));// 12.346
 *
 *         //取所有整数部分
 *         System.out.println(new DecimalFormat("#").format(pi));//12
 *
 *         //四舍五入参考RoundingMode
 *         new DecimalFormat(format).setRoundingMode();
 *
 *         //以百分比方式计数，并取两位小数
 *         System.out.println(new DecimalFormat("#.##%").format(pi));//1234.57%
 *
 *         //以千分比方式计数，并取两位小数
 *         System.out.println(new DecimalFormat("0.00\u2030").format(pi));//123.40‰
 *     *
 *
 * 格式化小数的工具类
 * Created by seven on 2016/8/1.
 */

public class DecimalFormatUtil {

    /**
     * @param format 格式化方式，格式化规则参考上方的类注释
     * @param num 待格式化数字
     * */
    public static String getFormatDecimal(String format,double num){
        return new DecimalFormat(format).format(num);
    }

    /**
     * 保留两位小数(固定两位小数，小数不够则补0)
     */
    public static String getFormatByTwoDecimal(double num){
        return getFormatDecimal("0.00",num);
    }

    /**
     * @return 小数省略0格式化（最大两位小数，如果小数位是0则省略）(直接舍去，没有四舍五入)
     */
    public static String getFormatByEllipsis0Decimal2(double num){
        DecimalFormat myformat = new DecimalFormat();
        myformat.setMaximumFractionDigits(2);				//允许的最大小数位数，末尾的0不会显示
        //myformat.setGroupingSize(0);						//整数部分分隔符之间的数字个数，如“123,456.78”的 groupingSize 为 3
        myformat.setRoundingMode(RoundingMode.DOWN);		//舍位模式,即直接舍掉小数点后3位至末尾的数字
        return myformat.format(num);

        /*String toDecimalStr = getFormatByTwoDecimal(num);
        if (toDecimalStr.endsWith(".00")){
            return toDecimalStr.substring(0,toDecimalStr.length()-3);
        }else if (toDecimalStr.endsWith("0")){
            return toDecimalStr.substring(0,toDecimalStr.length()-1);
        }else {
            return toDecimalStr;
        }*/
    }

}
