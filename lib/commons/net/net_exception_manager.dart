import '../../utils/log/log.dart';
import '../../utils/toast_utils.dart';
import 'net_api.dart';

/// 默认处理网络错误的方法（callback方式）
/// errCode：-1:网络状态问题； -2:请求取消：-3:解析错误；xx:业务逻辑错误拦截code
void handleNetException(
    int errCode, String errMsg, Function(int code, String msg)? onError,
    {String? realErrMsg}) {
  // 取消请求不吐司
  // -1:网络状态问题； -2:请求取消：-3:解析错误；xx:业务逻辑错误拦截code
  if (errCode != -2) {
    if(errCode<0){
      // 网络错误弹出错误的提示样式
      ToastUtils.showError(errMsg);
    }else{
      // 业务错误弹出普通的吐司样式
      ToastUtils.showToast(errMsg);
    }
    Log.e(realErrMsg ?? errMsg, tag: '${netLogTag}e');
    onError?.call(errCode, errMsg);
  }
}

/// 默认处理网络错误的方法（Future方式--抛到catchError）
/// errCode：-1:网络状态问题； -2:请求取消：-3:解析错误；xx:业务逻辑错误拦截code
void handleNetExceptionFuture(NetException netException,
    {String? realErrMsg}) {
  // 取消请求不吐司
  if (netException.errCode != -2) {
    if(netException.errCode<0){
      // 网络错误弹出错误的提示样式
      ToastUtils.showError(netException.errMsg);
    }else{
      // 业务错误弹出普通的吐司样式
      ToastUtils.showToast(netException.errMsg);
    }
    Log.e(realErrMsg ?? netException.errMsg, tag: '${netLogTag}e');
    throw netException;
  }
}

/// @Title   网络异常类
/// @Author: zhoubo
/// @CreateDate:  5/28/21 10:27 AM
class NetException {
  final int errCode;
  final String errMsg;

  NetException(this.errCode, this.errMsg);
}
