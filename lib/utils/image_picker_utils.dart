import 'dart:io';

import 'package:flutter/widgets.dart';
import 'package:image_cropper/image_cropper.dart';
import 'package:image_picker/image_picker.dart';

import 'permission_util.dart';

/// @Title   选择图片/照相的工具类
/// @Author: zhoubo
/// @CreateDate:  2022/4/22 3:00 下午
class ImagePickerUtils {
  /// 跳转到相机拍照（返回拍照完的XFile）
  static Future<XFile?> jumpToCameraByFile(BuildContext context,
      {double? maxWidth,
      double? maxHeight,
      String? titleWhenDenied,
      String? messageWhenDenied,
      String? okLabelWhenDenied}) async {
    // 检查拍照权限
    bool allowPermission = await PermissionUtil.requestCameraPermission(
        context: context,
        titleWhenDenied: titleWhenDenied,
        messageWhenDenied: messageWhenDenied,
        okLabelWhenDenied: okLabelWhenDenied);
    if (!allowPermission) return Future.value();
    XFile? xFile = await ImagePicker().pickImage(
        source: ImageSource.camera, maxWidth: maxWidth, maxHeight: maxHeight);
    return xFile;
  }

  /// 跳转到相机拍照（返回拍照完的路径）
  static Future<String?> jumpToCameraByPath(BuildContext context,
      {double? maxWidth,
      double? maxHeight,
      String? titleWhenDenied,
      String? messageWhenDenied,
      String? okLabelWhenDenied}) async {
    XFile? xFile = await jumpToCameraByFile(context,
        maxWidth: maxWidth,
        maxHeight: maxHeight,
        titleWhenDenied: titleWhenDenied,
        messageWhenDenied: messageWhenDenied,
        okLabelWhenDenied: okLabelWhenDenied);
    if (null == xFile) return Future.value();
    return xFile.path;
  }

  /// 跳转到相机拍照并裁剪（返回裁剪完的XFile）
  static Future<File?> jumpToCameraAndCropByFile(BuildContext context,
      {double? maxWidth,
      double? maxHeight,
      String? titleWhenDenied,
      String? messageWhenDenied,
      String? okLabelWhenDenied,
      int? cropMaxWidth,
      int? cropMaxHeight,
      bool isCircleCrop = false,
      double cropRatioX = 1,
      double cropRatioY = 1}) async {
    XFile? xFile = await jumpToCameraByFile(context,
        maxWidth: maxWidth,
        maxHeight: maxHeight,
        titleWhenDenied: titleWhenDenied,
        messageWhenDenied: messageWhenDenied,
        okLabelWhenDenied: okLabelWhenDenied);
    if (null == xFile) return Future.value();
    // 裁剪
    File? croppedFile = await jumpToCropPic(context, xFile.path,
        cropMaxWidth: cropMaxWidth,
        cropMaxHeight: cropMaxHeight,
        isCircleCrop: isCircleCrop,
        cropRatioX: cropRatioX,
        cropRatioY: cropRatioY);
    return croppedFile;
  }

  /// 跳转到相册--选单张（返回选中图片的路径）
  static Future<String?> jumpToSingleGalleryByPath(BuildContext context,
      {double? maxWidth,
      double? maxHeight,
      String? titleWhenDenied,
      String? messageWhenDenied,
      String? okLabelWhenDenied}) async {
    XFile? xFile = await jumpToSingleGalleryByFile(context,
        maxWidth: maxWidth,
        maxHeight: maxHeight,
        titleWhenDenied: titleWhenDenied,
        messageWhenDenied: messageWhenDenied,
        okLabelWhenDenied: okLabelWhenDenied);
    if (null == xFile) return Future.value();
    return xFile.path;
  }

  /// 跳转到相册--选单张（返回选中图片的XFile）
  static Future<XFile?> jumpToSingleGalleryByFile(BuildContext context,
      {double? maxWidth,
      double? maxHeight,
      String? titleWhenDenied,
      String? messageWhenDenied,
      String? okLabelWhenDenied}) async {
    // 检查相册权限
    bool allowPermission = await PermissionUtil.requestPhotosPermission(
        context: context,
        titleWhenDenied: titleWhenDenied,
        messageWhenDenied: messageWhenDenied,
        okLabelWhenDenied: okLabelWhenDenied);
    if (!allowPermission) return Future.value();
    XFile? xFile = await ImagePicker().pickImage(
        source: ImageSource.gallery, maxWidth: maxWidth, maxHeight: maxHeight);
    return xFile;
  }

  /// 跳转到相册--选单张并裁剪（返回裁剪完的File）
  static Future<File?> jumpToSingleGalleryAndCropByFile(BuildContext context,
      {double? maxWidth,
      double? maxHeight,
      String? titleWhenDenied,
      String? messageWhenDenied,
      String? okLabelWhenDenied,
      int? cropMaxWidth,
      int? cropMaxHeight,
      bool isCircleCrop = false,
      double cropRatioX = 1,
      double cropRatioY = 1}) async {
    XFile? xFile = await jumpToSingleGalleryByFile(context,
        maxWidth: maxWidth,
        maxHeight: maxHeight,
        titleWhenDenied: titleWhenDenied,
        messageWhenDenied: messageWhenDenied,
        okLabelWhenDenied: okLabelWhenDenied);
    if (null == xFile) return Future.value();
    // 裁剪
    File? croppedFile = await jumpToCropPic(context, xFile.path,
        cropMaxWidth: cropMaxWidth,
        cropMaxHeight: cropMaxHeight,
        isCircleCrop: isCircleCrop,
        cropRatioX: cropRatioX,
        cropRatioY: cropRatioY);
    return croppedFile;
  }

  /// 跳转到裁剪照片页面
  /// @param cropRatioX,cropRatioX 裁剪比例
  /// @param cropMaxWidth,cropMaxHeight 裁剪的最大宽高限制
  /// @param onChoose 获取选择的图片结果
  static Future<File?> jumpToCropPic(
    BuildContext context,
    String path, {
    int? cropMaxWidth,
    int? cropMaxHeight,
    bool isCircleCrop = false,
    double cropRatioX = 1,
    double cropRatioY = 1,
  }) async {
    if (isCircleCrop) cropRatioX = cropRatioY = 1;
    cropMaxWidth ??= MediaQuery.of(context).size.width.toInt();
    cropMaxHeight ??= MediaQuery.of(context).size.height.toInt();
    File? cropFile = await ImageCropper().cropImage(
        sourcePath: path,
        maxWidth: cropMaxWidth,
        maxHeight: cropMaxHeight,
        // compressFormat: ImageCompressFormat.png,
        // compressQuality: 50, // 质量压缩，png图片压缩无效
        cropStyle: isCircleCrop ? CropStyle.circle : CropStyle.rectangle,
        aspectRatio: CropAspectRatio(ratioX: cropRatioX, ratioY: cropRatioY));
    return cropFile;
  }

}
