package com.dengyun.baselibrary.base.view;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

/**
 * @titile  View层基类
 * @desc Created by seven on 2018/2/24.
 */

public interface BaseView {
    void toast(String msg);
    void toast(int msgId);
    void toastLong(String msg);
    void toastLong(int msgId);
    Context getContext();
    FragmentActivity getMyActivity();
    String getMyTag();
}
