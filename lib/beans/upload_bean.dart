/// @Title   上传的返回实体类
/// @Author: zhoubo
/// @CreateDate:  1/25/21 11:24 AM
class UploadFileBean {
  final String? fileName;  // 文件名称
  final String? fileUrl;   // 文件地址(全路径)
  final String? originalFileName;  // 原始文件名称

  UploadFileBean({this.fileName, this.fileUrl, this.originalFileName});

  factory UploadFileBean.fromJson(Map<String, dynamic> json) {
    return UploadFileBean(
        fileName: json['fileName'],
        fileUrl: json['fileUrl'],
        originalFileName: json['originalFileName']);
  }
}
