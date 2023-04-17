import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

RouteObserver<ModalRoute> lifecycleRouteObserver = RouteObserver<ModalRoute>();

/// @Title   onResume/onPause的生命周期的State
/// @Author: zhoubo
/// @CreateDate:  2022年08月16日10:45:26
abstract class ConsumerLifecycleState<T extends ConsumerStatefulWidget>
    extends ConsumerState<T> with WidgetsBindingObserver, RouteAware {
  /// 是否监听App的lifecycle（进入后台/从后台返回）
  bool get observerAppLifecycle => false;

  /// 是否监听页面的push和pop的lifecycle（跳转下个页面onPause，从下个页面返回onResume）
  bool get observerPageLifecycle => true;

  @override
  void initState() {
    super.initState();
    if (observerAppLifecycle) WidgetsBinding.instance.addObserver(this);
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    if (observerPageLifecycle) {
      ModalRoute? route = ModalRoute.of(context);
      if (null != route) {
        lifecycleRouteObserver.subscribe(this, route);
      }
    }
  }

  @override
  void dispose() {
    if (observerAppLifecycle) WidgetsBinding.instance.removeObserver(this);
    if (observerPageLifecycle) {
      lifecycleRouteObserver.unsubscribe(this);
    }
    super.dispose();
  }

  @override
  void didPush() {
    super.didPush();
    // 当前页面被打开
    if (observerPageLifecycle) onResume(true);
  }

  @override
  void didPushNext() {
    super.didPushNext();
    // 当前页面打开下一个页面
    if (observerPageLifecycle) onPause();
  }

  @override
  void didPopNext() {
    super.didPopNext();
    // 当前页面打开的下一个页面被关闭
    if (observerPageLifecycle) onResume(false);
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);
    switch (state) {
      case AppLifecycleState.resumed:
        // 亮屏/从手机后台返回，（目前没有判断是否在栈顶，先不调用onResume）
        if (observerAppLifecycle) onResume(false);
        break;
      case AppLifecycleState.paused:
        if (observerAppLifecycle) onPause();
        break;
      default:
        break;
    }
  }

  void onResume(bool isFirstResume) {}

  void onPause() {}
}

/// @Title   onResume/onPause的生命周期的State
/// @Author: zhoubo
/// @CreateDate:  2022年08月16日10:45:26
abstract class LifecycleState<T extends StatefulWidget> extends State<T>
    with WidgetsBindingObserver, RouteAware {
  /// 是否监听App的lifecycle（进入后台/从后台返回）
  bool get observerAppLifecycle => false;

  /// 是否监听页面的push和pop的lifecycle（跳转下个页面onPause，从下个页面返回onResume）
  bool get observerPageLifecycle => true;

  @override
  void initState() {
    super.initState();
    if (observerAppLifecycle) WidgetsBinding.instance.addObserver(this);
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    if (observerPageLifecycle) {
      ModalRoute? route = ModalRoute.of(context);
      if (null != route) {
        lifecycleRouteObserver.subscribe(this, route);
      }
    }
  }

  @override
  void dispose() {
    if (observerAppLifecycle) WidgetsBinding.instance.removeObserver(this);
    if (observerPageLifecycle) {
      lifecycleRouteObserver.unsubscribe(this);
    }
    super.dispose();
  }

  @override
  void didPush() {
    super.didPush();
    // 当前页面被打开
    if (observerPageLifecycle) onResume(true);
  }

  @override
  void didPushNext() {
    super.didPushNext();
    // 当前页面打开下一个页面
    if (observerPageLifecycle) onPause();
  }

  @override
  void didPopNext() {
    super.didPopNext();
    // 当前页面打开的下一个页面被关闭
    if (observerPageLifecycle) onResume(false);
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);
    switch (state) {
      case AppLifecycleState.resumed:
        // 亮屏/从手机后台返回，（目前没有判断是否在栈顶，先不调用onResume）
        if (observerAppLifecycle) onResume(false);
        break;
      case AppLifecycleState.paused:
        if (observerAppLifecycle) onPause();
        break;
      default:
        break;
    }
  }

  void onResume(bool isFirstResume) {}

  void onPause() {}
}
