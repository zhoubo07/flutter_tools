import 'package:dio/dio.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';
import '../../beans/api_paging_bean.dart';
import '../../widgets/loading_text_button.dart';
import 'net_exception_manager.dart';
import 'net_impl.dart';

/**
 *  请求示例：（固定分页格式PagingBean的网络封装）
 *      : MyBean需要在ApiBeanFactory中注册以便接收解析
 *      : refreshController为SmartRefresher的控制器（不传则不控制刷新/加载状态变化）
 *  NetPagingApi.instance.get(url,params: params,refreshController:refreshController,
 *      onSuccess:(PagingBean<MyBean> pagingBean){
 *          List<MyBean> myBeanList = pagingBean.records;
 *      })
 * */

/// @Title   分页加载的网络API
/// （固定分页格式PagingBean的网络封装）
/// @Author: zhoubo
/// @CreateDate:  5/24/21 3:17 PM
class NetPagingApi {
  static NetPagingApi get instance => _getInstance();

  factory NetPagingApi() => _getInstance();

  static NetPagingApi? _instance;

  static NetPagingApi _getInstance() {
    _instance ??= NetPagingApi._internal();
    return _instance!;
  }

  NetPagingApi._internal();

  /// 封装的get请求
  void get<T>(String url,
      {Map<String, dynamic>? params,
      Map<String, dynamic>? headers,
      bool? isShowLoadingDialog, // 默认值逻辑在下方request
      LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
      RefreshController? refreshController, // 刷新加载的控制器
      Function(PagingBean<T>)? onSuccess,
      Function(int code, String msg, PagingBean<T>)? onSuccess2,
      Function(int code, String msg)? onError,
      Function()? onComplete}) {
    // 发送网络请求
    _request<T>(url,
        params: params,
        headers: headers,
        requestMethod: 'get',
        isShowLoadingDialog: isShowLoadingDialog,
        loadingButtonController: loadingButtonController,
        refreshController: refreshController,
        onSuccess: onSuccess,
        onSuccess2: onSuccess2,
        onError: onError,
        onComplete: onComplete);
  }

  /// 封装的get请求(Future形式)
  Future<PagingBean<T>> getFuture<T>(
    String url, {
    Map<String, dynamic>? params,
    Map<String, dynamic>? headers,
    bool isShowLoadingDialog = false, // 默认值逻辑在下方request
    LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
    RefreshController? refreshController, // 刷新加载的控制器
  }) async {
    return _requestFuture<T>(
      url,
      params: params,
      headers: headers,
      requestMethod: 'get',
      isShowLoadingDialog: isShowLoadingDialog,
      loadingButtonController: loadingButtonController,
      refreshController: refreshController,
    );
  }

  void _request<T>(String url,
      {Map<String, dynamic>? params,
      Map<String, dynamic>? headers,
      String requestMethod = 'get', // 'post','put','delete'
      bool? isShowLoadingDialog,
      bool isFormParams = false, //是否是表单参数
      CancelToken? cancelToken,
      LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
      RefreshController? refreshController, // 刷新加载的控制器
      Function(PagingBean<T>)? onSuccess,
      Function(int code, String msg, PagingBean<T>)? onSuccess2,
      Function(int code, String msg)? onError,
      Function()? onComplete}) {
    NetImpl.instance.requestCallback<Map<String, dynamic>>(url,
        params: params,
        headers: headers,
        loadingButtonController: loadingButtonController,
        refreshController: refreshController,
        isDefaultRefreshSuccess: false,
        cancelToken: cancelToken,
        requestMethod: requestMethod,
        isShowLoadingDialog: isShowLoadingDialog ?? false,
        isFormParams: isFormParams, onError: (int code, String msg) {
      onError?.call(code, msg);
      onComplete?.call();
    }, onSuccess2: (code, msg, apiPagingJson) {
      PagingBean<T>? pagingBean;
      try {
        pagingBean = _parsePagingBeanJson<T>(apiPagingJson);
      } catch (e) {
        // 失败后处理刷新
        handleRefreshOnError(refreshController);
        handleNetException(-3, '解析失败', onError, realErrMsg: e.toString() ?? '');
        return;
      }
      // 请求成功后处理刷新
      handleRefreshOnSuccess(
          pagingBean?.isLastPage ?? false, refreshController);
      onSuccess?.call(pagingBean!);
      onSuccess2?.call(code, msg, pagingBean!);
      onComplete?.call();
    });
  }

  Future<PagingBean<T>> _requestFuture<T>(String url,
      {Map<String, dynamic>? params,
      Map<String, dynamic>? headers,
      String requestMethod = 'get', // 'post','put','delete'
      bool isShowLoadingDialog = false,
      bool isFormParams = false, //是否是表单参数
      CancelToken? cancelToken,
      LoadingButtonController? loadingButtonController, // 带加载圈的按钮控制器
      RefreshController? refreshController // 刷新加载的控制器
      }) async {
    // 请求接口
    Map<String, dynamic>? apiPagingJson = await NetImpl.instance
        .requestFuture<Map<String, dynamic>>(url,
            params: params,
            headers: headers,
            loadingButtonController: loadingButtonController,
            refreshController: refreshController,
            isDefaultRefreshSuccess: false,
            cancelToken: cancelToken,
            requestMethod: requestMethod,
            isShowLoadingDialog: isShowLoadingDialog,
            isFormParams: isFormParams);
    PagingBean<T>? pagingBean;
    try {
      pagingBean = _parsePagingBeanJson<T>(apiPagingJson);
    } catch (e) {
      // 失败后处理刷新
      handleRefreshOnError(refreshController);
      handleNetExceptionFuture(NetException(-3, '解析失败'),
          realErrMsg: e.toString() ?? '');
    }
    // 请求成功后处理刷新
    handleRefreshOnSuccess(pagingBean?.isLastPage ?? false, refreshController);
    return pagingBean!;
  }

  /// 解析json到PagingBean实体
  PagingBean<T>? _parsePagingBeanJson<T>(Map<String, dynamic>? apiPagingJson) {
    if (null == apiPagingJson) return null;
    ApiPagingBean<T> apiPagingBean = ApiPagingBean<T>.fromJson(apiPagingJson);
    return apiPagingBean.data;
  }

  /// 请求成功后处理刷新
  handleRefreshOnSuccess(
      bool isLastPage, RefreshController? refreshController) {
    if (null != refreshController) {
      // 正在刷新，结束刷新
      if (refreshController.isRefresh) {
        refreshController.refreshCompleted();
        // 不是最后一页重置上拉，是最后一页禁止上拉
        !isLastPage
            ? refreshController.resetNoData()
            : refreshController.loadNoData();
      } else if (refreshController.isLoading) {
        // 不是最后一页正常加载结束，是最后一页禁止上拉
        !isLastPage
            ? refreshController.loadComplete()
            : refreshController.loadNoData();
      } else {
        // 不是最后一页重置上拉，是最后一页禁止上拉
        !isLastPage
            ? refreshController.resetNoData()
            : refreshController.loadNoData();
      }
    }
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
}
