import 'dart:typed_data';

import 'package:fluwx_no_pay/fluwx_no_pay.dart';


/// @Title   分享的工具类
/// @Author: zhoubo
/// @CreateDate:  2/4/21 10:21 AM
class ShareUtils {
  /// 初始化微信分享
  static initFluwx() async {
    await registerWxApi(
      appId: "wxeb8fd0df8182b580",
      doOnAndroid: true,
      doOnIOS: true,
      // ios需要填写这个
      // universalLink: "https://rc288.com/"
    );
    var result = await isWeChatInstalled;
  }

  static shareImageToWx(Uint8List source, {Uint8List? thumbnail}) {
    shareToWeChat(WeChatShareImageModel(WeChatImage.binary(source),
        thumbnail: null == thumbnail ? null : WeChatImage.binary(thumbnail)));
  }
}
