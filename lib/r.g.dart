// IT IS GENERATED BY FLR - DO NOT MODIFY BY HAND
// YOU CAN GET MORE DETAILS ABOUT FLR FROM:
// - https://github.com/Fly-Mix/flr-cli
// - https://github.com/Fly-Mix/flr-vscode-extension
// - https://github.com/Fly-Mix/flr-as-plugin
//

// ignore: unused_import
import 'package:flutter/widgets.dart';
// ignore: unused_import
import 'package:flutter/services.dart' show rootBundle;
// ignore: unused_import
import 'package:path/path.dart' as path;
// ignore: unused_import
import 'package:flutter_svg/flutter_svg.dart';
// ignore: unused_import
import 'package:r_dart_library/asset_svg.dart';

/// This `R` class is generated and contains references to static asset resources.
class R {
  /// package name: flutter_tools
  static const package = "flutter_tools";

  /// This `R.image` struct is generated, and contains static references to static non-svg type image asset resources.
  static const image = _R_Image();

  /// This `R.svg` struct is generated, and contains static references to static svg type image asset resources.
  static const svg = _R_Svg();

  /// This `R.text` struct is generated, and contains static references to static text asset resources.
  static const text = _R_Text();

  /// This `R.fontFamily` struct is generated, and contains static references to static font asset resources.
  static const fontFamily = _R_FontFamily();
}

/// Asset resource’s metadata class.
/// For example, here is the metadata of `packages/flutter_demo/assets/images/example.png` asset:
/// - packageName：flutter_demo
/// - assetName：assets/images/example.png
/// - fileDirname：assets/images
/// - fileBasename：example.png
/// - fileBasenameNoExtension：example
/// - fileExtname：.png
class AssetResource {
  /// Creates an object to hold the asset resource’s metadata.
  const AssetResource(this.assetName, {this.packageName});

  /// The name of the main asset from the set of asset resources to choose from.
  final String assetName;

  /// The name of the package from which the asset resource is included.
  final String? packageName;

  /// The name used to generate the key to obtain the asset resource. For local assets
  /// this is [assetName], and for assets from packages the [assetName] is
  /// prefixed 'packages/<package_name>/'.
  String get keyName =>
      packageName == null ? assetName : "packages/$packageName/$assetName";

  /// The file basename of the asset resource.
  String get fileBasename {
    final basename = path.basename(assetName);
    return basename;
  }

  /// The no extension file basename of the asset resource.
  String get fileBasenameNoExtension {
    final basenameWithoutExtension = path.basenameWithoutExtension(assetName);
    return basenameWithoutExtension;
  }

  /// The file extension name of the asset resource.
  String get fileExtname {
    final extension = path.extension(assetName);
    return extension;
  }

  /// The directory path name of the asset resource.
  String get fileDirname {
    var dirname = assetName;
    if (packageName != null) {
      final packageStr = "packages/$packageName/";
      dirname = dirname.replaceAll(packageStr, "");
    }
    final filenameStr = "$fileBasename/";
    dirname = dirname.replaceAll(filenameStr, "");
    return dirname;
  }
}

// ignore: camel_case_types
class _R_Image_AssetResource {
  const _R_Image_AssetResource();

  /// asset: assets/images/ic_back_black.png
  // ignore: non_constant_identifier_names
  final ic_back_black =
      const AssetResource("assets/images/ic_back_black.png", packageName: null);
}

// ignore: camel_case_types
class _R_Svg_AssetResource {
  const _R_Svg_AssetResource();

  /// asset: assets/images/icon_input_paste_normal.svg
  // ignore: non_constant_identifier_names
  final icon_input_paste_normal = const AssetResource(
      "assets/images/icon_input_paste_normal.svg",
      packageName: null);

  /// asset: assets/images/icon_links_clear.svg
  // ignore: non_constant_identifier_names
  final icon_links_clear = const AssetResource(
      "assets/images/icon_links_clear.svg",
      packageName: null);
}

// ignore: camel_case_types
class _R_Text_AssetResource {
  const _R_Text_AssetResource();

  /// asset: assets/lottie/loading.json
  // ignore: non_constant_identifier_names
  final loading_json =
      const AssetResource("assets/lottie/loading.json", packageName: null);
}

/// This `_R_Image` class is generated and contains references to static non-svg type image asset resources.
// ignore: camel_case_types
class _R_Image {
  const _R_Image();

  final asset = const _R_Image_AssetResource();

  /// asset: assets/images/ic_back_black.png
  // ignore: non_constant_identifier_names
  AssetImage ic_back_black() {
    return AssetImage(asset.ic_back_black.keyName);
  }
}

/// This `_R_Svg` class is generated and contains references to static svg type image asset resources.
// ignore: camel_case_types
class _R_Svg {
  const _R_Svg();

  final asset = const _R_Svg_AssetResource();

  /// asset: assets/images/icon_input_paste_normal.svg
  // ignore: non_constant_identifier_names
  AssetSvg icon_input_paste_normal(
      {required double width, required double height}) {
    final imageProvider = AssetSvg(asset.icon_input_paste_normal.keyName,
        width: width, height: height);
    return imageProvider;
  }

  /// asset: assets/images/icon_links_clear.svg
  // ignore: non_constant_identifier_names
  AssetSvg icon_links_clear({required double width, required double height}) {
    final imageProvider =
        AssetSvg(asset.icon_links_clear.keyName, width: width, height: height);
    return imageProvider;
  }
}

/// This `_R_Text` class is generated and contains references to static text asset resources.
// ignore: camel_case_types
class _R_Text {
  const _R_Text();

  final asset = const _R_Text_AssetResource();

  /// asset: assets/lottie/loading.json
  // ignore: non_constant_identifier_names
  Future<String> loading_json() {
    final str = rootBundle.loadString(asset.loading_json.keyName);
    return str;
  }
}

/// This `_R_FontFamily` class is generated and contains references to static font asset resources.
// ignore: camel_case_types
class _R_FontFamily {
  const _R_FontFamily();
}
