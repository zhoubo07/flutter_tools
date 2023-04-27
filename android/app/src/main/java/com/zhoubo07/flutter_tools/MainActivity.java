package com.zhoubo07.flutter_tools;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhoubo07.flutter_tools.router.FlutterToNativePlugin;
import com.zhoubo07.flutter_tools.router.NativeToFlutterPlugin;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;

public class MainActivity extends FlutterActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        // 绑定Flutter调用原生的插件类
        FlutterToNativePlugin.registerWith(flutterEngine);
        // 绑定原生调用Flutter的插件类
        NativeToFlutterPlugin.registerWith(flutterEngine);

    }
}
