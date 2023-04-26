import 'dart:convert';

import 'package:flutter_tools/beans/upload_bean.dart';

import '../commons/net/net_api.dart';
import '../utils/log/log.dart';
import 'api_bean.dart';
import 'main_config_bean.dart';

/// 用户处理ApiBean的泛型转换的类
class ApiBeanFactory {
  static M fromJsonAsT<M>(json) {
    String type = M.toString();
    if (json is List && type.contains("List<")) {
      String itemType = type.substring(5, type.length - 1);
      List tempList = _getListFromType(itemType);
      json.forEach((itemJson) {
        tempList.add(_fromJsonSingle(itemType, itemJson));
      });
      return tempList as M;
    } else {
      return _fromJsonSingle(M.toString(), json) as M;
    }
  }

  /// 当ApiBean<List<>>泛型格式为List时候，在此注册
  static _getListFromType(String type) {
    switch (type) {
      case 'MainConfigBean': // 主配置
        return <MainConfigBean>[];
    }
    _printErrorListBean();
    return null;
  }

  /// 当ApiBean<>泛型格式为普通实体类时候，在此注册
  static _fromJsonSingle(String type, json) {
    switch (type) {
      case 'String':
        if (json is String) return json;
        if (json is int || json is num) return json.toString();
        return jsonEncode(json);
      case 'int':
      case 'num':
      case 'Map<String, dynamic>':
      case 'Map<dynamic, dynamic>':
      case 'Map':
      case 'dynamic':
        return json;
      case 'ApiBean':
        return ApiBean.fromJson(json);
      case 'MainConfigBean': // 主配置
        return MainConfigBean.fromJson(json);
      case 'UploadFileBean': // 单文件上传返回
        return UploadFileBean.fromJson(json);
    }
    _printErrorBean();
    return null;
  }

  static _printErrorBean() {
    Log.e('解析失败：实体未在ApiBeanFactory中注册', tag: '${netLogTag}e');
  }

  static _printErrorListBean() {
    Log.e('解析失败：List未在ApiBeanFactory中注册', tag: '${netLogTag}e');
  }
}
