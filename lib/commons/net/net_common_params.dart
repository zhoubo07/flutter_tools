import 'dart:io';

import '../../utils/device_info_utils.dart';
import '../../utils/package_info_util.dart';

/// @Title  公共参数
/// @Author: zhoubo
/// @CreateDate:  4/20/21 6:29 PM

/// 公共请求参数
Map<String, dynamic> commonParams() {
  Map<String, dynamic> commonParams = {
    'clientHeaderName': Platform.operatingSystem,
    'clientHeaderVersion': PackageInfoUtils.getVersion(),
    'deviceId': DeviceInfoUtils.instance.deviceId,
    // 'Authorization': 'Basic c2Fhcy1yZXRhaWw6YWJj',
    // 更换 Authorization
    'Authorization': Platform.isIOS
        ? 'Basic c2Fhcy15bWwtYWlkZS1pb3M6YWJj'
        : 'Basic c2Fhcy15bWwtYWlkZS1hbmRyb2lkOmFiYw',
  };
  return commonParams;
}
