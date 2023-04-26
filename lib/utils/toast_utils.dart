import 'package:flutter/material.dart';
import 'package:flutter_smart_dialog/flutter_smart_dialog.dart';

import '../widgets/loading_view.dart';

class ToastUtils {
  factory ToastUtils() => _instance;
  static final ToastUtils _instance = ToastUtils._internal();

  ToastUtils._internal();

  static TransitionBuilder init({
    TransitionBuilder? builder,
  }) {
    return FlutterSmartDialog.init(builder: builder);
  }

  static final observer = FlutterSmartDialog.observer;

  /// 加载框是否正在展示
  static bool get isLoadingShowing => SmartDialog.config.isExistLoading;

  static Future<void> showLoading({
    bool? clickBgDismiss = false,
    bool? backDismiss = true,
    bool? usePenetrate,
    Color? maskColor,
    String? text,
    TextStyle? textStyle,
    Duration? duration,
  }) {
    if (isLoadingShowing) return Future.value();
    return SmartDialog.showLoading(
      builder: (context) => text != null
          ? Column(
              mainAxisSize: MainAxisSize.min,
              children: [const LoadingView(), Text(text, style: textStyle)],
            )
          : const LoadingView(),
      maskColor: maskColor ?? Colors.transparent,
      clickMaskDismiss: clickBgDismiss,
      backDismiss: backDismiss,
      usePenetrate: usePenetrate,
      displayTime: duration,
    );
  }

  static Future<void> dismissLoading() {
    return SmartDialog.dismiss(status: SmartStatus.loading);
  }

  /// Toast 展示
  ///
  /// [duration] 默认2秒
  static Future<void> showToast(
    String? text, {
    BuildContext? context,
    Duration? duration,
    VoidCallback? onDismiss,
  }) async {
    if (text == null || text.isEmpty) return;
    await SmartDialog.showToast(
      text,
      alignment: Alignment.bottomCenter,
      animationTime: duration,
      displayType: SmartToastType.last,
    );

    if (onDismiss != null) onDismiss();
  }

  /// Toast 成功展示
  ///
  /// [duration] 默认2秒
  static Future<void> showSuccess(String? text, {Duration? duration}) {
    Widget widget = _getIndicatorTextWidget(
        text, const Icon(Icons.done, color: Colors.white, size: 40));
    return SmartDialog.showToast(
      '',
      builder: (_) => widget,
      alignment: Alignment.center,
      animationTime: duration,
      displayType: SmartToastType.last,
    );
  }

  /// Toast失败展示
  ///
  /// [duration] 默认2秒
  static Future<void> showError(String? text, {Duration? duration}) {
    Widget widget = _getIndicatorTextWidget(
        text, const Icon(Icons.clear, color: Colors.white, size: 40));
    return SmartDialog.showToast(
      '',
      builder: (_) => widget,
      alignment: Alignment.center,
      animationTime: duration,
      displayType: SmartToastType.last,
    );
  }

  /// 获取带指示器的文本显示组件
  static Widget _getIndicatorTextWidget(String? text, Widget icon) {
    return Container(
      margin: const EdgeInsets.all(50.0),
      decoration: BoxDecoration(
        color: Colors.black.withOpacity(0.9), // 可传
        borderRadius: BorderRadius.circular(10),
      ),
      constraints: const BoxConstraints(minWidth: 100),
      padding: const EdgeInsets.symmetric(vertical: 15.0, horizontal: 20.0),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          Container(
            margin: text?.isNotEmpty == true
                ? const EdgeInsets.only(bottom: 10.0)
                : EdgeInsets.zero,
            child: icon,
          ),
          if (text != null && text.isNotEmpty)
            Text(
              text,
              style: const TextStyle(color: Colors.white, fontSize: 15),
              textAlign: TextAlign.center,
            ),
        ],
      ),
    );
  }
}
