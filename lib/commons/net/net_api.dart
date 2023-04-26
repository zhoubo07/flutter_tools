
import 'package:dio/dio.dart';
import 'package:flutter_tools/commons/net/sp_urls.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../beans/upload_bean.dart';
import '../../utils/log/log.dart';
import '../../widgets/loading_text_button.dart';
import 'net_impl.dart';

/// 网络日志前缀
const String netLogTag = '-wwwwww-';

/**
 * 封装的请求(可以使用callback 或者 Future形式)
 *    返回接收泛型，返回解析默认会在外层包裹ApiBean<T> ，除非泛型是Map，泛型说明（默认）：
 *    // 如果传的泛型是ApiBean，默认不再使用ApiBean包裹，除非 isUseBaseBean 传true；
 *    // 如果传的泛型是Map，默认不再使用ApiBean包裹，除非 isUseBaseBean 传true；
 *    // 如果传的泛型是其他的：实体类/String/int/num ，默认使用ApiBean包裹，除非 isUseBaseBean 传false；
 * */

/// @title      网络请求工具
/// @author:    zhoubo
/// @CreateDate:  2020/11/27 4:39 PM
class NetApi {
  static NetApi get instance => _getInstance();

  factory NetApi() => _getInstance();

  static NetApi? _instance;

  static NetApi _getInstance() {
    return _instance ??= NetApi._internal();
  }

  NetApi._internal();

  /**
   *   /////////////////////////////////////////////////////////////////////////////////
   *   //
   *   //                      网络 Api
   *   //
   *   /////////////////////////////////////////////////////////////////////////////////
   */

  /// 封装的get请求（ 泛型和isUseBaseBean说明查看上方的注释说明 ）
  void get<T>(String url,
      {Map<String, dynamic>? params,
      Map<String, dynamic>? headers,
      bool isShowLoadingDialog = false,
      bool? isUseBaseBean, // 返回实体是否使用ApiBean包裹:默认情况：上方注释
      bool isInterceptCode = true, // 是否拦截code
      LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
      RefreshController? refreshController, // 刷新加载的控制器(get请求可能会有刷新)
      bool isDefaultRefreshSuccess = true, // 是否需要默认处理刷新成功事件（默认处理）
      CancelToken? cancelToken,
      Function(T)? onSuccess,
      Function(int code, String msg, T)? onSuccess2,
      Function(int code, String msg)? onError,
      Function()? onComplete}) {
    // 发送网络请求
    request<T>(url,
        params: params,
        headers: headers,
        requestMethod: 'get',
        loadingButtonController: loadingButtonController,
        refreshController: refreshController,
        cancelToken: cancelToken,
        isShowLoadingDialog: isShowLoadingDialog,
        isUseBaseBean: isUseBaseBean,
        isInterceptCode: isInterceptCode,
        onSuccess: onSuccess,
        onSuccess2: onSuccess2,
        onError: onError,
        onComplete: onComplete);
  }

  /// 封装的get请求(Future形式)（ 泛型和isUseBaseBean说明查看上方的注释说明 ）
  Future<T?> getFuture<T>(String url,
      {Map<String, dynamic>? params,
      Map<String, dynamic>? headers,
      LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
      RefreshController? refreshController, // 刷新加载的控制器(get请求可能会有刷新)
      bool isDefaultRefreshSuccess = true, // 是否需要默认处理刷新成功事件（默认处理）
      CancelToken? cancelToken,
      bool isShowLoadingDialog = false,
      bool? isUseBaseBean, // 返回实体是否使用ApiBean包裹
      bool isInterceptCode = true // 是否拦截code
      }) async {
    return requestFuture<T>(url,
        params: params,
        headers: headers,
        requestMethod: 'get',
        loadingButtonController: loadingButtonController,
        refreshController: refreshController,
        cancelToken: cancelToken,
        isShowLoadingDialog: isShowLoadingDialog,
        isUseBaseBean: isUseBaseBean,
        isInterceptCode: isInterceptCode);
  }

  /// 封装的post json 请求（ 泛型和isUseBaseBean说明查看上方的注释说明 ）
  void post<T>(String url,
      {Map<String, dynamic>? params,
      dynamic data, // data参数（用于参数不是Map的请求）
      Map<String, dynamic>? headers,
      bool isShowLoadingDialog = false,
      bool isFormParams = false, //是否是表单参数
      bool? isUseBaseBean, // 返回实体是否使用ApiBean包裹
      bool isInterceptCode = true, // 是否拦截code
      LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
      CancelToken? cancelToken,
      Function(T)? onSuccess,
      Function(int code, String msg, T)? onSuccess2,
      Function(int code, String msg)? onError,
      Function()? onComplete}) {
    request<T>(url,
        params: params,
        data: data,
        headers: headers,
        requestMethod: 'post',
        loadingButtonController: loadingButtonController,
        cancelToken: cancelToken,
        isShowLoadingDialog: isShowLoadingDialog,
        isFormParams: isFormParams,
        isUseBaseBean: isUseBaseBean,
        isInterceptCode: isInterceptCode,
        onSuccess: onSuccess,
        onSuccess2: onSuccess2,
        onError: onError,
        onComplete: onComplete);
  }

  /// 封装的post json 请求（Future）（ 泛型和isUseBaseBean说明查看上方的注释说明 ）
  Future<T?> postFuture<T>(String url,
      {Map<String, dynamic>? params,
      dynamic data, // data参数（用于参数不是Map的请求）
      Map<String, dynamic>? headers,
      bool isShowLoadingDialog = false,
      bool isFormParams = false, //是否是表单参数
      bool? isUseBaseBean, // 返回实体是否使用ApiBean包裹
      bool isInterceptCode = true, // 是否拦截code
      LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
      CancelToken? cancelToken}) async {
    return requestFuture<T>(url,
        params: params,
        data: data,
        headers: headers,
        requestMethod: 'post',
        loadingButtonController: loadingButtonController,
        cancelToken: cancelToken,
        isShowLoadingDialog: isShowLoadingDialog,
        isFormParams: isFormParams,
        isUseBaseBean: isUseBaseBean,
        isInterceptCode: isInterceptCode);
  }

  /// 封装的 put 请求（ 泛型和isUseBaseBean说明查看上方的注释说明 ）
  void put<T>(String url,
      {Map<String, dynamic>? params,
      dynamic data, // data参数（用于参数不是Map的请求）
      Map<String, dynamic>? headers,
      bool isShowLoadingDialog = false,
      bool isFormParams = false, //是否是表单参数
      bool? isUseBaseBean, // 返回实体是否使用ApiBean包裹
      bool isInterceptCode = true, // 是否拦截code
      LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
      CancelToken? cancelToken,
      Function(T)? onSuccess,
      Function(int code, String msg, T)? onSuccess2,
      Function(int code, String msg)? onError,
      Function()? onComplete}) {
    request<T>(url,
        params: params,
        data: data,
        headers: headers,
        requestMethod: 'put',
        loadingButtonController: loadingButtonController,
        cancelToken: cancelToken,
        isShowLoadingDialog: isShowLoadingDialog,
        isFormParams: isFormParams,
        isUseBaseBean: isUseBaseBean,
        isInterceptCode: isInterceptCode,
        onSuccess: onSuccess,
        onSuccess2: onSuccess2,
        onError: onError,
        onComplete: onComplete);
  }

  /// 封装的 put 请求（Future）（ 泛型和isUseBaseBean说明查看上方的注释说明 ）
  Future<T?> putFuture<T>(String url,
      {Map<String, dynamic>? params,
      dynamic data, // data参数（用于参数不是Map的请求）
      Map<String, dynamic>? headers,
      bool isShowLoadingDialog = false,
      bool isFormParams = false, //是否是表单参数
      bool? isUseBaseBean, // 返回实体是否使用ApiBean包裹
      bool isInterceptCode = true, // 是否拦截code
      LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
      CancelToken? cancelToken}) async {
    return requestFuture<T>(url,
        params: params,
        data: data,
        headers: headers,
        requestMethod: 'put',
        loadingButtonController: loadingButtonController,
        cancelToken: cancelToken,
        isShowLoadingDialog: isShowLoadingDialog,
        isFormParams: isFormParams,
        isUseBaseBean: isUseBaseBean,
        isInterceptCode: isInterceptCode);
  }

  /// 封装的 delete 请求（ 泛型和isUseBaseBean说明查看上方的注释说明 ）
  void delete<T>(String url,
      {Map<String, dynamic>? params,
      dynamic data, // data参数（用于参数不是Map的请求）
      Map<String, dynamic>? headers,
      bool isShowLoadingDialog = false,
      bool isFormParams = false, //是否是表单参数
      bool? isUseBaseBean, // 返回实体是否使用ApiBean包裹
      bool isInterceptCode = true, // 是否拦截code
      LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
      CancelToken? cancelToken,
      Function(T)? onSuccess,
      Function(int code, String msg, T)? onSuccess2,
      Function(int code, String msg)? onError,
      Function()? onComplete}) {
    request<T>(url,
        params: params,
        data: data,
        headers: headers,
        requestMethod: 'delete',
        loadingButtonController: loadingButtonController,
        cancelToken: cancelToken,
        isShowLoadingDialog: isShowLoadingDialog,
        isFormParams: isFormParams,
        isUseBaseBean: isUseBaseBean,
        isInterceptCode: isInterceptCode,
        onSuccess: onSuccess,
        onSuccess2: onSuccess2,
        onError: onError,
        onComplete: onComplete);
  }

  /// 封装的 put 请求（Future）（ 泛型和isUseBaseBean说明查看上方的注释说明 ）
  Future<T?> deleteFuture<T>(String url,
      {Map<String, dynamic>? params,
      dynamic data, // data参数（用于参数不是Map的请求）
      Map<String, dynamic>? headers,
      bool isShowLoadingDialog = false,
      bool isFormParams = false, //是否是表单参数
      bool? isUseBaseBean, // 返回实体是否使用ApiBean包裹
      bool isInterceptCode = true, // 是否拦截code
      LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
      CancelToken? cancelToken}) async {
    return requestFuture<T>(url,
        params: params,
        data: data,
        headers: headers,
        requestMethod: 'delete',
        loadingButtonController: loadingButtonController,
        cancelToken: cancelToken,
        isShowLoadingDialog: isShowLoadingDialog,
        isFormParams: isFormParams,
        isUseBaseBean: isUseBaseBean,
        isInterceptCode: isInterceptCode);
  }

  /// 封装的通用请求 （ 泛型和isUseBaseBean说明查看上方的注释说明 ）
  void request<T>(String url,
      {Map<String, dynamic>? params,
      dynamic data, // data参数（用于参数不是Map的请求）
      Map<String, dynamic>? headers,
      String requestMethod = 'get', // 'post','put','delete'
      bool isShowLoadingDialog = false,
      bool isFormParams = false, //是否是表单参数
      bool? isUseBaseBean, // 返回实体是否使用ApiBean包裹
      bool isInterceptCode = true, // 是否拦截code
      LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
      RefreshController? refreshController, // 刷新加载的控制器(get请求可能会有刷新)
      bool isDefaultRefreshSuccess = true, // 是否需要默认处理刷新成功事件（默认处理）
      CancelToken? cancelToken,
      Function(T)? onSuccess,
      Function(int code, String msg, T)? onSuccess2,
      Function(int code, String msg)? onError,
      Function()? onComplete}) async {
    NetImpl.instance.requestCallback<T>(url,
        params: params,
        data: data,
        headers: headers,
        loadingButtonController: loadingButtonController,
        refreshController: refreshController,
        isDefaultRefreshSuccess: isDefaultRefreshSuccess,
        cancelToken: cancelToken,
        requestMethod: requestMethod,
        isShowLoadingDialog: isShowLoadingDialog,
        isFormParams: isFormParams,
        isUseBaseBean: isUseBaseBean,
        isInterceptCode: isInterceptCode,
        onSuccess: onSuccess,
        onSuccess2: onSuccess2,
        onError: onError,
        onComplete: onComplete);
  }

  /// 封装的通用请求(Future形式) （ 泛型和isUseBaseBean说明查看上方的注释说明 ）
  Future<T?> requestFuture<T>(String url,
      {Map<String, dynamic>? params,
      dynamic data, // data参数（用于参数不是Map的请求）
      Map<String, dynamic>? headers,
      String requestMethod = 'get', // 'post','put','delete'
      bool isFormParams = false, //是否是表单参数
      bool isShowLoadingDialog = false,
      bool? isUseBaseBean, // 返回实体是否使用ApiBean包裹
      bool isInterceptCode = true, // 是否拦截code
      LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
      RefreshController? refreshController, // 刷新加载的控制器(get请求可能会有刷新)
      bool isDefaultRefreshSuccess = true, // 是否需要默认处理刷新成功事件（默认处理）
      CancelToken? cancelToken}) async {
    return NetImpl.instance.requestFuture<T>(url,
        params: params,
        data: data,
        headers: headers,
        loadingButtonController: loadingButtonController,
        refreshController: refreshController,
        isDefaultRefreshSuccess: isDefaultRefreshSuccess,
        cancelToken: cancelToken,
        requestMethod: requestMethod,
        isShowLoadingDialog: isShowLoadingDialog,
        isFormParams: isFormParams,
        isUseBaseBean: isUseBaseBean,
        isInterceptCode: isInterceptCode);
  }

  /// 单文件上传
  void upload(String filePath,
      {bool isShowLoadingDialog = true,
      LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
      Function(UploadFileBean fileBean)? success,
      Function(int code, String msg)? onError}) async {
    var filename =
        filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length);
    Map<String, dynamic> params = {
      'file': await MultipartFile.fromFile(filePath, filename: filename),
    };

    post(SpUrls.uploadSingleFile,
        params: params,
        isShowLoadingDialog: isShowLoadingDialog,
        loadingButtonController: loadingButtonController,
        isFormParams: true,
        isUseBaseBean: true,
        onSuccess: success,
        onError: onError);
  }

  /// 文件下载，返回字节数组；eg：可以使用 file.writeAsBytesSync(bytes) 写入文件;
  Future<List<int>?> download(String downUrl,
      {ProgressCallback? progressCallback}) async {
    try {
      Response response = await Dio().get(downUrl,
          onReceiveProgress: progressCallback,
          options: Options(
            responseType: ResponseType.bytes,
            followRedirects: false,
          ));
      return response.data;
    } catch (e) {
      Log.e(e, tag: '${netLogTag}e');
      return Future.value();
    }
  }
}
