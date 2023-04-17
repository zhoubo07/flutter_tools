import 'package:flutter/foundation.dart';
import 'log.dart';
import 'log_message.dart';

/// @Title   日志的绘制者
/// @Author: zhoubo
/// @CreateDate:  5/12/21 6:29 PM
abstract class LogPrinter {
  /// 是否开启输出日志
  bool? _enableLog; // 主动控制是否开启日志

  /// 主动设置是否开启日志（这个开关的优先级最高）
  set enableLog(bool enable) {
    _enableLog = enable;
  }

  LogPrinter();

  Future<void> write(LogMessage message);

  /// 判断日志开关
  /// （如果没有主动设置，则根据debug环境判断，如果主动设置了，则使用设置的）
  bool shouldShowLog(LogMessage message) {
    // 没有设置日志开关
    if (null == _enableLog) {
      // 如果是生产环境、则不显示日志；debug环境显示日志
      return kDebugMode;
    }
    return _enableLog!;
  }
}

/// @Title  自定义绘制日志（添加分割线）
/// @Author: zhoubo
/// @CreateDate:  5/12/21 6:28 PM
class CommonPrinter extends LogPrinter {
  static const topLeftCorner = '┌';
  static const bottomLeftCorner = '└';
  static const verticalLine = '│';
  static const divider = '─';

  int printMaxLength = 88800; //print的打印有字数限制，过长则不显示超出部分，因此再次限制输出长度
  int lineLength = 80;
  String _topBorder = '';
  String _bottomBorder = '';

  CommonPrinter() : super() {
    //初始化分割线
    var dividerLine = StringBuffer();
    for (var i = 0; i < lineLength - 1; i++) {
      dividerLine.write(divider);
    }
    _topBorder = '$topLeftCorner$dividerLine';
    _bottomBorder = '$bottomLeftCorner$dividerLine';
  }

  // 预先整合日志信息至StringBuffer，当StringBuffer存储的长度超过规定的输出值，
  // 则输出当前缓存日志。输出清空后继续遍历直至输出所有
  @override
  Future<void> write(LogMessage message) async {
    try {
      writeByDebugPrint(message);
    } catch (e) {
      Log.e(e.toString(), tag: '----print err');
    }
  }

  // Future<void> writeByPrint(LogMessage message) async {
  //   if (shouldShowLog(message)) {
  //     var info = StringBuffer();
  //     var startInfo = "${message.level.name} ${message.tag}";
  //     info.write("$startInfo $_topBorder\n");
  //     var lines = message.toString().split("\n");
  //     for (var line in lines) {
  //       var midStr = "$startInfo $verticalLine $line\n";
  //       if (info.length + midStr.length >= printMaxLength) {
  //         _toPrint(midStr, message.level.ansiColorPre);
  //       } else {
  //         _toPrint(info, message.level.ansiColorPre);
  //         info.clear();
  //         info.write(midStr);
  //       }
  //     }
  //     _toPrint(info, message.level.ansiColorPre);
  //     _toPrint("$startInfo $_bottomBorder", message.level.ansiColorPre);
  //   }
  // }

  Future<void> writeByDebugPrint(LogMessage message) async {
    if (shouldShowLog(message)) {
      var startInfo = "${message.level.name} ${message.tag}";
      _toPrint("$startInfo $_topBorder", message.level.ansiColorPre);
      var lines = message.toString().split("\n");
      for (var line in lines) {
        var midStr = "$startInfo $verticalLine $line";
        _toPrint(midStr, message.level.ansiColorPre);
      }
      _toPrint("$startInfo $_bottomBorder", message.level.ansiColorPre);
    }
  }

  _toPrint(Object? object, String ansiColorPre) {
    if (object == null) return;
    // 暂时不使用颜色，颜色的会在Logcat中增加额外色值代码
    // print('$ansiColorPre${object.toString()}\x1B[0m');
    if (kDebugMode) {
      // print(object.toString());
      debugPrint(object.toString());
    }
  }
}
