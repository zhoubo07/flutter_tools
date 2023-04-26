import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

import 'list_utils.dart';

/// @Title: 文字判断的工具类
/// @Author: zhoubo
/// @CreateDate: 2020年12月24日11:24:54
class TextUtils {
  /// Regex to find all links in the text.
  static const regexLink =
      r'((http|ftp|https):\/\/)?([\w_-]+(?:(?:\.[\w_-]+)+))([\w.,@?^=%&:/~+#-]*[\w@?^=%&/~+#-])?';

  static const regexEmail =
      r'([a-zA-Z0-9+._-]+@[a-zA-Z0-9._-]+\.[a-zA-Z0-9_-]+)';

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

  /// 在url之前追加http前缀
  static String appendHttpAheadUrl(String? url){
    if(isEmpty(url)) return '';
    if (!url!.toLowerCase().startsWith('http')) {
      url = 'https://$url';
    }
    return url;
  }

  /// 从text文案中获取所有的url
  static List<String> getUrlsFromText(String? content) =>
      getRegexMatchedListFromText(
          content, RegExp(regexLink, caseSensitive: false));

  /// 从text文案中获取所有的Email
  static List<String> getEmailsFromText(String? content) =>
      getRegexMatchedListFromText(
          content, RegExp(regexEmail, caseSensitive: false));

  /// 从text文案中获取所有的能匹配到正则的列表
  static List<String> getRegexMatchedListFromText(
      String? content, RegExp regex) {
    if (isEmpty(content)) return [];
    Iterable<RegExpMatch> matches = regex.allMatches(content!);
    List<String> retList = [];
    for (var match in matches) {
      retList.add((content).substring(match.start, match.end));
    }
    return retList;
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

  /// 翻译文案转为富文本（例如待翻译的文案，中间某部分加粗）
  /// richName：翻译中需要加粗的文本
  /// intlRichTextFun：带参数的翻译方法（加粗的部分需要使用返回的分隔符separator代替）
  ///   例如：(richName1, richName2)=>loc()!.remove_members_desc(richName1, richName2);
  static RichText getIntlRichText(
      String richName, String Function(String separator) intlRichTextFun,
      {TextStyle? textStyle, TextStyle? richTextStyle}) {
    return getIntlRichListText(
      [richName],
      intlRichTextFun,
      textStyle: textStyle,
      richTextStyle: richTextStyle,
    );
  }

  /// 带两个参数的翻译文案转为富文本（例如待翻译的文案，中间某部分加粗）
  /// richName：翻译中需要加粗的文本
  /// intlRichTextFun：带参数的翻译方法（加粗的部分需要使用返回的分隔符separator代替）
  ///   例如：(richName1, richName2)=>loc()!.remove_members_desc(richName1, richName2);
  static RichText getIntlRichText2(String richName1, String richName2,
      String Function(String separator) intlRichTextFun,
      {TextStyle? textStyle, TextStyle? richTextStyle}) {
    return getIntlRichListText(
      [richName1, richName2],
      intlRichTextFun,
      textStyle: textStyle,
      richTextStyle: richTextStyle,
    );
  }

  /// 带三个参数的翻译文案转为富文本（例如待翻译的文案，中间某部分加粗）
  /// richName：翻译中需要加粗的文本
  /// intlRichTextFun：带参数的翻译方法（加粗的部分需要使用返回的分隔符separator代替）
  ///   例如：(richName1, richName2)=>loc()!.remove_members_desc(richName1, richName2);
  static RichText getIntlRichText3(String richName1, String richName2,
      String richName3, String Function(String separator) intlRichTextFun,
      {TextStyle? textStyle, TextStyle? richTextStyle}) {
    return getIntlRichListText(
      [richName1, richName2, richName3],
      intlRichTextFun,
      textStyle: textStyle,
      richTextStyle: richTextStyle,
    );
  }

  /// 带多个加粗文案列表的字符串转为富文本（待翻译的文案，中间某部分加粗）
  ///  -- 带参数的翻译转换为富文本
  /// intlRichTextFun：带参数的翻译方法（加粗的部分需要使用返回的分隔符separator代替）
  /// richName：翻译中需要加粗的文本
  static RichText getIntlRichListText(List<Object> richNameList,
      String Function(String separator) intlRichTextFun,
      {TextStyle? textStyle, TextStyle? richTextStyle}) {
    const String tempName = '2877e22b4aea02dc';
    // 获取所有的文本字符串（例如调用翻译传参的方法）
    String text = intlRichTextFun.call(tempName);
    // 字符分割后的字符串列表
    List<String> textSplit = text.split(tempName);
    if (ListUtils.isEmpty(textSplit) || textSplit.length < 2) {
      return RichText(
        textAlign: TextAlign.center,
        text: TextSpan(
            style: textStyle ??
                TextStyle(
                    color: Colors.black87,
                    fontSize: 16,
                    fontWeight: FontWeight.w400),
            children: [TextSpan(text: text)]),
      );
    } else {
      List<TextSpan> textChildren = [];
      for (int i = 0; i < textSplit.length; i++) {
        textChildren.add(TextSpan(text: textSplit[i]));
        if (richNameList.length > i) {
          textChildren.add(TextSpan(
              text: richNameList[i].toString(),
              style: richTextStyle ??
                  TextStyle(
                      color:  Colors.black87,
                      fontWeight: FontWeight.w600)));
        }
      }
      return RichText(
        textAlign: TextAlign.center,
        text: TextSpan(
            style: textStyle ??
                TextStyle(
                    color:  Colors.grey,
                    fontSize: 16,
                    fontWeight: FontWeight.w400),
            children: textChildren),
      );
    }
  }
}
