import 'package:flutter/material.dart';
import 'package:lottie/lottie.dart';

import '../../../r.g.dart';

class LoadingView extends StatelessWidget {
  const LoadingView({Key? key, this.size = 96}) : super(key: key);

  final double size;

  @override
  Widget build(BuildContext context) {
    return Lottie.asset(
      R.text.asset.loading_json.assetName,
      package: R.package,
      width: size,
      height: size,
    );
  }
}
