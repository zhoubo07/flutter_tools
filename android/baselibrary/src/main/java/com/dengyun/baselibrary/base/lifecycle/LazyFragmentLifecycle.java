package com.dengyun.baselibrary.base.lifecycle;

import android.view.View;

/**
 * @titile
 * @desc Created by seven on 2018/5/24.
 */

public interface LazyFragmentLifecycle extends FragmentLifecycle {
    void onLazyInitView(View view);
//    void onLazyInitData();
}
