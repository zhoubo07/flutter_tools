package com.dengyun.baselibrary.base.lifecycle;

import android.os.Bundle;
import androidx.annotation.Nullable;

/**
 * @titile
 * @desc Created by seven on 2018/5/23.
 */

public interface ActivityLifecycle {
    void onCreate(@Nullable Bundle savedInstanceState);
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();

    //下面两个是我们自己的生命周期
    void onInitViews(Bundle savedInstanceState);
//    void onInitData();
}
