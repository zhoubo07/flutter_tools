
/// @Title   返回码拦截
/// @Author: zhoubo
/// @CreateDate:  4/21/21 9:27 AM

/// 拦截返回码，
/// true：拦截-返回到error；false：不拦截
bool interceptCodeToError(int code, String? msg) {
  // 允许返回的在200后面增加
  switch (code) {
    case 200: // 正常返回
    case 20701003:  // 无最新版本信息
      // 不需要拦截的code，返回到success
      return false;
    case 11110011:
      // 接口不兼容，需升级
      // CommonDialog.showNeedUpdateDialog();
      return true;
    case 901001:
    case 9010001:
      // token 过期
      // SpUserInfo.removeUserInfo();
      // CommonRouter.jumpToLoginPageGlobal();
      return true;
    default:
      // 需要拦截的code，返回到error中处理
      return true;
  }
}
