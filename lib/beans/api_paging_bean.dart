import 'api_bean_factory.dart';

/// @Title   分页的实体类
/// @Author: zhoubo
/// @CreateDate:  5/24/21 3:31 PM
class ApiPagingBean<T> {
  final int? code;
  final String? msg;
  final PagingBean<T>? data;

  ApiPagingBean({this.code, this.msg, this.data});

  factory ApiPagingBean.fromJson(Map<String, dynamic> json) {
    return ApiPagingBean(
        code: json['code'],
        msg: json['msg'],
        data:
            json['data'] == null ? null : PagingBean<T>.fromJson(json['data']));
  }
}

class PagingBean<T> {
  final int? current;
  final int? pages;
  final int? size;
  final int? total;
  final List<T>? records;

  PagingBean({this.current, this.pages, this.size, this.total, this.records});

  bool get isLastPage => (current ?? 0) >= (pages ?? 0);

  factory PagingBean.fromJson(Map<String, dynamic> json) {
    var records = json['records'];
    List? recordsJson = null == records ? null : records as List;
    List<T>? recordsData =
        recordsJson?.map((e) => ApiBeanFactory.fromJsonAsT<T>(e)).toList() ??
            null;
    return PagingBean(
        current: json['current'],
        pages: json['pages'],
        size: json['size'],
        total: json['total'],
        records: recordsData);
  }
}
