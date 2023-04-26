import 'api_bean_factory.dart';

/// 解析实体类中的code
int? parseApiCode(Map<String, dynamic> json) => json['code'];

/// 解析实体类中的Msg
String? parseApiMsg(Map<String, dynamic> json) => json['msg'];

/// @Title: 最外层实体类
/// @Author: zhoubo
/// @CreateDate: 2020年12月24日15:00:49
class ApiBean<T> {
  final int? code;
  final String? msg;
  final T? data;

  ApiBean({this.code, this.msg, this.data});

  factory ApiBean.fromJson(Map<String, dynamic> json) {
    return ApiBean(
        code: parseApiCode(json),
        msg: parseApiMsg(json),
        data: json['data'] == null
            ? null
            : ApiBeanFactory.fromJsonAsT<T>(json['data']));
  }
}
