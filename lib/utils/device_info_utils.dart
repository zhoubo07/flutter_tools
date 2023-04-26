import 'dart:io';

import 'package:device_info/device_info.dart';

/// @Title   设备信息工具类
/// @Author: zhoubo
/// @CreateDate:  12/28/21 2:56 PM
class DeviceInfoUtils {
  // 单例
  static DeviceInfoUtils get instance => _getInstance();

  factory DeviceInfoUtils() => _getInstance();

  static DeviceInfoUtils? _instance;

  static DeviceInfoUtils _getInstance() =>
      _instance ??= DeviceInfoUtils._internal();

  DeviceInfoUtils._internal();

  // --------------------------------------------------------------------------
  // Android的设备信息类
  AndroidDeviceInfo? androidDeviceInfo;

  // iOS的设备信息类
  IosDeviceInfo? iosDeviceInfo;

  /// 初始化获取设备信息（只在启动时初始化一次就行）
  void initDevice() async {
    if (null != androidDeviceInfo || null != iosDeviceInfo)
      return;
    DeviceInfoPlugin deviceInfo = DeviceInfoPlugin();
    if (Platform.isAndroid)
      androidDeviceInfo = await deviceInfo.androidInfo;
    else if (Platform.isIOS) iosDeviceInfo = await deviceInfo.iosInfo;
  }

  /// 获取设备id，Android获取AndroidId，iOS获取UUID
  String get deviceId =>
      (androidDeviceInfo?.androidId) ??
      (iosDeviceInfo?.identifierForVendor) ??
      '';

  /// 其他需要用到的设备信息，用到的时候再添加

}
