import 'package:dio/dio.dart';
import 'package:flutter_tools/utils/toast_utils.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';
import '../../beans/api_bean.dart';
import '../../beans/api_bean_factory.dart';
import '../../widgets/loading_text_button.dart';
import 'net_exception_manager.dart';
import 'net_interceptor.dart';
import 'net_interceptor_code.dart';

/**
 * 封装的请求(可以使用callback 或者 Future形式)
 *    返回接收泛型，返回解析默认会在外层包裹ApiBean<T> ，除非泛型是Map，泛型说明（默认）：
 *    // 如果传的泛型是ApiBean，默认不再使用ApiBean包裹，除非isUseBaseBean传true；
 *    // 如果传的泛型是Map，默认不再使用ApiBean包裹，除非isUseBaseBean传true；
 *    // 如果传的泛型是其他的 实体类/String/int/num ，默认使用ApiBean包裹，除非isUseBaseBean传false；
 * */

/// @Title   网络请求的实现
/// @Author: zhoubo
/// @CreateDate:  5/21/21 5:08 PM
class NetImpl {
  static NetImpl get instance => _getInstance();

  factory NetImpl() => _getInstance();

  static NetImpl? _instance;

  static NetImpl _getInstance() {
    _instance ??= NetImpl._internal();
    return _instance!;
  }

  late BaseOptions _netOptions;
  late Dio _dio;

  NetImpl._internal() {
    _netOptions = BaseOptions(
        // baseUrl: _BASEURL,
        //连接时间为15秒
        connectTimeout: 30000,
        //响应时间为15秒
        receiveTimeout: 30000,
        //默认值是"application/json; charset=utf-8",Headers.formUrlEncodedContentType会自动编码请求体.
        // contentType: Headers.formUrlEncodedContentType,
        //共有三种方式json,bytes(响应字节),stream（响应流）,plain
        responseType: ResponseType.json);
    _dio = Dio(_netOptions);
    //添加拦截器
    _dio.interceptors.add(netInterceptor);
  }

  /// 封装的请求(使用 callback 形式)
  /// 返回接收泛型，返回解析默认会在外层包裹ApiBean<T> ，除非泛型是Map，泛型说明（默认）：
  ///  // 如果传的泛型是ApiBean，默认不再使用ApiBean包裹，除非isUseBaseBean传true；
  ///  // 如果传的泛型是Map，默认不再使用ApiBean包裹，除非isUseBaseBean传true；
  ///  // 如果传的泛型是其他的 实体类/String/int/num ，默认使用ApiBean包裹，除非isUseBaseBean传false；
  void requestCallback<T>(String url,
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
      CancelToken? cancelToken,
      Function(T)? onSuccess,
      Function(int code, String msg, T)? onSuccess2,
      Function(int code, String msg)? onError,
      Function()? onComplete}) async {
    // 如果传了refreshController 并且正在刷新，则不展示缓冲圈
    if (null != refreshController &&
        (refreshController.isRefresh || refreshController.isLoading)) {
      isShowLoadingDialog = false;
    }
    Response? response;
    try {
      // 展示加载框
      if (isShowLoadingDialog) ToastUtils.showLoading();
      loadingButtonController?.startLoading();
      // 使用dio网络请求（请求方式、请求体、弹框）
      response = await _requestByOption(url,
          params: params,
          data: data,
          headers: headers,
          cancelToken: cancelToken,
          requestMethod: requestMethod,
          isFormParams: isFormParams);
      // 取消加载框
      if (isShowLoadingDialog) ToastUtils.dismissLoading();
      loadingButtonController?.stopLoading();
    } on DioError catch (e) {
      // 取消加载框
      if (isShowLoadingDialog) ToastUtils.dismissLoading();
      loadingButtonController?.stopLoading();
      // 刷新的取消只能在每个失败中以及最终成功时都添加，因为如果只在dio错误时添加的话，后面的失败后会不消失
      NetException exception = getDioError(e);
      _handleError(refreshController, exception.errCode, exception.errMsg,
          onError, onComplete);
      return;
    }
    // 判断返回网络状态
    if (response == null || response.data == null) {
      _handleError(refreshController, -1, '网络错误', onError, onComplete);
      return;
    }
    var responseCode = parseApiCode(response.data);
    var responseMsg = parseApiMsg(response.data);
    if (responseCode != null && isInterceptCode) {
      // 是标准的ApiBean实体  // 拦截code
      bool isInterceptCodeToError =
          interceptCodeToError(responseCode, responseMsg);
      if (isInterceptCodeToError) {
        _handleError(refreshController, responseCode, responseMsg ?? '',
            onError, onComplete);
        return;
      }
    }
    T? t;
    bool isRequestSuccess = false;
    try {
      // 解析返回的json为实体
      t = _parseJson(isUseBaseBean, response);
      isRequestSuccess = true;
    } catch (e) {
      isRequestSuccess = false;
      _handleError(refreshController, -3, '解析失败', onError, onComplete,
          realErrMsg: e.toString() ?? '');
      return;
    }
    if (isRequestSuccess) {
      // 如果全局捕捉错误，可以在这儿添加try-catch
      onSuccess2?.call(responseCode!, responseMsg!, t!);
      onSuccess?.call(t!);
      if (isDefaultRefreshSuccess) handleRefreshOnSuccess(refreshController);
      onComplete?.call();
    }
  }

  /// 封装的请求(使用 Future 形式)
  /// 返回接收泛型，返回解析默认会在外层包裹ApiBean<T> ，除非泛型是Map，泛型说明（默认）：
  ///  // 如果传的泛型是ApiBean，默认不再使用ApiBean包裹，除非isUseBaseBean传true；
  ///  // 如果传的泛型是Map，默认不再使用ApiBean包裹，除非isUseBaseBean传true；
  ///  // 如果传的泛型是其他的 实体类/String/int/num ，默认使用ApiBean包裹，除非isUseBaseBean传false；
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
    // 如果传了refreshController 并且正在刷新，则不展示缓冲圈
    if (null != refreshController &&
        (refreshController.isRefresh || refreshController.isLoading)) {
      isShowLoadingDialog = false;
    }
    Response? response;
    try {
      // 展示加载框
      if (isShowLoadingDialog) ToastUtils.showLoading();
      loadingButtonController?.startLoading();
      // 使用dio网络请求（请求方式、请求体、弹框）
      response = await _requestByOption(url,
          params: params,
          data: data,
          headers: headers,
          cancelToken: cancelToken,
          requestMethod: requestMethod,
          isFormParams: isFormParams);
      // 取消加载框
      if (isShowLoadingDialog) ToastUtils.dismissLoading();
      loadingButtonController?.stopLoading();
    } on DioError catch (e) {
      // 取消加载框
      if (isShowLoadingDialog) ToastUtils.dismissLoading();
      loadingButtonController?.stopLoading();
      // 刷新的取消只能在每个失败中以及最终成功时都添加，因为如果只在dio错误时添加的话，后面的失败后会不消失
      NetException exception = getDioError(e);
      _handleErrorFuture(refreshController, exception);
    }
    // 判断返回网络状态
    if (response == null || response.data == null) {
      _handleErrorFuture(refreshController, NetException(-1, '网络错误'));
    } else {
      var responseCode = parseApiCode(response.data);
      var responseMsg = parseApiMsg(response.data);
      if (responseCode != null && isInterceptCode) {
        // 是标准的ApiBean实体  // 拦截code
        bool isInterceptCodeToError =
            interceptCodeToError(responseCode, responseMsg);
        if (isInterceptCodeToError) {
          _handleErrorFuture(
              refreshController, NetException(responseCode, responseMsg ?? ''));
        }
      }
      // 解析返回的json为实体
      T? t;
      try {
        // 解析返回的json为实体
        t = _parseJson(isUseBaseBean, response);
      } catch (e) {
        _handleErrorFuture(refreshController, NetException(-3, '解析失败'),
            realErrMsg: e.toString() ?? '');
      }
      if (isDefaultRefreshSuccess) handleRefreshOnSuccess(refreshController);
      // 如果全局捕捉错误，可以在这儿添加try-catch
      return t;
    }
    return null;
  }

  /// 配置请求参数、开始请求
  Future<Response> _requestByOption(String url,
      {Map<String, dynamic>? params,
      dynamic data, // data参数（用于参数不是Map的请求）
      Map<String, dynamic>? headers,
      String requestMethod = 'get',
      bool isFormParams = false, // 是否是表单参数
      CancelToken? cancelToken}) async {
    // 添加公共参数
    //addCommonParams(params);
    Future<Response>? requestFuture;
    if (requestMethod == 'get') {
      // get请求拼接参数使用queryParameters
      requestFuture = _dio.request(url,
          cancelToken: cancelToken,
          queryParameters: params,
          options: Options(method: requestMethod, headers: headers));
    } else {
      // 其他的请求区别表单
      requestFuture = _dio.request(url,
          cancelToken: cancelToken,
          data: isFormParams && null != params
              ? FormData.fromMap(params)
              : (params ?? data),
          options: Options(method: requestMethod, headers: headers));
    }
    // 正式请求
    Response response = await requestFuture;
    requestFuture = null;
    return response;
  }

  /// 获取dio网络失败的信息
  NetException getDioError(DioError error) {
    switch (error.type) {
      case DioErrorType.cancel:
        // 请求取消
        return NetException(-2, "取消请求");
      case DioErrorType.connectTimeout:
        // 连接超时
        return NetException(-1, "连接超时");
      case DioErrorType.sendTimeout:
        // 请求超时
        return NetException(-1, "请求超时");
      case DioErrorType.receiveTimeout:
        // 响应超时
        return NetException(-1, "响应超时");
      case DioErrorType.response:
        // 404 503
        return NetException(-1, "请求失败");
      default:
        return NetException(-1, "网络错误");
    }
  }

  /// 解析返回的json为实体
  T? _parseJson<T>(bool? isUseBaseBean, Response response) {
    T? t;
    String beanType = T.toString();
    if (beanType == 'ApiBean' || beanType.startsWith('ApiBean<')) {
      // 如果是使用ApiBean接收，默认不再使用ApiBean包裹，除非传true；
      if (isUseBaseBean == null || !isUseBaseBean) {
        ApiBean apiBeanSimple = ApiBean.fromJson(response.data);
        t = apiBeanSimple as T;
      } else {
        ApiBean<T> apiBean = ApiBean<T>.fromJson(response.data);
        t = apiBean.data;
      }
    } else if (beanType == 'Map<String, dynamic>' ||
        beanType.startsWith('Map<String')) {
      // 如果是使用Map接收，默认不再使用ApiBean包裹，除非传true；
      if (isUseBaseBean == null || !isUseBaseBean) {
        t = response.data;
      } else {
        ApiBean<T> apiBean = ApiBean<T>.fromJson(response.data);
        t = apiBean.data;
      }
    } else {
      // 其他的 实体类/String/int/num 接收，默认使用ApiBean包裹，除非传false；
      if (isUseBaseBean ?? true) {
        ApiBean<T> apiBean = ApiBean<T>.fromJson(response.data);
        t = apiBean.data;
      } else {
        t = ApiBeanFactory.fromJsonAsT<T>(response.data);
      }
    }
    return t;
  }

  /// 处理错误
  _handleError(RefreshController? refreshController, int errCode, String errMsg,
      Function(int code, String msg)? onError, Function()? onComplete,
      {String? realErrMsg}) {
    // 取消刷新
    handleRefreshOnError(refreshController);
    handleNetException(errCode, errMsg, onError, realErrMsg: realErrMsg);
    onComplete?.call();
  }

  /// 处理错误
  _handleErrorFuture(
      RefreshController? refreshController, NetException netException,
      {String? realErrMsg}) {
    // 取消刷新
    handleRefreshOnError(refreshController);
    handleNetExceptionFuture(netException, realErrMsg: realErrMsg);
  }

  /// 失败后处理刷新
  handleRefreshOnError(RefreshController? refreshController) {
    if (null == refreshController) return;
    // 正在刷新，结束刷新
    if (refreshController.isRefresh) {
      refreshController.refreshCompleted();
    } else if (refreshController.isLoading) {
      // 正在上拉加载，加载失败
      refreshController.loadFailed();
    }
  }

  /// 请求成功后处理刷新(公共网络的处理，不判断最后一页，如果比较详细判断，则使用NetPagingApi中判断)
  handleRefreshOnSuccess(RefreshController? refreshController) {
    if (null != refreshController) {
      // 正在刷新，结束刷新
      if (refreshController.isRefresh) {
        refreshController.refreshCompleted();
      } else if (refreshController.isLoading) {
        refreshController.loadComplete();
      }
    }
  }
}
