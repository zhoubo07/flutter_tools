import 'package:flutter/material.dart';

import '../commons/common_colors.dart';

/// @Title   带加载状态的按钮
/// @Author: zhoubo
/// @CreateDate:  12/27/21 5:36 PM
class LoadingTextButton extends StatefulWidget {
  // 加载动画的控制器
  final LoadingButtonController controller;
  final double height; // 组件高度
  final double? width; // 组件宽度
  final EdgeInsetsGeometry? margin; // 组件margin
  final EdgeInsetsGeometry? padding; // 组件margin
  final double loadingDiam; // 加载圈直径
  final String? text;
  final double textSize;
  final Color textColor;
  final FontWeight textFontWeight;
  final Widget? textWidget;
  final Decoration? decoration;
  final GestureTapCallback? onTap;

  const LoadingTextButton(this.controller,
      {Key? key,
      this.height = 36,
      this.width,
      this.loadingDiam = 14,
      this.margin,
      this.padding,
      this.textWidget,
      this.decoration,
      this.text,
      this.textSize = 16,
      this.textColor = Colors.white,
      this.textFontWeight = FontWeight.bold,
      this.onTap})
      : super(key: key);

  @override
  State<StatefulWidget> createState() => _LoadingTextButtonState();
}

class _LoadingTextButtonState extends State<LoadingTextButton> {
  bool _isLoading = false; // 正在加载

  @override
  void initState() {
    super.initState();
    _isLoading = false;
    widget.controller.bindStartLoadingMethod = _startLoading;
    widget.controller.bindStopLoadingMethod = _stopLoading;
    widget.controller.bindLoadingStateMethod = getIsLoading;
  }

  _startLoading() {
    if (_isLoading) return;
    _isLoading = true;
    setState(() {});
  }

  _stopLoading() {
    if (!_isLoading) return;
    _isLoading = false;
    setState(() {});
  }

  bool getIsLoading() => _isLoading;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
        onTap: widget.onTap,
        behavior: HitTestBehavior.opaque,
        child: Container(
          height: widget.height,
          width: widget.width,
          margin: widget.margin,
          padding: widget.padding,
          decoration: widget.decoration ??
              BoxDecoration(
                  color: CommonColor.mainColor,
                  borderRadius: BorderRadius.circular(widget.height / 2)),
          alignment: Alignment.center,
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              // CupertinoActivityIndicator(radius: 8,),
              Visibility(
                  visible: _isLoading,
                  child: Container(
                      width: widget.loadingDiam,
                      height: widget.loadingDiam,
                      margin: EdgeInsets.only(right: 8),
                      child: CircularProgressIndicator(
                          backgroundColor: CommonColor.greyBF,
                          color: CommonColor.greyF6,
                          strokeWidth: 1.5))),
              widget.textWidget ??
                  Text(
                    widget.text ?? '提交',
                    style: TextStyle(
                        color: widget.textColor,
                        fontSize: widget.textSize,
                        fontWeight: widget.textFontWeight),
                  )
            ],
          ),
        ));
  }
}

/// 带加载圈按钮的控制器
class LoadingButtonController {
  VoidCallback? bindStartLoadingMethod;
  VoidCallback? bindStopLoadingMethod;
  bool Function()? bindLoadingStateMethod;

  /// 开始加载动画
  void startLoading() => bindStartLoadingMethod?.call();

  /// 结束加载动画
  void stopLoading() => bindStopLoadingMethod?.call();

  /// 当前是否正在加载的状态
  bool get isLoading => bindLoadingStateMethod?.call() ?? false;

  void dispose() {
    bindStartLoadingMethod = null;
    bindStopLoadingMethod = null;
    bindLoadingStateMethod = null;
  }
}
