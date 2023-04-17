import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

/// @Title   公共的Scaffold
/// @Author: zhoubo
/// @CreateDate:  12/3/21 10:11 AM
class CommonScaffold extends Scaffold {
  CommonScaffold(
      {
      // SafeArea的属性
      bool safeLeft = true,
      bool safeTop = true,
      bool safeRight = true,
      bool safeBottom = true,
      bool hideSafeArea = false, // 如果传了这个属性true，则不添加safeArea组件
      // 状态栏颜色类型，dark or light，如果传了appBar，则使用appBar设置状态栏
      // 如果是没传appBar(沉浸式)，如果要设置状态栏颜色，则可以使用这个属性
      SystemUiOverlayStyle? systemUiOverlayStyle,
      // 其他自定义属性
      EdgeInsetsGeometry? bodyPadding,
      // 下方是Scaffold的属性
      Key? key,
      AppBar? appBar,
      required Widget body,
      Widget? floatingActionButton,
      FloatingActionButtonLocation? floatingActionButtonLocation,
      FloatingActionButtonAnimator? floatingActionButtonAnimator,
      List<Widget>? persistentFooterButtons,
      Widget? drawer,
      DrawerCallback? onDrawerChanged,
      Widget? endDrawer,
      DrawerCallback? onEndDrawerChanged,
      Widget? bottomNavigationBar,
      Widget? bottomSheet,
      Color? backgroundColor,
      bool? resizeToAvoidBottomInset,
      bool primary = true,
      DragStartBehavior drawerDragStartBehavior = DragStartBehavior.start,
      bool extendBody = false,
      bool extendBodyBehindAppBar = false,
      Color? drawerScrimColor,
      double? drawerEdgeDragWidth,
      bool drawerEnableOpenDragGesture = true,
      bool endDrawerEnableOpenDragGesture = true,
      String? restorationId})
      : super(
            key: key,
            appBar: appBar,
            body: _safeBody(
                safeLeft: safeLeft,
                safeTop: safeTop,
                safeRight: safeRight,
                safeBottom: safeBottom,
                hideSafeArea: hideSafeArea,
                body: body,
                bodyPadding: bodyPadding,
                haveAppBar: null != appBar,
                systemUiOverlayStyle: systemUiOverlayStyle,
                backgroundColor: backgroundColor),
            floatingActionButton: floatingActionButton,
            floatingActionButtonLocation: floatingActionButtonLocation,
            floatingActionButtonAnimator: floatingActionButtonAnimator,
            persistentFooterButtons: persistentFooterButtons,
            drawer: drawer,
            onDrawerChanged: onDrawerChanged,
            endDrawer: endDrawer,
            onEndDrawerChanged: onEndDrawerChanged,
            bottomNavigationBar: bottomNavigationBar,
            bottomSheet: bottomSheet,
            backgroundColor: backgroundColor,
            resizeToAvoidBottomInset: resizeToAvoidBottomInset,
            primary: primary,
            drawerDragStartBehavior: drawerDragStartBehavior,
            extendBody: extendBody,
            extendBodyBehindAppBar: extendBodyBehindAppBar,
            drawerScrimColor: drawerScrimColor,
            drawerEdgeDragWidth: drawerEdgeDragWidth,
            drawerEnableOpenDragGesture: drawerEnableOpenDragGesture,
            endDrawerEnableOpenDragGesture: endDrawerEnableOpenDragGesture,
            restorationId: restorationId);
}

Widget _safeBody({
  bool safeLeft = true,
  bool safeTop = true,
  bool safeRight = true,
  bool safeBottom = true,
  // 如果传了这个属性true，则不添加safeArea组件
  bool hideSafeArea = false,
  required Widget body,
  EdgeInsetsGeometry? bodyPadding,
  // 是否传了AppBar(如果传了，则使用AppBar设置状态栏，不使用SystemUiOverlayStyle)
  bool haveAppBar = false,
  SystemUiOverlayStyle? systemUiOverlayStyle,
  Color? backgroundColor,
}) {
  Widget realBody = body;
  if (null != bodyPadding) {
    realBody = Padding(padding: bodyPadding, child: realBody);
  }

  if (!hideSafeArea) {
    realBody = SafeArea(
        left: safeLeft,
        top: safeTop,
        right: safeRight,
        bottom: safeBottom,
        child: realBody);
  }

  if (haveAppBar) {
    return realBody;
  } else {
    SystemUiOverlayStyle defaultSystemUiOverlayStyle;
    if (null != systemUiOverlayStyle) {
      defaultSystemUiOverlayStyle = systemUiOverlayStyle;
    } else if (null != backgroundColor) {
      defaultSystemUiOverlayStyle = isLightColor(backgroundColor)
          ? SystemUiOverlayStyle.dark
          : SystemUiOverlayStyle.light;
    } else {
      defaultSystemUiOverlayStyle = SystemUiOverlayStyle.dark;
    }
    return AnnotatedRegion(value: defaultSystemUiOverlayStyle, child: realBody);
  }
}

/// 判断颜色是light还是dark
bool isLightColor(Color color) {
  double darkness =
      1 - (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue) / 255;
  if (darkness < 0.3) {
    return true; // It's a light color
  } else {
    return false; // It's a dark color
  }
}
