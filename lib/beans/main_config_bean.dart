/// @Title: 主配置模型
/// @Author: zhoubo
/// @CreateDate: 2020年12月24日14:17:48
class MainConfigBean {
  final List<UrlConfigBean>? urlConfig;

  MainConfigBean({this.urlConfig});

  factory MainConfigBean.fromJson(Map<String, dynamic> json) {
    var urlConfigJson = json['urlConfig'] as List;
    List<UrlConfigBean> urlConfigList =
        urlConfigJson.map((e) => UrlConfigBean.fromJson(e)).toList() ?? [];
    return MainConfigBean(urlConfig: urlConfigList);
  }
}

class UrlConfigBean {
  final String? code;
  final String? domain;
  final String? link;

  UrlConfigBean({this.code, this.domain, this.link});

  factory UrlConfigBean.fromJson(Map<String, dynamic> json) {
    return UrlConfigBean(
        code: json['code'], domain: json['domain'], link: json['link']);
  }
}
