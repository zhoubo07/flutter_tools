import 'dart:io';

import 'package:getuiflut/getuiflut.dart';

/// @Title   推送的配置
/// @Author: zhoubo
/// @CreateDate:  12/20/21 3:23 PM
class PushConfig {
  /// 初始化个推
  static void initGetui() {
    if (Platform.isIOS) {
      // iOS 的个推初始化
      Getuiflut().startSdk(
          appId: "zgOFC59s4jA9XqQmvxVdE6",
          appKey: "ifJDr1NG4S8bnXDk4qKYI3",
          appSecret: "LnmFWmXnUKA7AMVpyfQ6x9");
    } else {
      // Android的个推初始化（因为已经在Android工程配置了key）
      Getuiflut.initGetuiSdk;
    }

    Getuiflut().addEventHandler(
      // 注册收到 cid 的回调
      onReceiveClientId: (String message) async {},
      // 注册 DeviceToken 回调
      onRegisterDeviceToken: (String message) async {},
      // SDK收到透传消息回调
      onReceivePayload: (Map<String, dynamic> message) async {},
      // 点击通知回调
      onReceiveNotificationResponse: (Map<String, dynamic> message) async {},
      // APPLink中携带的透传payload信息
      onAppLinkPayload: (String message) async {},
      //通知服务开启\关闭回调
      onPushModeResult: (Map<String, dynamic> message) async {},
      // SetTag回调
      onSetTagResult: (Map<String, dynamic> message) async {},
      //设置别名回调
      onAliasResult: (Map<String, dynamic> message) async {},
      //查询别名回调
      onQueryTagResult: (Map<String, dynamic> message) async {},
      //APNs通知即将展示回调
      onWillPresentNotification: (Map<String, dynamic> message) async {},
      //APNs通知设置跳转回调
      onOpenSettingsForNotification: (Map<String, dynamic> message) async {},
      onNotificationMessageClicked: (Map<String, dynamic> event) async {
        // 。。点击通知消息 (未登录的不跳转，已登录的跳转到消息tab)
      },
      onReceiveMessageData: (Map<String, dynamic> event) async {
        // 携带附加的json消息（如果点击通知的后续动作是附加消息）
      },
      onNotificationMessageArrived: (Map<String, dynamic> event) async {
        // 。。收到通知消息
      },
      onTransmitUserMessageReceive: (Map<String, dynamic> event) async {},
      onGrantAuthorization: (String res) async {
        // // ios通知授权结果
      },
    );
  }
}
