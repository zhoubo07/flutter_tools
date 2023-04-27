package com.zhoubo07.flutter_tools.router;

import static com.zhoubo07.flutter_tools.router.FlutterToNativePlugin.CHANNEL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

/**
 * @title: 原生调用Flutter的插件类
 * @author: zhoubo
 * @CreateDate: 12/21/21 2:03 PM
 */
public class NativeToFlutterPlugin {

    /// 用于调用Flutter的方法渠道
    private static MethodChannel nativeToFlutterMethodChannel;

    /// 绑定原生调用flutter插件
    public static void registerWith(@NonNull FlutterEngine flutterEngine) {
        nativeToFlutterMethodChannel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL);
    }

    /// 原生调用Flutter方法
    public static void invokeMethod(@NonNull String method, @Nullable Object arguments) {
        invokeMethod(method, arguments, null);
    }

    /// 原生调用Flutter方法
    public static void invokeMethod(String method, @Nullable Object arguments, @Nullable MethodChannel.Result callback) {
        try {
            nativeToFlutterMethodChannel.invokeMethod(method, arguments, callback);
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    /**
     * 连续定位经纬度变化的回调，传回到Flutter中
     *
     * @param latitude  纬度
     * @param longitude 经度
     */
    public static void onLocationChanged(double latitude, double longitude) {
        invokeMethod("onLocationChanged", latitude + "," + longitude);
    }

}
