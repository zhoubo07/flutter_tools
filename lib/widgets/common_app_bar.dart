import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../r.g.dart';
import '../utils/text_utils.dart';
import 'common_scaffold.dart';
import 'custom_image.dart';

/// @Title   通用AppBar
/// 目前默认是红色主题+白色标题色，如果要修改为白色主题，只传背景色backgroundColor白色就可以；
/// 标题色会根据背景色判断使用深浅主题色。如果使用其他颜色，再单独设置不同的颜色属性。
/// @Author: zhoubo.
/// @CreateDate:  2023年01月08日09:47:18
class CommonAppBar extends AppBar {
  // 深色toolbar主题（背景是mainColor，标题是白色）
  static const Color mTitleColorDark = Colors.white;
  static const Color mBackgroundColorDark = Color(0xffA880FF);

  // 浅色toolbar主题（背景是白色，标题是黑色）
  static const Color mTitleColorLight = Color(0xff141414);
  static const Color mBackgroundColorLight = Colors.white;

  CommonAppBar(
    BuildContext context, {
    Key? key,
    // 左侧按钮
    Widget? leadingIcon,
    String? leadingIconPath,
    Color? leadingIconColor, // 左侧按钮颜色，如果不传则根据背景色判断设置深浅主题色
    double leadingIconSize = 15.0,
    bool hideLeading = false,
    VoidCallback? leadingOnPressed,
    automaticallyImplyLeading = true, // 与leading部件有关，当leading为null时是否隐含前导小部件
    // 标题属性
    Widget? titleWidget,
    String? title,
    Color? titleColor, // 标题颜色，如果不传则根据背景色判断设置深浅主题色
    double titleSize = 17.0,
    FontWeight? titleFontWeight,
    // 右侧按钮
    Widget? action,
    String? actionStr,
    TextStyle? actionTextStyle, // 右侧text按钮样式,传了这个下面的color不用
    Color? actionColor, // 右侧按钮颜色，如果不传则根据背景色判断设置深浅主题色
    double? actionFontSize, // 右侧按钮文字大小，
    GestureTapCallback? actionOnPressed,
    List<Widget>? actions,
    // 其他属性
    Widget? flexibleSpace, // 此小部件堆叠在工具栏和选项卡栏的后面。它的高度将与应用栏的整体高度相同
    PreferredSizeWidget? bottom, // 位于AppBar底部的子组件
    double elevation = 0, // 阴影
    Color? shadowColor, // 应用栏阴影颜色
    ShapeBorder? shape, // 组件的形状
    Color backgroundColor = mBackgroundColorLight,
    Brightness? brightness, // 待废弃 --状态栏图标颜色(目前添加了逻辑判断深色和浅色)
    SystemUiOverlayStyle? systemUiOverlayStyle, // 状态栏图标颜色(目前添加了逻辑判断深色和浅色)
    IconThemeData? iconTheme, // 应用栏图标大小，颜色，阴影控制
    IconThemeData? actionsIconTheme, // title右侧小部件图标颜色、大小、阴影控制
    bool primary = true, // 应用栏是否显示在状态栏的底部
    bool centerTitle = true, // 标题title是否应该居中
    bool excludeHeaderSemantics = false,
    double titleSpacing =
        NavigationToolbar.kMiddleSpacing, // 水平轴上[title]内容周围的间距
    double toolbarOpacity = 1.0, // 应用栏部件的透明度
    double bottomOpacity = 1.0, // 应用栏底部bottom的透明度
    double? toolbarHeight,
    double? leadingWidth,
  }) : super(
          key: key,
          leading: _leading(
              context,
              leadingIcon,
              leadingIconPath,
              leadingIconColor ?? _titleColor(titleColor, backgroundColor),
              leadingIconSize,
              hideLeading,
              leadingOnPressed),
          automaticallyImplyLeading: automaticallyImplyLeading,
          title: _title(
              titleWidget,
              title,
              _titleColor(titleColor, backgroundColor),
              titleSize,
              titleFontWeight),
          actions: _actions(
              action,
              actionStr,
              actionTextStyle,
              actionColor ?? const Color(0xffA880FF),
              actionFontSize,
              actionOnPressed,
              actions),
          flexibleSpace: flexibleSpace,
          bottom: bottom,
          elevation: elevation,
          shadowColor: shadowColor,
          shape: shape,
          backgroundColor: backgroundColor,
          systemOverlayStyle: systemUiOverlayStyle ??
              (isLightColor(backgroundColor)
                  ? SystemUiOverlayStyle.dark
                  : SystemUiOverlayStyle.light),
          // ignore: deprecated_member_use
          brightness: brightness ??
              (isLightColor(backgroundColor)
                  ? Brightness.light
                  : Brightness.dark),
          iconTheme: iconTheme,
          actionsIconTheme: actionsIconTheme,
          primary: primary,
          centerTitle: centerTitle,
          excludeHeaderSemantics: excludeHeaderSemantics,
          titleSpacing: titleSpacing,
          toolbarOpacity: toolbarOpacity,
          bottomOpacity: bottomOpacity,
          leadingWidth: leadingWidth,
          toolbarHeight: toolbarHeight,
        );
}

/// 获取标题颜色（如果传了标题色，则使用；如果没传，则根据背景色设置标题色深浅）
Color _titleColor(Color? titleColor, Color backgroundColor) {
  if (null != titleColor) return titleColor;
  bool isLightBgColor = isLightColor(backgroundColor);
  return isLightBgColor
      ? CommonAppBar.mTitleColorLight
      : CommonAppBar.mTitleColorDark;
}

/// 左侧返回按钮
Widget? _leading(
    BuildContext context,
    Widget? leadingIcon,
    String? leadingIconPath,
    Color leadingIconColor,
    double leadingIconSize,
    bool hideLeading,
    VoidCallback? leadingOnPressed) {
  return hideLeading
      ? Container()
      : IconButton(
          icon: leadingIcon ??
              loadAssetImage(
                  leadingIconPath ?? R.image.asset.ic_back_black.assetName,
                  width: leadingIconSize,
                  height: leadingIconSize,
                  color: leadingIconColor),
          onPressed: leadingOnPressed ?? () => Navigator.maybePop(context));
}

/// 标题
Widget? _title(Widget? titleWidget, String? title, Color titleColor,
    double titleSize, FontWeight? titleFontWeight) {
  return titleWidget ??
      (title == null
          ? null
          : Text(title,
              style: TextStyle(
                  color: titleColor,
                  fontSize: titleSize,
                  fontWeight: titleFontWeight)));
}

/// 右侧按钮
List<Widget> _actions(
    Widget? action,
    String? actionStr,
    TextStyle? actionTextStyle,
    Color? actionColor,
    double? actionFontSize,
    GestureTapCallback? actionOnPressed,
    List<Widget>? actions) {
  return actions ??
      ((action == null && TextUtils.isEmpty(actionStr))
          ? []
          : [
              GestureDetector(
                  onTap: actionOnPressed,
                  behavior: HitTestBehavior.opaque,
                  child: Center(
                      child: action ??
                          Padding(
                              padding: const EdgeInsets.fromLTRB(16, 5, 16, 5),
                              child: Text(actionStr ?? '',
                                  style: actionTextStyle ??
                                      TextStyle(
                                          color: actionColor,
                                          fontWeight: FontWeight.w500,
                                          fontSize: actionFontSize ?? 16)))))
            ]);
}
