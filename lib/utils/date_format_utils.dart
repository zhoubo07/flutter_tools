import 'package:intl/intl.dart';

/// @Title   时间转换的工具类
/// @Author: zhoubo
/// @CreateDate:  2022/4/15 11:39 上午
class DateTimeUtils {

  /// 判断是否是今年
  static bool isCurrentYear(DateTime dateTime) {
    return DateFormat('yyyy').format(DateTime.now()) ==
        DateFormat('yyyy').format(dateTime);
  }

  /// 判断是否是今月
  static bool isCurrentMonth(DateTime dateTime) {
    return DateFormat('yyyy-MM').format(DateTime.now()) ==
        DateFormat('yyyy-MM').format(dateTime);
  }

  /// 判断是否是今天
  static bool isToday(DateTime dateTime) {
    return DateFormat('yyyy-MM-dd').format(DateTime.now()) ==
        DateFormat('yyyy-MM-dd').format(dateTime);
  }

  /// 是否是昨天
  static bool isYesterday(DateTime dateTime) {
    return DateTime.parse(DateFormat('yyyy-MM-dd').format(DateTime.now()))
            .difference(
                DateTime.parse(DateFormat('yyyy-MM-dd').format(dateTime)))
            .inDays ==
        1;
  }

  /// 格式化时间
  static String formatDate(DateTime date, {String format = 'yyyy-MM-dd'}) =>
      DateFormat(format).format(date);

  /// 时间戳（毫秒）转换时间
  static String formatDateFromMillisecondsSinceEpoch(
      int? millisecondsSinceEpoch,
      {String format = 'yyyy-MM-dd'}) {
    if (null == millisecondsSinceEpoch) return '';
    return formatDate(
        DateTime.fromMillisecondsSinceEpoch(millisecondsSinceEpoch),
        format: format);
  }

  /// 时间戳（秒）转换时间
  static String formatDateFromSecondsSinceEpoch(int? secondsSinceEpoch,
      {String format = 'yyyy-MM-dd'}) {
    if (null == secondsSinceEpoch) return '';
    return formatDate(
        DateTime.fromMillisecondsSinceEpoch(secondsSinceEpoch * 1000),
        format: format);
  }

  /// 格式化时间（格式为：Jul 22,2022）
  static String formatDate1FromSecondsSinceEpoch(int secondsSinceEpoch) {
    DateTime dateTime =
        DateTime.fromMillisecondsSinceEpoch(secondsSinceEpoch * 1000);
    String monthStr = getMouthStr(dateTime, abbr: true);
    String dayStr = '${dateTime.day}'.padLeft(2, '0');
    String yearStr = '${dateTime.year}';
    return '$monthStr $dayStr, $yearStr';
  }

  /// get Mouth.
  static String getMouthStr(DateTime dateTime, {bool abbr = true}) {
    String mouth = '';
    switch (dateTime.month) {
      case 1:
        mouth = abbr ? "Jan" : "January";
        break;
      case 2:
        mouth = abbr ? "Feb" : "February";
        break;
      case 3:
        mouth = abbr ? "Mar" : "March";
        break;
      case 4:
        mouth = abbr ? "Apr" : "April";
        break;
      case 5:
        mouth = abbr ? "May" : "May";
        break;
      case 6:
        mouth = abbr ? "Jun" : "June";
        break;
      case 7:
        mouth = abbr ? "Jul" : "July";
        break;
      case 8:
        mouth = abbr ? "Aug" : "August";
        break;
      case 9:
        mouth = abbr ? "Sept" : "September";
        break;
      case 10:
        mouth = abbr ? "Oct" : "October";
        break;
      case 11:
        mouth = abbr ? "Nov" : "November";
        break;
      case 12:
        mouth = abbr ? "Dec" : "December";
        break;
      default:
        break;
    }
    return mouth;
  }
}
