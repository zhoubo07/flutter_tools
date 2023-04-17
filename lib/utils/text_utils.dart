import 'package:flutter/widgets.dart';

/// @Title: 文字判断的工具类
/// @Author: zhoubo
/// @CreateDate: 2020年12月24日11:24:54
class TextUtils {
  static bool isEmpty(String? content) {
    return content?.isEmpty ?? true;
  }

  static bool isNotEmpty(String? content) {
    return content?.isNotEmpty ?? false;
  }

  /// 截取字符串，如果截掉了末尾加省略号
  static String subStringWithEllipsis(String? content,
      {int? maxLength, String ellipsis = '...'}) {
    if (isEmpty(content)) return '';
    if (null == maxLength) return content!;
    if (content!.length < maxLength) return content;
    String subContent = content.substring(0, maxLength);
    return '$subContent$ellipsis';
  }

  /// 计算文字绘制的宽高
  /// value: 文本内容；
  static Size calculateTextSize(
    BuildContext context,
    String value,
    fontSize, {
    FontWeight? fontWeight, // 文字权重 (加粗)；
    double maxWidth = double.infinity, // 文本框的最大宽度；
    int maxLines = 1, // 文本支持最大多少行;
  }) {
    //过滤文本
    TextPainter painter = TextPainter(
        // 华为手机如果不指定locale的时候，该方法算出来的文字高度是比系统计算偏小的。
        locale: Localizations.localeOf(context),
        maxLines: maxLines,
        textDirection: TextDirection.ltr,
        text: TextSpan(
            text: value,
            style: TextStyle(fontWeight: fontWeight, fontSize: fontSize)));
    // 设置layout
    painter.layout(maxWidth: maxWidth);
    //文字的Size
    return Size(painter.width, painter.height);
  }

  /// 获取文本中的url
  static List<String> findUrlsInText(String text) {
    RegExp urlPattern = RegExp(
        r"(?:(?:https?|ftp)://|www\.)[-a-zA-Z\d@:%._+~#=]{1,256}\.[a-zA-Z\d()]{1,10}(?:[-a-zA-Z0-9()@:%_+.~#?&/=]*)?");
    final matches = urlPattern.allMatches(text);
    final urls = <String>[];
    for (var match in matches) {
      urls.add(match.group(0)!);
    }
    return urls;
  }
}
