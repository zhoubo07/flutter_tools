import 'dart:ui';

import 'package:flutter_tools/utils/text_utils.dart';

/// @Title   颜色转换相关工具类
/// @Author: zhoubo
/// @CreateDate:  2022/5/5 6:03 下午
class ColorUtil {
  /// 颜色检测只保存 #RRGGBB格式 FF透明度
  /// [color] 格式可能是材料风/十六进制/string字符串
  /// 返回[String] #rrggbb 字符串
  static String? color2HEX(Object? color) {
    if (null == color) return null;
    if (color is Color) {
      // 0xFFFFFFFF
      //将十进制转换成为16进制 返回字符串但是没有0x开头
      String temp = color.value.toRadixString(16);
      color = "#${temp.substring(2, 8)}";
    }
    return color.toString();
  }

  ///"#ffffff"转color
  static Color? stringToColor(String? colorStr) {
    if (TextUtils.isEmpty(colorStr)) return null;
    if (colorStr!.contains('#')) {
      List<String> tmp = colorStr.split("#");
      if (tmp.length < 2) return null;
      return Color(int.parse("0xFF${tmp[1]}"));
    }
    return null;
  }
}
