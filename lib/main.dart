import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_tools/utils/loading_utils.dart';
import 'package:flutter_tools/widgets/consumer_lifecycle_state.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import 'commons/common_colors.dart';
import 'commons/common_refresh.dart';
import 'commons/global_config.dart';

void main() {
  runApp(ProviderScope(child: const MyApp()));
  if (Platform.isAndroid) {
    // 设置android沉浸透明状态栏。
    // 以下两行 设置android状态栏为透明的沉浸。写在组件渲染之后，是为了在渲染后进行set赋值，覆盖状态栏，写在渲染之前MaterialApp组件会覆盖掉这个值。
    SystemUiOverlayStyle systemUiOverlayStyle =
        const SystemUiOverlayStyle(statusBarColor: Colors.transparent);
    SystemChrome.setSystemUIOverlayStyle(systemUiOverlayStyle);
  }
  //强制竖屏
  SystemChrome.setPreferredOrientations(
      [DeviceOrientation.portraitUp, DeviceOrientation.portraitDown]);
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return ScreenUtilInit(
      designSize: const Size(375, 812),
      minTextAdapt: true,
      splitScreenMode: true,
      builder: (ctx, child) => CommonRefresh.defaultRefreshConfig(MaterialApp(
          navigatorKey: navigatorKey,
          title: '工具项目',
          theme: ThemeData(
              // 定义一个单一的颜色以及十个色度的色块
              primarySwatch: Colors.blue,
              // 应用程序主要部分的背景颜色(toolbars、tab bars 等)
              primaryColor: CommonColor.mainColor,
              // Scaffold的默认颜色。典型Material应用程序或应用程序内页面的背景颜色
              scaffoldBackgroundColor: CommonColor.themeBgColor,
              visualDensity: VisualDensity.adaptivePlatformDensity,
              textSelectionTheme:
                  TextSelectionThemeData(cursorColor: CommonColor.mainColor)),
          locale: Locale('zh', 'CH'),
          localizationsDelegates: [
            // ... 自定义应用支持本地化
            GlobalCupertinoLocalizations.delegate,
            GlobalMaterialLocalizations.delegate,
            GlobalWidgetsLocalizations.delegate,
            RefreshLocalizations.delegate // 刷新框架的多语言
          ],
          supportedLocales: [
            const Locale('zh', 'CH'),
            const Locale('en', 'US')
          ],
          navigatorObservers: [lifecycleRouteObserver, LoadingUtils.observer],
          home: MyHomePage(title: ''),
          builder: LoadingUtils.init(
              builder: (context, child) => MediaQuery(
                  //设置文字大小不随系统设置改变
                  data: MediaQuery.of(context).copyWith(textScaleFactor: 1.0),
                  child: GestureDetector(
                      // 设置点击取消键盘焦点
                      onTap: () {
                        FocusScopeNode currentFocus = FocusScope.of(context);
                        if (!currentFocus.hasPrimaryFocus &&
                            currentFocus.focusedChild != null) {
                          FocusManager.instance.primaryFocus?.unfocus();
                        }
                      },
                      child: child))))),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Text(
              'You have pushed the button this many times:',
            ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headline4,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
