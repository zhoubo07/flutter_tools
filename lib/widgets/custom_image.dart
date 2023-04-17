import 'dart:io';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/flutter_svg.dart';

import '../utils/text_utils.dart';

/// 默认的加载中的样式（缓冲圈）
Widget defaultPlaceHolderWidget = Material(
    color: const Color(0xffF2F5F9),
    child: Center(
        child: SizedBox(
            width: 25.sp,
            height: 25.sp,
            child: CircularProgressIndicator(
                strokeWidth: 2.sp,
                color: const Color(0xffADADAD),
                backgroundColor: Colors.white))));

/// 默认的加载失败的样式
Widget defaultImageErrorWidget = Container(
    decoration: BoxDecoration(
        color: const Color(0xffF2F5F9),
        borderRadius: BorderRadius.circular(8)));

/// @Title   自定义Image【本地图片、网络图片、宽高、圆角（圆形）、描边、占位图、网络加载错误图】
/// 网络图片占位说明：默认占位样式：展示占位图（加载圈）-->失败显示白色圆角背景，
///      加载中占位图：参数调用优先级：是否展示占位图 --> 占位widget --> 占位图片path --> 默认占位widget(缓冲圈)
///      加载失败占位：参数调用优先级：是否展示占位图 --> 失败占位widget -- 默认占位widget
/// @Author: zhoubo.
/// @CreateDate:  2022年04月25日13:44:00
class CustomImage extends StatelessWidget {
  // 这三个参数必填其一
  final String? imageUrl; // 图片网络链接
  final String? imageAssetPath; // 图片asset本地路径
  final String? imageFilePath; // 图片手机file本地路径

  final ImageWidgetBuilder? imageBuilder; // 网络图片加载器
  // 宽高、适配方式
  final double? width, height;
  final BoxFit fit;

  // 圆角大小
  final double radiusAll,
      radiusTopLeft,
      radiusTopRight,
      radiusBottomLeft,
      radiusBottomRight;
  final double borderWidth; // 描边宽度
  final Color? borderColor; // 描边颜色
  final Alignment alignment; // 位置参数

  final bool showPlaceholder; // 是否展示占位图
  final String? placeholderPath; // 占位图本地路径
  final Widget? placeholderWidget; // 占位图组件（有定制占位组件的传这个参数）
  final Widget? errorWidget; // 加载失败的样式
  final String? package;

  /// Will resize the image in memory to have a certain width using [ResizeImage]
  final int? cacheWidth;

  /// Will resize the image in memory to have a certain height using [ResizeImage]
  final int? cacheHeight;

  const CustomImage({
    Key? key,
    this.width,
    this.height,
    this.fit = BoxFit.cover,
    this.radiusAll = 0,
    this.radiusTopLeft = 0,
    this.radiusTopRight = 0,
    this.radiusBottomLeft = 0,
    this.radiusBottomRight = 0,
    this.borderWidth = 0.5,
    this.borderColor,
    this.showPlaceholder = true, // 是否展示占位图
    this.placeholderPath,
    this.placeholderWidget,
    this.errorWidget,
    this.imageUrl,
    this.imageAssetPath,
    this.imageFilePath,
    this.imageBuilder,
    this.package,
    this.alignment = Alignment.center,
    this.cacheWidth,
    this.cacheHeight,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    bool hasBorder = null != borderColor;
    bool hasFile = TextUtils.isNotEmpty(imageFilePath);
    bool hasAsset = TextUtils.isNotEmpty(imageAssetPath);
    int? imageCacheWidth = cacheWidth;
    int? imageCacheHight = cacheHeight;
    // if (width != null &&
    //     height != null &&
    //     imageCacheWidth == null &&
    //     imageCacheHight == null) {
    //   // 向下取整
    //   imageCacheWidth = (width! / 0.382).truncate();
    //   imageCacheHight = (height! / 0.382).truncate();
    // }
    // 如果有描边并且宽高不为空，则使用描边叠加的方式优化图片抗锯齿
    if (null != width && null != height && hasBorder) {
      // 内侧图片的宽度的差额：
      double subInnerWidth = borderWidth < 1 ? 0 : (borderWidth - 1);
      double imageWidth = width! - subInnerWidth;
      double imageHeight = height! - subInnerWidth;
      return SizedBox(
        width: width,
        height: height,
        child: Stack(children: [
          Center(
              child: ClipRRect(
                  borderRadius: _imageBorderRadius(radiusAll - subInnerWidth),
                  // 加载判断顺序：本地图片 -> asset文件 -> 网络链接
                  child: hasFile
                      ? Image.file(File(imageFilePath!),
                          width: imageWidth,
                          height: imageHeight,
                          cacheWidth: imageCacheWidth,
                          cacheHeight: imageCacheHight,
                          fit: fit,
                          alignment: alignment)
                      : hasAsset
                          ? loadAssetImage(imageAssetPath!,
                              width: imageWidth,
                              height: imageHeight,
                              fit: fit,
                              alignment: alignment,
                              package: package)
                          : NetImage(
                              imageUrl: imageUrl,
                              imageBuilder: imageBuilder,
                              showPlaceholder: showPlaceholder,
                              placeholderPath: placeholderPath,
                              placeholderWidget: placeholderWidget,
                              errorWidget: errorWidget,
                              width: imageWidth,
                              height: imageHeight,
                              cacheWidth: imageCacheWidth,
                              cacheHeight: imageCacheHight,
                              fit: fit,
                              alignment: alignment,
                              package: package,
                            ))),
          Container(
              width: width,
              height: height,
              decoration: BoxDecoration(
                  border: hasBorder
                      ? Border.all(color: borderColor!, width: borderWidth)
                      : null,
                  borderRadius: _imageBorderRadius(radiusAll)))
        ]),
      );
    }

    return Container(
      decoration: BoxDecoration(
        border: hasBorder
            ? Border.all(color: borderColor!, width: borderWidth)
            : null,
        borderRadius: _imageBorderRadius(radiusAll),
      ),
      child: ClipRRect(
          borderRadius:
              _imageBorderRadius(radiusAll - (hasBorder ? borderWidth : 0)),
          // 加载判断顺序：本地图片 -> asset文件 -> 网络链接
          child: hasFile
              ? Image.file(File(imageFilePath!),
                  width: width, height: height, fit: fit, alignment: alignment)
              : hasAsset
                  ? loadAssetImage(imageAssetPath!,
                      width: width,
                      height: height,
                      fit: fit,
                      alignment: alignment,
                      package: package)
                  : NetImage(
                      imageUrl: imageUrl,
                      imageBuilder: imageBuilder,
                      showPlaceholder: showPlaceholder,
                      placeholderPath: placeholderPath,
                      placeholderWidget: placeholderWidget,
                      errorWidget: errorWidget,
                      width: width,
                      height: height,
                      cacheWidth: imageCacheWidth,
                      cacheHeight: imageCacheHight,
                      fit: fit,
                      alignment: alignment,
                      package: package,
                    )),
    );
  }

  BorderRadius _imageBorderRadius(double radiusAll) {
    return radiusAll > 0
        ? BorderRadius.circular(radiusAll)
        : BorderRadius.only(
            topLeft: Radius.circular(radiusTopLeft),
            topRight: Radius.circular(radiusTopRight),
            bottomLeft: Radius.circular(radiusBottomLeft),
            bottomRight: Radius.circular(radiusBottomRight),
          );
  }
}

/// @Title  包装的CachedNetworkImage图片加载组件
///   默认占位样式：展示占位图（加载圈）-->失败显示白色圆角背景，
///      加载中占位图：参数调用优先级：是否展示占位图 --> 占位widget --> 占位图片path --> 默认占位widget(缓冲圈)
///      加载失败占位：参数调用优先级：是否展示占位图 --> 失败占位widget -- 默认占位widget
/// @Author: zhoubo
/// @CreateDate: 2022年04月25日13:30:13
class NetImage extends StatelessWidget {
  final String? imageUrl; // 网络图片链接
  final ImageWidgetBuilder? imageBuilder; // 图片加载器
  final bool showPlaceholder; // 是否展示占位图
  final String? placeholderPath; // 占位图本地路径
  final String? errorPath; // 失败占位图本地路径
  final Widget? placeholderWidget; // 占位图组件（有定制占位组件的传这个参数）
  final Widget? errorWidget; // 加载失败的样式
  final double? width; // 图片约束宽度
  final double? height; // 图片约束高度
  final BoxFit? fit; // 加载方式
  final Alignment alignment; // 位置参数
  final String? package;

  /// Will resize the image in memory to have a certain width using [ResizeImage]
  final int? cacheWidth;

  /// Will resize the image in memory to have a certain height using [ResizeImage]
  final int? cacheHeight;

  /// 图片加载构造函数，提取常用的参数
  ///   .去掉加载错误的参数，加载错误和加载中共用占位
  ///   .如果传了placeholderPath参数，使用默认占位格式，使用此本地路径作为占位图
  ///   .如果传了placeholderWidget参数，则使用此作为占位，不使用默认占位
  const NetImage({
    Key? key,
    this.imageUrl, // 网络图片链接
    this.imageBuilder,
    this.showPlaceholder = true, // 是否展示占位图
    this.placeholderPath, // 占位图本地路径
    this.placeholderWidget, // 占位图组件（有定制占位组件的传这个参数）
    this.errorPath, // 加载失败的本地路径占位
    this.errorWidget, // 加载失败的样式
    this.width, // 图片约束宽度
    this.height, // 图片约束高度
    this.fit = BoxFit.cover, // 加载方式
    this.alignment = Alignment.center, // 位置参数
    this.package,
    this.cacheWidth,
    this.cacheHeight,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // 没有网络图，判断加载占位图
    if (TextUtils.isEmpty(imageUrl)) {
      if (showPlaceholder) {
        return _loadDefaultErrorPlaceHolder(context);
      } else {
        return SizedBox(width: width, height: height);
      }
    }

    return CachedNetworkImage(
        imageUrl: imageUrl ?? '',
        imageBuilder: imageBuilder,
        // 加载中占位图：参数使用优先级：是否展示占位图 --> 占位widget --> 占位图片path --> 默认占位widget(缓冲圈)
        placeholder: showPlaceholder
            ? (context, url) => _loadDefaultPlaceHolder(context)
            : null,
        // 加载失败占位：参数使用优先级：是否展示占位图 --> 失败占位widget -- 占位图片path --> 默认失败占位widget
        errorWidget: showPlaceholder
            ? (context, url, error) => _loadDefaultErrorPlaceHolder(context)
            : null,
        width: width,
        height: height,
        memCacheWidth: cacheWidth,
        memCacheHeight: cacheHeight,
        fit: fit,
        alignment: alignment);
  }

  Widget _loadDefaultPlaceHolder(BuildContext context) {
    return placeholderWidget ??
        (TextUtils.isNotEmpty(placeholderPath)
            ? _loadPlaceholderByPath(context, placeholderPath!, width, height)
            : defaultPlaceHolderWidget);
  }

  Widget _loadDefaultErrorPlaceHolder(BuildContext context) {
    if (null != errorWidget) {
      return errorWidget!;
    } else if (TextUtils.isNotEmpty(errorPath)) {
      return _loadPlaceholderByPath(context, errorPath!, width, height);
    }
    // else if (null != placeholderWidget) {
    //   return placeholderWidget!;
    // }
    else if (TextUtils.isNotEmpty(placeholderPath)) {
      return _loadPlaceholderByPath(context, placeholderPath!, width, height);
    } else {
      return defaultImageErrorWidget;
    }
  }

  /// 通过路径的加载占位图
  Widget _loadPlaceholderByPath(BuildContext context, String placeholderPath,
      double? width, double? height) {
    if (width != null && height != null) {
      return loadAssetImage(placeholderPath,
          width: width, height: height, fit: BoxFit.contain, package: package);
    } else if (width != null) {
      return loadAssetImage(placeholderPath,
          width: width, height: height, fit: BoxFit.fitWidth, package: package);
    } else if (height != null) {
      return loadAssetImage(placeholderPath,
          width: width,
          height: height,
          fit: BoxFit.fitHeight,
          package: package);
    } else {
      return loadAssetImage(placeholderPath,
          width: width, height: height, fit: null, package: package);
    }
  }
}

Widget loadAssetImage(String assetImagePath,
    {double? width,
    double? height,
    BoxFit? fit,
    Color? color,
    String? package,
    AlignmentGeometry alignment = Alignment.center}) {
  if (assetImagePath.endsWith('svg')) {
    return SvgPicture.asset(assetImagePath,
        width: width,
        height: height,
        package: package,
        fit: fit ?? BoxFit.contain,
        color: color,
        alignment: alignment);
  } else {
    return Image.asset(assetImagePath,
        width: width,
        height: height,
        package: package,
        fit: fit ?? BoxFit.contain,
        color: color,
        alignment: alignment);
  }
}
