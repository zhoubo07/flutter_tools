package com.dengyun.baselibrary.base.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dengyun.baselibrary.utils.AppLogUtil;

/**
 * @titile 懒加载的Fragment，
 * <p>
 * 使用条件：1：必须使用FragmentPageAdapter管理的Fragment才会触发懒加载生命周期
 * 2：经典使用场景是和ViewPage搭配使用，使用FragmentPageAdapter管理Fragment
 * 3：单独的Fragment，使用add、hide、show、replace管理的Fragment不可以使用此懒加载Fragmeng（不会触发setUserVisibleHint）
 * <p>
 * 懒加载的需求：
 * 普通的Fragment使用直接在正常的create方法做就可以，由于和ViewPage搭配使用时，会最少加载两个Fragment，
 * 即多个Fragment创建，不适合在create方法中加载data，因此根据显示与否，使用懒加载控制加载数据
 * @desc Created by seven on 2018/3/14.
 */

public abstract class BaseLazyFragment extends BaseFragment {

    //Fragment的View加载完毕的标记
    private boolean isViewCreated;

    //Fragment对用户可见的标记
    private boolean isUIVisible;

    //已经加载过一次
    private boolean hasLoadedOnce;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        onVisible();
    }

    @Deprecated
    @Override
    public void initViews(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    /**
     * Fragment当前状态是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            /*可见*/
            isUIVisible = true;
            onVisible();
        } else {
            /*不可见*/
            isUIVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        if (!isViewCreated || !isUIVisible || hasLoadedOnce) {
            return;
        }
        hasLoadedOnce = true;
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {
    }


    protected void lazyLoad() {
        AppLogUtil.setFragmentLCLog(getMyTag(), "lazyLoad");
        lazyInitView(getView());
    }

    protected abstract void lazyInitView(View view);

}
