package com.zhoubo07.flutter_tools.router;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.dengyun.baselibrary.utils.activity.ActivityUtils;
import com.idengyun.updatelib.bean.UpdateBean;
import com.idengyun.updatelib.update.UpdateUtils;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * @title flutter和原生交互的桥梁
 * @author: zhoubo
 * @CreateDate: 2020/12/1 2:11 PM
 */
public class FlutterToNativePlugin implements MethodChannel.MethodCallHandler {
    public static final String CHANNEL = "com.zhoubo07.flutter_tools"; // 渠道名
    
    /// 绑定flutter调用原生插件
    public static void registerWith( @NonNull FlutterEngine flutterEngine) {
        MethodChannel methodChannel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL);
        FlutterToNativePlugin instance = new FlutterToNativePlugin();
        methodChannel.setMethodCallHandler(instance);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        if (call.method.equals("showUpgradeDialog")) {
            // 弹出升级弹框
            showUpgradeDialog(call);
        } else {
            // result.success(xx);
            result.notImplemented();
        }
    }


    /**
     * 展示升级弹框
     */
    public void showUpgradeDialog(MethodCall call) {
        Activity activity = ActivityUtils.getTopActivity();
        if (!(activity instanceof FragmentActivity)) return;
        String title = call.argument("title");  //标题
        String message = call.argument("message");  //升级内容
        int isUpdate = call.argument("isUpdate");//是否有新版本：0：没有，1：有
        int isForce = call.argument("isForce");//是否强更 0 不需要，1 需要
        String versionName = call.argument("versionName");//新版本号
        String apkUrl = call.argument("apkUrl");//更新连接
        UpdateBean updateBean = new UpdateBean();
        updateBean.setTitle(title);
        updateBean.setMessage(message);
        updateBean.setIsUpdate(isUpdate);
        updateBean.setIsForce(isForce);
        updateBean.setVersionName(versionName);
        updateBean.setApkUrl(apkUrl);
        UpdateUtils.showUpdateDialog(((FragmentActivity) activity).getSupportFragmentManager(), updateBean);
    }


}
