import 'dart:io';

import 'package:flutter/widgets.dart';
import 'package:flutter_tools/utils/loading_utils.dart';
import 'package:flutter_tools/utils/package_info_util.dart';
import 'package:permission_handler/permission_handler.dart';

import 'device_info_utils.dart';

/// @Title   App权限相关工具类
/// @Author: zhoubo
/// @CreateDate:  5/31/21 5:23 PM
class PermissionUtil {
  /// 检查、请求相机权限
  /// 返回true：有权限或者同意权限；返回false:拒绝权限；
  static Future<bool> requestCameraPermission(
          {BuildContext? context,
          String? titleWhenDenied,
          String? messageWhenDenied,
          String? okLabelWhenDenied}) =>
      requestSinglePermission(Permission.camera,
          context: context,
          titleWhenDenied: titleWhenDenied,
          messageWhenDenied: messageWhenDenied,
          okLabelWhenDenied: okLabelWhenDenied);

  /// 检查、请求相册权限
  /// 返回true：有权限或者同意权限；返回false:拒绝权限；
  static Future<bool> requestPhotosPermission(
      {BuildContext? context,
      String? titleWhenDenied,
      String? messageWhenDenied,
      String? okLabelWhenDenied}) {
    Permission permission = Permission.storage;
    if (Platform.isIOS) {
      permission = Permission.photos;
    } else if (Platform.isAndroid) {
      if ((DeviceInfoUtils.instance.androidDeviceInfo?.version.sdkInt ?? 30) <=
          32) {
        permission = Permission.storage;
      } else {
        permission = Permission.photos;
      }
    }
    titleWhenDenied ??= '需要权限';
    okLabelWhenDenied ??= '去开启';
    return requestSinglePermission(permission,
        context: context,
        titleWhenDenied: titleWhenDenied,
        messageWhenDenied: messageWhenDenied,
        okLabelWhenDenied: okLabelWhenDenied);
  }

  /// 检查、请求单个权限
  /// 返回true：有权限或者同意权限；返回false:拒绝权限；
  static Future<bool> requestSinglePermission(Permission permission,
      {BuildContext? context,
      String? titleWhenDenied,
      String? messageWhenDenied,
      String? okLabelWhenDenied}) async {
    PermissionStatus status = await permission.status;
    // 有此权限
    if (status == PermissionStatus.granted ||
        status == PermissionStatus.limited) return true;
    // 权限被永久拒绝
    if (status.isPermanentlyDenied) {
      // 提示用户打开权限
      _defaultOpenAppSetting(
          context: context,
          titleWhenDenied: titleWhenDenied,
          messageWhenDenied: messageWhenDenied,
          okLabelWhenDenied: okLabelWhenDenied);
      return false;
    }
    // 没有此权限也没有被永久拒绝，去请求权限
    PermissionStatus requestStatus = await permission.request();
    // 请求权限成功
    if (requestStatus == PermissionStatus.granted) return true;
    // 用户拒绝了权限
    // LinkrLoading.showToast(msg: '您拒绝了开启权限');
    return false;
  }

  /// 检查、请求多个权限
  /// 返回true：有权限或者同意权限；返回false:拒绝权限；
  static Future<bool> requestMultiplePermission(
      List<Permission> multiplePermission,
      {BuildContext? context,
      String? titleWhenDenied,
      String? messageWhenDenied,
      String? okLabelWhenDenied}) async {
    // 是否拥有全部权限
    bool isAllGranted = true;
    for (Permission permission in multiplePermission) {
      PermissionStatus status = await permission.status;
      if (status.isPermanentlyDenied) {
        // 永久拒绝
        _defaultOpenAppSetting(
            context: context,
            titleWhenDenied: titleWhenDenied,
            messageWhenDenied: messageWhenDenied,
            okLabelWhenDenied: okLabelWhenDenied);
        return false;
      } else if (!status.isGranted && !status.isLimited) {
        isAllGranted = false;
      }
    }
    if (isAllGranted) {
      return true;
    } else {
      Map<Permission, PermissionStatus> statuses =
          await multiplePermission.request();
      return statuses.values.every((element) {
        if (!element.isGranted && !element.isLimited) {
          // LinkrLoading.showToast(msg: '您拒绝了开启权限');
        }
        return element.isGranted;
      });
    }
  }

  // static Future<bool> _checkPermission(Permission permission) async {
  //   PermissionStatus status = await permission.status;
  //   switch (status) {
  //     case PermissionStatus.granted:
  //       // 允许
  //       return true;
  //     case PermissionStatus.denied:
  //       // 拒绝
  //       return false;
  //     case PermissionStatus.restricted:
  //       // 限制
  //       return false;
  //     case PermissionStatus.limited:
  //       return true;
  //     case PermissionStatus.permanentlyDenied:
  //       // 永久拒绝
  //       return false;
  //   }
  //   return false;
  // }

  static void _defaultOpenAppSetting(
      {BuildContext? context,
      String? titleWhenDenied,
      String? messageWhenDenied,
      String? okLabelWhenDenied}) async {
    // 提示用户打开权限
    if (null == context) {
      LoadingUtils.showToast(titleWhenDenied ?? 'Permission Denied',
          context: context);
    } else {
      // todo 缺少封装的DialogUtils打开弹框
      // final result = await showOkAlertDialog(
      //     context: context,
      //     title: titleWhenDenied,
      //     message: messageWhenDenied,
      //     okLabel: okLabelWhenDenied);
      // if (result == OkCancelResult.ok) {
      //   openAppSettings();
      // }
    }
  }
}
