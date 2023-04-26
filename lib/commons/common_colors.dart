import 'package:flutter/material.dart';

/// @Title   业务中颜色类
/// @Author: zhoubo.
/// @CreateDate:  2021/2/19 0019 15:00
class CommonColor {
  CommonColor._();

  /// 主色调#FF6633
  static const Color mainColor = redFF6633;

  /// 主色调#FF6633半透明
  static const Color mainColorAlpha50 = redFF6633Alpha50;

  /// 主题背景色
  static const Color themeBgColor = greyF6;

  /**
   *   /////////////////////////////////////////////////////////////////////////////////
   *   //
   *   //                 上方是功能色值（以业务功能命名），下方是具体色值（以色值命名）
   *   //
   *   /////////////////////////////////////////////////////////////////////////////////
   */

  /// 黑色
  static const Color blackAlpha10 = Color(0x1A000000);
  static const Color blackAlpha95 = Color(0x0A000000);
  static const Color blackAlpha60 = Color(0x99000000);
  static const Color black54 = Color(0xFF545454);
  static const Color black22 = Color(0xFF222222);
  static const Color black26 = Color(0xFF262626);
  static const Color black59 = Color(0xFF595959);

  /// 白色
  static const Color whiteAlpha30 = Color(0xB2FFFFFF);
  static const Color whiteAlpha45 = Color(0x73FFFFFF);

  /// 灰色
  static const Color greyEEE = Color(0xFFEEEEEE);
  static const Color greyE5 = Color(0xFFE5E5E5);
  static const Color greyF6 = Color(0xFFF6F6F6);
  static const Color greyF8 = Color(0xFFF8F8F8);
  static const Color greyF2 = Color(0xFFF2F2F2);
  static const Color grey97 = Color(0xFF979797);
  static const Color grey87 = Color(0xFF878787);
  static const Color greyFA = Color(0xFFFAFAFA);
  static const Color greyCCC = Color(0xFFCCCCCC);
  static const Color greyCE = Color(0xFFCECECE);
  static const Color greyB8 = Color(0xFFB8B8B8);
  static const Color greyDE = Color(0xFFDEDEDE);
  static const Color greyD9 = Color(0xFFD9D9D9);
  static const Color grey8C = Color(0xFF8C8C8C);
  static const Color greyBF = Color(0xFFBFBFBF);
  static const Color greyF0 = Color(0xFFF0F0F0);
  static const Color greyC4CD = Color(0xFFC4CDCF);
  static const Color grey9CA9 = Color(0xFF9CA9AB);
  static const Color grey8c = Color(0xFF8c8c8c);
  static const Color greyFFFBF9 = Color(0xFFFFFBF9);

  /// 红色
  static const Color redF50 = Color(0xFFF50000);
  static const Color redAB8F = Color(0xFFFFAB8F);
  static const Color redF0EA = Color(0xFFFFF0EA);
  static const Color red3140 = Color(0xFFFF3140);
  static const Color redFF794D = Color(0xFFFF794D);
  static const Color redFF7E3E = Color(0xFFFF7E3E);
  static const Color redFF6633 = Color(0xFFFF6633);
  static const Color redFF6633Alpha10 = Color(0x1AFF6633);
  static const Color redFF6633Alpha50 = Color(0x80FF6633);

  /// 黄色
  static const Color yellow9F17 = Color(0xFFFF9F17);
  static const Color yellowFF9966 = Color(0xFFFF9966);
  static const Color yellowF6E8 = Color(0xFFFFF6E8);
  static const Color yellowF5E7 = Color(0xFFFFF5E7);

  /// 绿色
  static const Color green20B35F = Color(0xFF20B35F);

  /// 判断颜色是light还是dark
  static bool isLightColor(Color color) {
    if(color.value==Colors.transparent.value) return true;
    double darkness = 1 -
        (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue) / 255;
    if (darkness < 0.3) {
      return true; // It's a light color
    } else {
      return false; // It's a dark color
    }
  }
}
