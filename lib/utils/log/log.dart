import 'log_level.dart';
import 'log_message.dart';
import 'log_printer.dart';

/// @Title   自定义日志工具
/// @Author: zhoubo
/// @CreateDate:  5/12/21 6:21 PM
class Log {
  static String? _kTag;

  static LogPrinter? _printer;

  /// 主动设置是否开启日志（这个开关的优先级最高）
  /// （如果没有主动设置，则根据debug环境判断，如果主动设置了，则使用设置的）
  static set enableLog(bool enable) => printer.enableLog = enable;

  /// 设置全局日志默认的tab，不设置默认使用文件名
  static void setDefaultTag(String tag) => _kTag = tag;

  static LogPrinter get printer {
    _printer ??= CommonPrinter();
    return _printer!;
  }

  static set printer(LogPrinter printer) => _printer = printer;

  static void _log(LogMessage message) {
    printer.write(message);
  }

  static void d(Object message, {String? tag, bool isJson = false}) {
    _log(LogMessage(LogLevel.debug, message, _getLoggerTag(tag), isJson));
  }

  static void i(Object message, {String? tag, bool isJson = false}) {
    _log(LogMessage(LogLevel.info, message, _getLoggerTag(tag), isJson));
  }

  static void w(Object message, {String? tag, bool isJson = false}) {
    _log(LogMessage(LogLevel.warn, message, _getLoggerTag(tag), isJson));
  }

  static void e(Object message, {String? tag, bool isJson = false}) {
    _log(LogMessage(LogLevel.error, message, _getLoggerTag(tag), isJson));
  }

  static void f(String message, {String? tag, bool isJson = false}) {
    _log(LogMessage(LogLevel.fatal, message, _getLoggerTag(tag), isJson));
  }

  //如果没有设置tag，则获取当前执行文件的文件名
  static String _getLoggerTag(String? tag) {
    if (tag == null) {
      if (null != _kTag) return _kTag!;
      var traceString = StackTrace.current.toString();
      var curMatch = RegExp(r'[A-Za-z_]+.dart').firstMatch(traceString);
      var allMatch = RegExp(r'[A-Za-z_]+.dart').allMatches(traceString);
      // ignore: unnecessary_null_comparison
      if (curMatch != null && allMatch != null) {
        List? stacks = allMatch
            .where((element) => element.group(0) != curMatch.group(0))
            .toList();
        // ignore: unnecessary_null_comparison
        if (stacks != null) {
          return stacks[0].group(0);
        } else {
          return '';
        }
      }
      return '';
    } else {
      return tag;
    }
  }
}
