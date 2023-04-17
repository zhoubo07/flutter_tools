import 'dart:convert';

/// @Title  json字符串格式化
/// @Author: zhoubo
/// @CreateDate:  5/12/21 6:28 PM
String jsonFormat(Object? message) {
  try {
    if (null == message) return '';
    if ((message is String) && message.isEmpty) return '';
    // 判断message是否是基础数据类型
    if (message is int || message is double || message is bool) {
      return message.toString();
    }
    String json = (message is String) ? message : jsonEncode(message);
    if (json.startsWith("{") || json.startsWith('[')) {
      return _convert(const JsonCodec().decode(json), 0);
    } else {
      //错误的json格式
      return "Wrong format: $json";
    }
  } catch (e) {
    return "jsonEncode Error\nmsg.toString: ${message.toString()}";
  }
}

/// [object]  解析的对象
/// [deep]  递归的深度，用来获取缩进的空白长度
/// [isObject] 用来区分当前map或list是不是来自某个字段，则不用显示缩进。单纯的map或list需要添加缩进
String _convert(dynamic object, int deep, {bool isObject = false}) {
  var buffer = StringBuffer();
  var nextDeep = deep + 1;

  if (!isObject) {
    //如果来自某个字段，则不需要显示缩进
    buffer.write(getDeepSpace(deep));
  }

  if (object is Map) {
    var list = object.keys.toList();
    buffer.write("{");
    if (list.isEmpty) {
      //当map为空，直接返回‘}’
      buffer.write("}");
    } else {
      buffer.write("\n");
      for (int i = 0; i < list.length; i++) {
        buffer.write("${getDeepSpace(nextDeep)}\"${list[i]}\":");
        buffer.write(_convert(object[list[i]], nextDeep, isObject: true));
        if (i < list.length - 1) {
          buffer.write(",");
          buffer.write("\n");
        }
      }
      buffer.write("\n");
      buffer.write("${getDeepSpace(deep)}}");
    }
  } else if (object is List) {
    buffer.write("[");
    if (object.isEmpty) {
      //当list为空，直接返回‘]’
      buffer.write("]");
    } else {
      buffer.write("\n");
      for (int i = 0; i < object.length; i++) {
        buffer.write(_convert(object[i], nextDeep));
        if (i < object.length - 1) {
          buffer.write(",");
          buffer.write("\n");
        }
      }
      buffer.write("\n");
      buffer.write("${getDeepSpace(deep)}]");
    }
  } else if (object is String) {
    //为字符串时，需要添加双引号并返回当前内容
    buffer.write("\"$object\"");
  } else if (object is num || object is bool) {
    //为数字或者布尔值时，返回当前内容
    buffer.write(object);
  } else {
    //如果对象为空，则返回null字符串
    buffer.write("null");
  }
  return buffer.toString();
}

/// 获取缩进空白符
String getDeepSpace(int deep) {
  var tab = StringBuffer();
  for (int i = 0; i < deep; i++) {
    // tab.write("\t");
    tab.write("  ");
  }
  return tab.toString();
}
