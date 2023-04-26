import '../../utils/sp_util.dart';

/// @title      本地Sp存储的Url
/// @author:    zhoubo
/// @CreateDate:  2020年12月30日09:43:16
class SpUrls {
  // sp存储前缀
  static const String _prefix = 'url.';

  static String get customPrefix => _prefix;

  static String _getSpUrl(String key) => SpUtils.get(_prefix + key);

  static void saveUrl<T>(String key, T value) =>
      SpUtils.setData(_prefix + key, value);

  /// 查询新版本信息
  static String get version => _getSpUrl("getApiPcAppVersion");

  /// 单文件上传
  static String get uploadSingleFile => _getSpUrl("uploadSingleFile");

}
