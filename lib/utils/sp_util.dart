import 'package:shared_preferences/shared_preferences.dart';
import 'package:synchronized/synchronized.dart';

/// @Title: 本地Sp存储的工具类
/// @Author: zhoubo
/// @CreateDate: 2020年12月24日11:24:54
class SpUtils {
  static SpUtils? _singleton;
  static SharedPreferences? _prefs;
  static Lock lock = Lock();

  static Future<SpUtils> getInstance() async {
    _singleton ??= await lock.synchronized(() async {
      if (_singleton == null) {
        // 保持本地实例直到完全初始化。
        var singleton = SpUtils._();
        await singleton._init();
        _singleton = singleton;
      }
    });

    return _singleton!;
  }

  SpUtils._();

  Future _init() async {
    _prefs = await SharedPreferences.getInstance();
  }

  /// 存储数据
  static Future<bool> setData<T>(String? key, T value) {
    if (_prefs == null || key == null) return Future.value(false);
    if (value == null) return remove(key);
    switch (T) {
      case String:
        return _prefs!.setString(key, value as String);
      case int:
        return _prefs!.setInt(key, value as int);
      case double:
        return _prefs!.setDouble(key, value as double);
      case bool:
        return _prefs!.setBool(key, value as bool);
      case List:
        return _prefs!.setStringList(key, value as List<String>);
      default:
        return Future.value(false);
    }
  }

  /// 返回数据data -> replace -> null
  static get(String key, [dynamic replace]) {
    if (_prefs == null) return null;
    var data = _prefs!.get(key);
    return data ?? replace ?? null;
  }

  static Future<bool> remove(String? key) {
    if (_prefs == null || null == key) return Future.value(false);
    return _prefs!.remove(key);
  }

  static Future<bool> clearAll() {
    if (_prefs == null) return Future.value(false);
    return _prefs!.clear();
  }

  static SharedPreferences? get getPrefs => _prefs;
}
