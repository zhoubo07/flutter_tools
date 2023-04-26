import 'package:dio/dio.dart';
import '../../utils/log/log.dart';
import 'net_api.dart';
import 'net_common_params.dart';

/// @Title   网络拦截器
/// @Author: zhoubo
/// @CreateDate:  1/25/21 5:24 PM

/// 实现网络拦截器
InterceptorsWrapper get netInterceptor {
  return InterceptorsWrapper(onRequest: (options, handler) {
    //LogUtil.d("在请求之前的拦截信息");
    commonParams()
        .forEach((key, value) => options.headers.putIfAbsent(key, () => value));
    return handler.next(options);
  }, onResponse: (response, handler) {
    //LogUtil.d("在响应之前的拦截信息");
    _printResponse(response);
    return handler.next(response);
  }, onError: (e, handler) {
    _printError(e);
    return handler.next(e); //continue
  });
}

_printResponse(Response response) {
  Log.d(response.requestOptions.path, tag: '${netLogTag}u');
  Log.d(response.requestOptions.headers, tag: '${netLogTag}h', isJson: true);
  try {
    var params =
        response.requestOptions.data ?? response.requestOptions.queryParameters;
    Log.d(params, tag: '${netLogTag}p', isJson: true);
  } catch (e) {}
  Log.d(response.data, tag: '${netLogTag}r', isJson: true);
}

_printError(DioError e) {
  Log.e(e.requestOptions.path, tag: '${netLogTag}u');
  try {
    var params = e.requestOptions.data ?? e.requestOptions.queryParameters;
    Log.e(params, tag: '${netLogTag}p', isJson: true);
  } catch (e) {}
  Log.e(e.message, tag: '${netLogTag}e');
}
