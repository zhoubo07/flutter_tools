import 'package:flutter/material.dart';

/// @title: 自定义宽度的tab指示器（复制源码UnderlineTabIndicator，修改_indicatorRectFor和paint方法）
/// @creator: zhoubo
/// @create time: 2022年11月16日17:10:07
class CustomWidthTabIndicator extends Decoration {
  /// Create an underline style selected tab indicator.
  ///
  /// The [borderSide] and [insets] arguments must not be null.
  const CustomWidthTabIndicator({
    this.width = 20,
    this.strokeCap = StrokeCap.round,
    this.borderSide = const BorderSide(width: 2.0, color: Colors.white),
    this.insets = EdgeInsets.zero,
  });

  /// The color and weight of the horizontal line drawn below the selected tab.
  final BorderSide borderSide;

  ///新增的属性
  final double width; // 控制器宽度
  final StrokeCap strokeCap; // 控制器边角形状

  /// Locates the selected tab's underline relative to the tab's boundary.
  ///
  /// The [TabBar.indicatorSize] property can be used to define the tab
  /// indicator's bounds in terms of its (centered) tab widget with
  /// [TabBarIndicatorSize.label], or the entire tab with
  /// [TabBarIndicatorSize.tab].
  final EdgeInsetsGeometry insets;

  @override
  Decoration? lerpFrom(Decoration? a, double t) {
    if (a is UnderlineTabIndicator) {
      return UnderlineTabIndicator(
        borderSide: BorderSide.lerp(a.borderSide, borderSide, t),
        insets: EdgeInsetsGeometry.lerp(a.insets, insets, t)!,
      );
    }
    return super.lerpFrom(a, t);
  }

  @override
  Decoration? lerpTo(Decoration? b, double t) {
    if (b is UnderlineTabIndicator) {
      return UnderlineTabIndicator(
        borderSide: BorderSide.lerp(borderSide, b.borderSide, t),
        insets: EdgeInsetsGeometry.lerp(insets, b.insets, t)!,
      );
    }
    return super.lerpTo(b, t);
  }

  @override
  BoxPainter createBoxPainter([VoidCallback? onChanged]) {
    return _CustomWidthPainter(this, onChanged);
  }

  Rect _indicatorRectFor(Rect rect, TextDirection textDirection) {
    final Rect indicator = insets.resolve(textDirection).deflateRect(rect);
    // return Rect.fromLTWH(
    //   indicator.left,
    //   indicator.bottom - borderSide.width,
    //   indicator.width,
    //   borderSide.width,
    // );
    // 希望的宽度
    double wantWidth = width;
    // 取中间坐标
    double cw = (indicator.left + indicator.right) / 2;
    //下划线靠左
    // return Rect.fromLTWH(indicator.left,
    //     indicator.bottom - borderSide.width, wantWidth, borderSide.width);
    //下划线居中
    return Rect.fromLTWH(cw - wantWidth / 2,
        indicator.bottom - borderSide.width, wantWidth, borderSide.width);
  }

  @override
  Path getClipPath(Rect rect, TextDirection textDirection) {
    return Path()..addRect(_indicatorRectFor(rect, textDirection));
  }
}

class _CustomWidthPainter extends BoxPainter {
  _CustomWidthPainter(this.decoration, VoidCallback? onChanged)
      : super(onChanged);

  final CustomWidthTabIndicator decoration;

  ///决定控制器边角形状的方法
  @override
  void paint(Canvas canvas, Offset offset, ImageConfiguration configuration) {
    assert(configuration.size != null);
    final Rect rect = offset & configuration.size!;
    final TextDirection textDirection = configuration.textDirection!;
    final Rect indicator = decoration
        ._indicatorRectFor(rect, textDirection)
        .deflate(decoration.borderSide.width / 2.0);
    // final Paint paint = decoration.borderSide.toPaint()..strokeCap = StrokeCap.square;
    final Paint paint = decoration.borderSide.toPaint()
      ..strokeCap = decoration.strokeCap; //这块更改为想要的形状
    canvas.drawLine(indicator.bottomLeft, indicator.bottomRight, paint);
  }
}
