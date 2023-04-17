/// @Title   列表相关的工具方法
/// @Author: zhoubo
/// @CreateDate:  2020/12/29 11:15 AM
class ListUtils {
  /// 列表是否为空
  static bool isEmpty(List? list) {
    return list?.isEmpty ?? true;
  }

  /// 列表是否非空
  static bool isNotEmpty(List? list) {
    return list?.isNotEmpty ?? false;
  }

  /// 列表拼接转换成String separator：分隔符
  static String listToString(List list, String separator) {
    if (isEmpty(list)) return '';
    StringBuffer sbf = StringBuffer();
    int length = list.length;
    for (int i = 0; i < length; i++) {
      sbf.write(list[i]);
      if (i != length - 1) sbf.write(separator);
    }
    return sbf.toString();
  }
}
