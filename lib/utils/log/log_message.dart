import 'log_level.dart';
import 'log_convert_util.dart';

/// @Title   日志的信息
/// @Author: zhoubo
/// @CreateDate:  5/12/21 6:27 PM
class LogMessage {
  final LogLevel level; //日志等级
  final Object? message; //日志信息
  final String tag; //日志标识
  final bool isJson;

  const LogMessage(this.level, this.message, this.tag, this.isJson);

  @override
  String toString() {
    if (isJson) {
      return jsonFormat(message);
    } else {
      return message.toString();
    }
  }
}
