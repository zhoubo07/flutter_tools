import 'package:package_info/package_info.dart';

/// @Title: 获取包信息的工具类
/// @Author: zhoubo
/// @CreateDate: 2020年12月24日11:24:54
class PackageInfoUtils {
  static PackageInfo? _packageInfo;

  static Future init() async {
    _packageInfo = await PackageInfo.fromPlatform();
  }

  static String getVersion() {
    return _packageInfo?.version ?? '';
  }

  static String getAppName() {
    return _packageInfo?.appName ?? '';
  }

  static String getPackageName() {
    return _packageInfo?.packageName ?? '';
  }

  static String getBuildNumber() {
    return _packageInfo?.buildNumber ?? '';
  }
}