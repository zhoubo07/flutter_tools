package com.dengyun.baselibrary.base.lifecycle;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @titile
 * @desc Created by seven on 2018/5/23.
 */

public interface FragmentLifecycle {
    void onAttach(Context context);
    void onCreate(Bundle savedInstanceState);
    void onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
    void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState);
    void onActivityCreated(@Nullable Bundle savedInstanceState);
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroyView();
    void onDestroy();
    void onDetach();

    //下面两个是我们自己的生命周期
    void onInitViews(@NonNull View view, @Nullable Bundle savedInstanceState);
//    void onInitData();
}
