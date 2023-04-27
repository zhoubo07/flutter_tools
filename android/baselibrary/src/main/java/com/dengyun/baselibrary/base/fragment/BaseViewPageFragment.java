package com.dengyun.baselibrary.base.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Title 适配 viewpage 的fragment
 * @Desc: 生命周期onSume和onPause的监测通过onUserVisible和onUserInvisible方法，
 *        （整合：左右滑动切换，home，下一页返回）
 * @Author: zhoubo
 * @CreateDate: 2020-04-10 17:44
 */
public abstract class BaseViewPageFragment extends BaseFragment {
    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isUserVisible = false;
    private boolean isPrepared = false;

    @Override
    public void initViews(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }

        if (getUserVisibleHint()) {
            isUserVisible = true;
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            if(isUserVisible){
                isUserVisible = false;
                onUserInvisible();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                isUserVisible = true;
                onUserVisible();
            }
        } else {
            if(isUserVisible){
                isUserVisible = false;
                onUserInvisible();
            }
        }
    }

    private void initPrepare() {
        if (isPrepared) {
            isUserVisible = true;
            onUserVisible();
        } else {
            isPrepared = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstResume = true;
        isFirstVisible = true;
        isUserVisible = false;
        isPrepared = false;
    }

    protected abstract void onUserVisible();
    protected abstract void onUserInvisible();
}
