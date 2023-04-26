
import 'package:flutter/widgets.dart';
import 'package:flutter_tools/commons/push_config.dart';
import 'package:flutter_tools/commons/share_utils.dart';

import '../utils/device_info_utils.dart';
import '../utils/log/log.dart';
import '../utils/package_info_util.dart';
import '../utils/sp_util.dart';

/// @Title: 全局的初始化工作
/// @Author: zhoubo
/// @CreateDate: 2020年12月24日11:24:54

/// 全局的globalKey，相当于全局context
final GlobalKey<NavigatorState> navigatorKey = GlobalKey();

class GlobalConfig {
  /// 当前开发状态：是否是正式模式/debug模式
  static const bool inProduction =
      const bool.fromEnvironment("dart.vm.product", defaultValue: true);

  /// 初始化项目
  static Future init() async {
    // // 这个日志开关暂时先不控制，正式上线需要修改
    // Log.enableLog = true;
    Log.enableLog = !inProduction;
    // 初始化设备信息
    DeviceInfoUtils.instance.initDevice();
    // 初始化包信息
    await PackageInfoUtils.init();
    // 初始化本地SP存储
    await SpUtils.getInstance();
    // 初始化推送
    PushConfig.initGetui();
    // 初始化微信分享
    ShareUtils.initFluwx();
  }
}
