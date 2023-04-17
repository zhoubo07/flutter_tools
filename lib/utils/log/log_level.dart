/// @Title   日志级别
/// @Author: zhoubo
/// @CreateDate:  5/12/21 6:26 PM
class LogLevel {
  //
  static LogLevel debug = const LogLevel('[D]', ''); // 默认色
  static LogLevel info = const LogLevel('[I]', ''); // 默认色
  static LogLevel warn = const LogLevel('[W]', '33m'); // 黄色
  static LogLevel error = const LogLevel('[E]', '31m'); // 红色
  static LogLevel fatal = const LogLevel('[F]', '31m'); // 红色

  final String name;

  // ansi 转义颜色 使用 '\x1B[+前景色代码(8中初始颜色)+文字'，或者使用 '\x1B[38:5:+前景色代码(256种进阶颜色)+文字'
  // 参考：https://blog.csdn.net/scilogyhunter/article/details/106874395
  // 转义颜色的前缀， /// 目前暂时没有使用颜色（有坑）
  final String ansiColorPre;

  const LogLevel(this.name, String ansiColorPre)
      : ansiColorPre = '\x1B[$ansiColorPre';
}
