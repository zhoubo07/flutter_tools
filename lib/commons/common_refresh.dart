import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/physics.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

/// @Title   公共的刷新加载样式
/// @Author: zhoubo
/// @CreateDate:  5/25/21 1:49 PM
class CommonRefresh {
  static RefreshConfiguration defaultRefreshConfig(Widget child) {
    // 全局配置子树下的SmartRefresher,下面列举几个特别重要的属性
    return RefreshConfiguration(
        // 配置默认头部指示器,假如你每个页面的头部指示器都一样的话,你需要设置这个,不设置头默认使用系统的
        headerBuilder: () =>
            ClassicHeader(refreshingIcon: CupertinoActivityIndicator()),
        // 配置默认底部指示器
        footerBuilder: () => defaultFooter(),
        // 头部触发刷新的越界距离
        headerTriggerDistance: 70.0,
        // 自定义回弹动画,三个属性值意义请查询flutter api
        springDescription:
            SpringDescription(stiffness: 170, damping: 16, mass: 1.9),
        //头部最大可以拖动的范围,如果发生冲出视图范围区域,请设置这个属性
        maxOverScrollExtent: 100,
        // 底部最大可以拖动的范围
        maxUnderScrollExtent: 0,
        //这个属性不兼容PageView和TabBarView,如果你特别需要TabBarView左右滑动,你需要把它设置为true
        enableScrollWhenRefreshCompleted: true,
        //在加载失败的状态下,用户仍然可以通过手势上拉来触发加载更多
        enableLoadingWhenFailed: true,
        // Viewport不满一屏时,禁用上拉加载更多功能
        hideFooterWhenNotFull: true,
        // footer状态组件是否跟随内容布局，默认是noData状态跟随
        shouldFooterFollowWhenNotFull: (status) => false,
        // 可以通过惯性滑动触发加载更多
        enableBallisticLoad: true,
        child: child);
  }

  static Widget defaultFooter() {
    return CustomFooter(
      builder: (BuildContext context, LoadStatus? mode) {
        Widget? body;
        if (mode == LoadStatus.idle) {
          body = Text("", // 之前是上拉加载
              style: TextStyle(color: Colors.black45, fontSize: 13));
        } else if (mode == LoadStatus.loading) {
          body = CupertinoActivityIndicator();
        } else if (mode == LoadStatus.noMore) {
          body = Text(
            "没有更多数据了!",
            style: TextStyle(color: Colors.black45, fontSize: 13),
          );
        } else if (mode == LoadStatus.failed) {
          // body = Text("Load Failed!Click retry!");
        } else if (mode == LoadStatus.canLoading) {
          body = Text("松手开始加载数据",
              style: TextStyle(color: Colors.black45, fontSize: 13));
        }

        return Visibility(
            visible: body != null,
            child: Container(
              height: 50.0,
              child: Center(child: body),
            ));
      },
    );
  }
}
