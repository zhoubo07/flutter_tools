import 'dart:io';

import 'package:device_info/device_info.dart';
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

class DeviceInfoUtils {
  static AndroidDeviceInfo? androidInfo;
  static IosDeviceInfo? iosInfo;

  static Future init() async {
    final deviceInfoPlugin = DeviceInfoPlugin();
    if (Platform.isAndroid) {
      androidInfo = await deviceInfoPlugin.androidInfo;
    } else if (Platform.isIOS) {
      iosInfo = await deviceInfoPlugin.iosInfo;
    }
  }

  static String getUserAgent() {
    String userAgent = PackageInfoUtils.getAppName() +
        "/" +
        PackageInfoUtils.getVersion() +
        "(${PackageInfoUtils.getBuildNumber()})";
    if (Platform.isAndroid && androidInfo != null) {
      userAgent +=
          " (Android ${androidInfo!.version.release}; ${androidInfo!.brand} ${androidInfo!.model})";
    } else if (Platform.isIOS && iosInfo != null) {
      userAgent +=
          " (${iosInfo!.systemName} ${iosInfo!.systemVersion}; ${iosInfo!.model})";
    }
    return userAgent;
  }
}
