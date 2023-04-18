import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/svg.dart';

import '../../../utils/text_utils.dart';
import '../r.g.dart';

/// @Title   复制公共Linkr输入框修改后的输入框（待移入公共库）
///   修改点：1：移除height默认高度：公共库中默认44；
///         2：添加textController；
///         3：添加错误文案控制器：公共库中是String error文案控制，改为ValueNotifier
///         4：添加minLines最小行数
///         5：右侧复制/删除按钮的显示判断增加enable判断
/// @Author: zhoubo
/// @CreateDate:  2023年03月10日17:12:38
// ignore: must_be_immutable
class CustomTextField extends StatefulWidget {
  CustomTextField(
      {this.text = "",
      this.textController, // --修改点：添加
      this.inputFormatter,
      this.hintText = '',
      this.enabled = true,
      this.radius = 10,
      this.onFocus,
      this.maxLength = -1,
      this.autoFocus = false,
      this.errorController, // --修改点：添加参数
      this.maxLines = 1,
      this.minLines,
      this.onChange,
      this.autocorrect = true,
      this.onLeadingClick,
      this.keyBoardType = TextInputType.text,
      this.sectionTitle,
      this.smartDashesType = SmartDashesType.disabled,
      this.smartQuotesType = SmartQuotesType.disabled,
      this.showClearBtnWhenNotEmpty = true,
      this.showPasteBtnWhenEmpty = false,
      this.onlyShowPasteOrClearActionWhenFocus = true,
      this.height, // --修改点
      this.isCollapsed = true,
      this.leadingWidget,
      this.trailingWidget,
      this.onTrailingClick,
      Key? key})
      : super(key: key);

  ///输入文本
  final String text;

  /// 控制器 // --修改点：添加参数
  TextEditingController? textController;

  /// 输入错误样式的控制器 // --修改点：添加参数
  // ValueNotifier<String>? errorController;
  TextEditingController? errorController;

  ///输入格式统计
  final List<TextInputFormatter>? inputFormatter;

  ///输入键盘类型
  final TextInputType? keyBoardType;

  ///提示文本
  final String hintText;

  ///是否启用
  final bool enabled;

  ///最大长度
  final int maxLength;

  ///焦点变化
  final ValueChanged<bool>? onFocus;

  ///自动获取焦点
  final bool autoFocus;

  ///弧度
  final double radius;

  ///输入回调
  final ValueChanged<String>? onChange;

  ///最大行数
  final int maxLines;

  ///最小行数 // --修改点：添加参数
  final int? minLines;

  ///是否展示清除按钮(内容不为空时)  默认true
  final bool showClearBtnWhenNotEmpty;

  ///是否展示粘贴按钮(内容为空时)  默认false
  final bool showPasteBtnWhenEmpty;

  ///自动校正
  final bool autocorrect;

  ///粘贴/清除按钮是否在输入状态才显示，默认true
  final bool onlyShowPasteOrClearActionWhenFocus;

  ///左侧icon点击事件
  Function? onLeadingClick;

  /// 左侧组件，设置后leadingIcon失效
  Widget? leadingWidget;

  /// 右侧组件, 设置后actions、actionText失效
  Widget? trailingWidget;

  ///右侧自定义组件点击事件
  Function? onTrailingClick;

  ///输入框上标题
  String? sectionTitle;

  /// 智能标点, 默认Disable
  SmartDashesType? smartDashesType;

  /// 智能标点, 默认Disable
  SmartQuotesType? smartQuotesType;

  ///输入框高度，不包括标题，错误文案
  final double? height;

  /// 高度包裹 默认为true
  bool isCollapsed;

  @override
  State<CustomTextField> createState() => _CustomTextFieldState();
}

class _CustomTextFieldState extends State<CustomTextField> {
  late TextEditingController _textController; // --修改点：
  final FocusNode _focusNode = FocusNode();
  bool _hasFocus = false;

  @override
  void initState() {
    super.initState();
    // --修改点：_textController
    _textController = widget.textController ?? TextEditingController();
    if (TextUtils.isNotEmpty(widget.text)) {
      _textController.text = widget.text;
    }
    _focusNode.addListener(() {
      setState(() {
        _hasFocus = _focusNode.hasFocus;
      });
      widget.onFocus?.call(_focusNode.hasFocus);
    });
    if (widget.autoFocus) _focusNode.requestFocus();

    // --修改点：添加错误文案控制
    widget.errorController?.addListener(() {
      setState(() {});
    });
  }

  @override
  void dispose() {
    super.dispose();
    _textController.dispose();
    widget.errorController?.dispose();
    _focusNode.removeListener(() {});
    _focusNode.dispose();
  }

  @override
  Widget build(BuildContext context) {
    bool isError = TextUtils.isNotEmpty(widget.errorController?.text);
    return Column(
      children: [
        // 输入框上方标题
        _sectionTitleView(),
        // 输入框
        Container(
            height: widget.height,
            padding: EdgeInsets.zero,
            decoration: BoxDecoration(
                color: widget.enabled ? Colors.white : const Color(0xFFF4F6F9),
                borderRadius: BorderRadius.circular(widget.radius),
                border: Border.all(
                    width: 1,
                    color: isError
                        ? const Color(0xFFDC352C)
                        // todo 默认改为主题色
                        : (_hasFocus ? Color(0xFF8757FF) : Color(0xFFDCE3EE)))),
            child:
                Row(crossAxisAlignment: CrossAxisAlignment.center, children: [
              // 输入框前方leading组件
              _leadingWidget(),
              // 实际输入框
              Expanded(child: _textFieldBody()),
              // 右侧的清除按钮
              _rightClearWidget(),
              // 右侧的粘贴按钮
              _rightPasteWidget(),
              // 右侧自定义按钮
              _trailingWidget(),
            ])),
        // 底部的错误信息
        _bottomErrorText(isError)
      ],
    );
  }

  /// 输入框上方标题
  Widget _sectionTitleView() {
    return Visibility(
        visible: (widget.sectionTitle ?? "").isNotEmpty,
        child: Align(
            alignment: Alignment.centerLeft,
            child: Padding(
                padding: const EdgeInsets.fromLTRB(2, 0, 0, 8),
                child: Text(widget.sectionTitle ?? "",
                    style: TextStyle(
                        fontSize: 14,
                        fontWeight: FontWeight.w500,
                        color: Colors.black87)))));
  }

  /// 输入框前方leading组件
  Widget _leadingWidget() {
    return Visibility(
        visible: widget.leadingWidget != null,
        child: GestureDetector(
            onTap: () => widget.onLeadingClick?.call(),
            child: Padding(
                padding: const EdgeInsets.only(left: 12),
                child: widget.leadingWidget)));
  }

  /// 输入框后方自定义组件
  Widget _trailingWidget() {
    return Visibility(
        visible: widget.trailingWidget != null,
        child: GestureDetector(
          onTap: () => widget.onTrailingClick?.call(),
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            child: widget.trailingWidget,
          ),
        ));
  }

  /// 右侧的清除按钮(只有在内容不为空时才显示)
  Widget _rightClearWidget() {
    bool visible = widget.maxLines == 1 &&
        widget.showClearBtnWhenNotEmpty &&
        _textController.text.isNotEmpty &&
        widget.enabled;
    if (widget.onlyShowPasteOrClearActionWhenFocus && !_hasFocus) {
      visible = false;
    }

    return Visibility(
        visible: visible,
        child: GestureDetector(
            onTap: () {
              setState(() {
                _textController.clear();
              });
              if (widget.onChange != null) {
                widget.onChange!(_textController.text);
              }
            },
            child: Padding(
                padding:
                    const EdgeInsets.symmetric(horizontal: 12, vertical: 5),
                child:
                    SvgPicture.asset(R.svg.asset.icon_links_clear.assetName))));
  }

  /// 右侧的粘贴按钮（只有在输入框为空的时候才展示）
  Widget _rightPasteWidget() {
    bool visible = widget.maxLines == 1 &&
        widget.showPasteBtnWhenEmpty &&
        _textController.text.isEmpty &&
        widget.enabled;
    if (widget.onlyShowPasteOrClearActionWhenFocus && !_hasFocus) {
      visible = false;
    }

    return Visibility(
      visible: visible,
      child: GestureDetector(
          onTap: () async {
            final ClipboardData? data =
                await Clipboard.getData(Clipboard.kTextPlain);
            debugPrint('Clipboard data: ${data?.text}');
            if (TextUtils.isNotEmpty(data?.text)) {
              setState(() {
                _textController.text = data!.text ?? "";
              });
              if (widget.onChange != null) {
                widget.onChange!(_textController.text);
              }
            }
          },
          child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
              child: SvgPicture.asset(
                  R.svg.asset.icon_input_paste_normal.assetName,
                  width: 24,
                  height: 24))),
    );
  }

  /// 底部的错误信息
  Widget _bottomErrorText(bool isError) {
    return Visibility(
        visible: isError,
        child: Align(
            alignment: Alignment.centerLeft,
            child: Padding(
                padding: const EdgeInsets.all(12),
                child: Text(widget.errorController?.text ?? '',
                    style: const TextStyle(
                      fontSize: 14,
                      color: Color(0xFFDC352C),
                      fontWeight: FontWeight.w400,
                    ),
                    textAlign: TextAlign.left))));
  }

  /// 实际输入框
  _textFieldBody() {
    return RepaintBoundary(
        child: TextField(
            style: TextStyle(
                fontSize: 16,
                color:
                    widget.enabled ? Colors.black87 : const Color(0xFFADADAD)),
            inputFormatters: widget.inputFormatter,
            keyboardType: widget.keyBoardType,
            enabled: widget.enabled,
            autocorrect: widget.autocorrect,
            focusNode: _focusNode,
            onChanged: (value) => _onChangeAction(value),
            controller: _textController,
            maxLines: widget.maxLines,
            minLines: widget.minLines,
            maxLength: widget.maxLength > 0 ? widget.maxLength : null,
            // 智能标点
            smartQuotesType: widget.smartQuotesType,
            decoration: InputDecoration(
                counterText: '',
                isCollapsed: widget.isCollapsed,
                contentPadding:
                    const EdgeInsets.only(left: 10, top: 10, bottom: 10),
                hintText: widget.hintText,
                hintStyle: const TextStyle(
                    color: Color(0xFFADADAD),
                    fontSize: 16,
                    height: 1,
                    //用于提示文字对齐
                    textBaseline: TextBaseline.alphabetic),
                border: InputBorder.none)));
  }

  // 输入内容改变
  _onChangeAction(value) {
    widget.errorController?.text = '';
    if (widget.onChange != null) {
      widget.onChange!(value);
    }
  }
}
